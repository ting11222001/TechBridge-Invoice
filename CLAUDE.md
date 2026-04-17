# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Spring Boot REST API backend for TechBridge Invoice — a prototype to manage customers and invoices for a technology donation platform. The frontend (Angular, port 4200) is a separate project.

## Build & Run Commands

```bash
# Build (skipping tests)
./mvnw clean package -DskipTests

# Run with dev profile (default)
./mvnw spring-boot:run

# Run with prod profile
./mvnw spring-boot:run -Pprod

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=TechbridgeInvoiceApplicationTests

# Build for prod
./mvnw clean package -Pprod -DskipTests
```

## Required Environment Variables

### Dev (set locally or in IDE run config)
- `DB_USERNAME`, `DB_PASSWORD` — MySQL credentials for `dev_db_techbridge_invoice` at `localhost:3306`
- `MY_JWT_SECRET` — HMAC512 secret for JWT signing
- `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN`, `TWILIO_FROM_NUMBER` — SMS/MFA (can be dummy values if MFA not used)
- `ALLOWED_ORIGINS` — defaults to `http://localhost:4200`

### Prod (Railway deployment)
- All of the above plus: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `SPRING_JPA_HIBERNATE_DDL_AUTO`, `SPRING_JPA_SHOW_SQL`, `SQL_INITIALISE_MODE`, `PORT`

## Database

- MySQL. Dev schema: `dev_db_techbridge_invoice`, initialized manually via `src/main/resources/schema.sql`.
- In dev, `spring.jpa.hibernate.ddl-auto=update` and `spring.sql.init.mode=never` — schema changes must be applied manually by running `schema.sql`.
- In prod, uses `schema-prod.sql` (no `CREATE SCHEMA` or `USE` statements; database is provided by Railway).
- The `schema.sql` file uses `DROP TABLE IF EXISTS` before each `CREATE TABLE` — re-running it wipes all data.

## Architecture

### Layer structure
```
resource/      → REST controllers (@RestController)
service/       → Business logic interfaces + implementations
repository/    → Data access interfaces + implementations (raw JDBC, no JPA entities)
domain/        → Plain Java domain objects (User, Role, Customer, Invoice, Stats, etc.)
dto/           → UserDTO (safe view of User, includes role/permissions, no password)
dtoMapper/     → UserDTOMapper (User + Role → UserDTO)
query/         → SQL query string constants (UserQuery, RoleQuery, CustomerQuery)
rowMapper/     → Spring RowMapper implementations to map ResultSet → domain objects
filter/        → CustomAuthorizationFilter (JWT extraction per request)
configuration/ → SecurityConfig (Spring Security filter chain, CORS)
provider/      → TokenProvider (Auth0 JWT: create/validate access + refresh tokens)
handler/       → CustomAuthenticationEntryPoint (401), CustomAccessDeniedHandler (403)
form/          → Request body POJOs with validation (LoginForm, UpdateForm)
enumeration/   → RoleType (ROLE_ADMIN, ROLE_USER, ROLE_MANAGER, ROLE_SYSADMIN), VerificationType
exception/     → ApiException, HandleException (@RestControllerAdvice)
utils/         → UserUtils, SmsService (Twilio), VerificationCodeGenerator, ExceptionUtils
```

### Data access pattern — mixed approach
**Customer and Invoice**: Standard Spring Data JPA (`PagingAndSortingRepository`, `ListCrudRepository`) with `@Entity` classes. Tables are created/managed by Hibernate (`ddl-auto=update` in dev), **not** in `schema.sql`.

**User and Role**: Custom JDBC via `NamedParameterJdbcTemplate` with named parameters (`:paramName`). Domain objects are plain `@Data @SuperBuilder` Lombok classes mapped via `RowMapper` implementations (`UserRowMapper`, `RoleRowMapper`, `StatsRowMapper`). SQL strings live in `*Query.java` constants classes. These tables (Users, Roles, UserRoles, AccountVerifications, ResetPasswordVerifications, TwoFactorVerifications, UserEvents, Events) are defined in `schema.sql`.

### Authentication & Authorization flow
1. **Login**: `POST /user/login` → `AuthenticationManager` → `UserDetailsService.loadUserByUsername()` → returns `UserPrincipal` (wraps `User` + `Role`).
2. **Token creation**: `TokenProvider.createAccessToken()` embeds user ID as `subject` and permissions string as a JWT claim. Access token expires in 30 min; refresh token in 5 days.
3. **Per-request auth**: `CustomAuthorizationFilter` (runs before `UsernamePasswordAuthenticationFilter`) extracts the `Bearer` token, calls `tokenProvider.getAuthorities()` to parse permissions from the JWT claim, and sets `SecurityContextHolder`.
4. **MFA**: If `user.isUsingMfa()` is true at login, a verification code is generated and (optionally) sent via Twilio SMS. The full token is only issued after `GET /user/verify/code/{email}/{code}`.

### RBAC — current state (incomplete)
- Infrastructure exists: `Role.permission` is a comma-separated authority string (e.g., `"READ:USER,DELETE:CUSTOMER"`). `UserPrincipal.getAuthorities()` splits this into `SimpleGrantedAuthority` objects, which are embedded in JWT tokens.
- `@EnableMethodSecurity` is on, so `@PreAuthorize("hasAuthority('PERMISSION')")` will work on any controller or service method.
- Currently only two paths are restricted in `SecurityConfig`:
  - `DELETE /user/delete/**` → requires `DELETE:USER`
  - `DELETE /customer/delete/**` → requires `DELETE:CUSTOMER`
- All other authenticated endpoints in `CustomerResource` and `UserResource` have **no role restrictions** yet. To add restrictions, use `@PreAuthorize` on controller methods — the plumbing already supports it.

### Response envelope
All endpoints return `HttpResponse` (a builder-style wrapper with `timestamp`, `status`, `statusCode`, `message`, `developerMessage`, `data: Map<String, Object>`). The `data` map typically includes a `"user"` key (current `UserDTO`) alongside the main payload.

### Key design decisions
- `UserDTO` (not `User`) is used in all API responses and as `@AuthenticationPrincipal` in controllers — it excludes the password and includes `roleName` / `permissions`.
- `UserPrincipal.getUser()` returns a `UserDTO`, used by `TokenProvider` to reconstruct the principal from a refresh token.
- New users are always assigned `ROLE_USER` on registration; `ROLE_ADMIN` must be assigned manually in the database.
- The `UserRoles` table has a `UNIQUE` constraint on `user_id` — each user has exactly one role.
- `Invoice.invoiceNumber` is auto-generated as `INV-{8-char-random}` in the service layer on creation.
- `SmsService` normalizes Australian phone numbers (e.g., `0412...` → `+61412...`) before calling Twilio.

### Public endpoints (no auth required)
`/user/login/**`, `/user/register/**`, `/user/verify/code/**`, `/user/resetpassword/**`, `/user/verify/password/**`, `/user/verify/account/**`, `/user/refresh/token/**`

### CI/CD
GitHub Actions (`.github/workflows/backend-ci.yml`) runs `./mvnw package -DskipTests` on push/PR to `main`. No deploy step — Railway auto-deploys from the main branch via Nixpacks.

### Seed data
`src/main/resources/data.sql` contains 4 users and 15 customers (mix of BUSINESS/INDIVIDUAL, statuses ACTIVE/INACTIVE/PENDING). All seed users have password `Demo@2026` (BCrypt). This file is not auto-run; apply manually when needed.


## Deployment Strategy

- main branch → Railway auto-deploys via Nixpacks (no Dockerfile)
- Frontend → Vercel