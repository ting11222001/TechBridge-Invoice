# DEV-NOTES.md

Developer notes and references for the TechBridge Invoice backend.

---

## SQL - Derived Tables & Stats Query

Tried using derived tables (temporary tables created from a subquery):
```sql
SELECT COUNT(*) total_customers FROM customer;
```

Each of these produces a single-row table:
```sql
(SELECT COUNT(*) total_customers FROM customer) c
(SELECT COUNT(*) total_invoices FROM invoice) i
(SELECT ROUND(SUM(total)) total_billed FROM invoice) inv
```

Can cross join them in the `FROM` clause:
```sql
SELECT
    c.total_customers,
    i.total_invoices,
    inv.total_billed
FROM
    (SELECT COUNT(*) total_customers FROM customer) c,
    (SELECT COUNT(*) total_invoices FROM invoice) i,
    (SELECT ROUND(SUM(total)) total_billed FROM invoice) inv;
```

Or cleaner using scalar subqueries (preferred, used in `CustomerQuery.java`):
```sql
SELECT
    (SELECT COUNT(*) FROM customer) AS total_customers,
    (SELECT COUNT(*) FROM invoice) AS total_invoices,
    (SELECT ROUND(SUM(total)) FROM invoice) AS total_billed;
```

---

## application.yml - Config & Profile Strategy

`application.yml` is the base file - shared config referenced by both dev and prod:
```yaml
spring:
  profiles:
    active: @spring.profiles.active@   # Maven-filtered at build time

server:
  port: ${PORT:8080}

application:
  title: TechBridge-Invoices
  version: 1.0

twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}   # IntelliJ: Edit Configurations > Environment Variables
  auth-token: ${TWILIO_AUTH_TOKEN}
  from-number: ${TWILIO_FROM_NUMBER}

jwt:
  secret: ${MY_JWT_SECRET}             # IntelliJ: Edit Configurations > Environment Variables

allowed:
  origins: ${ALLOWED_ORIGINS:http://localhost:4200}   # override with Vercel frontend URL in prod
```

### How Spring picks the YAML

`pom.xml` sets `dev` as the default active profile:
```xml
<profile>
    <id>dev</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
        <spring.profiles.active>dev</spring.profiles.active>
    </properties>
</profile>
```

Locally Spring loads: `application.yml` + `application-dev.yml`

On Railway (`SPRING_PROFILES_ACTIVE=prod`): `application.yml` + `application-prod.yml`

### application-dev.yml
```yaml
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/dev_db_techbridge_invoice
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
    jpa:
        hibernate:
            ddl-auto: update
        show-sql: true
    sql:
        init:
            mode: never       # change to "always" to re-run schema.sql (delete existing tables first)
            schema-locations: classpath:schema.sql
            continue-on-error: false
```

### application-prod.yml
```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
  jpa:
    hibernate:
        ddl-auto: update
    show-sql: true
  sql:
    init:
        mode: ${SQL_INITIALISE_MODE}    # set to 'always' first deploy, then 'never'
        schema-locations: classpath:schema-prod.sql   # no CREATE SCHEMA / USE statements
        continue-on-error: false
```

### Railway env variables required
```
MY_JWT_SECRET
SPRING_DATASOURCE_PASSWORD
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_JPA_HIBERNATE_DDL_AUTO
SPRING_JPA_SHOW_SQL
SPRING_PROFILES_ACTIVE
SQL_INITIALISE_MODE
TWILIO_ACCOUNT_SID
TWILIO_AUTH_TOKEN
TWILIO_FROM_NUMBER
```

### Other
Spring Boot Banner Generator: https://devops.datenkollektiv.de/banner.txt/index.html

---

## pom.xml - Key Dependencies

- Apache Commons libraries: https://commons.apache.org/
- Apache Commons Lang: https://central.sonatype.com/artifact/org.apache.commons/commons-lang3/3.20.0/jar
- Twilio library: https://mvnrepository.com/artifact/com.twilio.sdk/twilio

---

## Twilio 2FA - Notes & Costs

Twilio operates on a prepaid model.

- Phone numbers incur a monthly rental fee (USD $6.5/month).
- SMS messages billed per message from prepaid balance. International SMS may cost more.
- Trial accounts restricted to verified recipient numbers only.
- My Twilio number is an Australian number. Real Australian number saved in DB for testing.

For a test user to trigger MFA, these must be set manually in the `Users` table:
- `using_mfa = 1` (when `0`, login works with just email + password)
- `enabled = 1`
- `non_locked = 1`

Twilio env settings are in `application-dev.yml` only (not prod yet).

Billing: using Wise USD account. Auto-recharge is off; some balance remaining. Monthly rental continues from that balance.

---

## JWT Token - Libraries & Debugging

Using Auth0 library for JWT creation:
- Repo: https://github.com/auth0/java-jwt
- Maven: https://mvnrepository.com/artifact/com.auth0/java-jwt
- JWT Debugger (decode access tokens from Postman): https://www.jwt.io/

---

## JPA Mapping - Mental Model

```
HTTP request
↓
Controller
↓
Service
↓
Create/modify Java object
↓
Repository.save(object)
↓
JPA converts object → SQL
↓
Database
```

The database never sees anything until `save()` is called. Example:

```java
public Invoice createInvoice(Invoice invoice)
```

`invoice` is a Java object in memory - not in the database yet:
```
Invoice object
------------------------
id = null
invoiceNumber = null
services = "Consulting"
date = 2026-03-10
status = "PENDING"
total = 200
customer = Customer#1
```

Setting a field modifies the in-memory object only:
```java
invoice.setInvoiceNumber("INV-" + RandomStringUtils.secure().nextAlphanumeric(8).toUpperCase());
```

Then `invoiceRepository.save(invoice)` triggers JPA to INSERT with whatever values are on the object at that point:
```sql
INSERT INTO invoice (invoice_number, services, date, status, total, customer_id)
VALUES ('INV-A8F4K2L1', 'Consulting', '2026-03-10', 'PENDING', 200, 1);
```

---

## DTO & Principal - Mental Model

### Where the principal comes from

In `CustomAuthorizationFilter`:
```java
Authentication authentication = tokenProvider.getAuthentication(userId, authorities, request);
```

`getAuthentication()` creates:
```java
public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken userPasswordAuthToken =
        new UsernamePasswordAuthenticationToken(
            userService.getUserById(userId),   // ← this becomes the principal
            null,
            authorities
        );
    userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return userPasswordAuthToken;
}
```

`userService.getUserById(userId)` returns a `UserDTO`, so:
```java
UserDTO user = (UserDTO) authentication.getPrincipal();
```
is always safe.

### Full pipeline
```
JWT token
↓
filter extracts userId
↓
userService.getUserById(userId)
↓
UserDTO created
↓
Authentication(principal = UserDTO)
↓
SecurityContextHolder
↓
Controller
↓
authentication.getPrincipal() → UserDTO
```

The type of principal depends entirely on what is passed into `UsernamePasswordAuthenticationToken`.

---

## Seeding MySQL on Railway

### Connect via DBeaver

In DBeaver, click **New Connection** → MySQL, then fill in:
```
Host:     mainline.proxy.rlwy.net
Port:     50452
Database: railway
Username: root
Password: (reveal from Railway dashboard)
```
Click **Test Connection** → **OK**.

### Seed data

Follow `data.sql`. Drop the `dev_db_techbridge_invoice` or `railway` prefix from any schema-qualified names - Spring Boot runs scripts against whichever database is in `application.properties`, so hardcoding the schema name causes errors across environments.

### Verify via Postman

Register a user against the prod API:
```
POST https://techbridge-invoice-production.up.railway.app/user/register
```
A new entry should appear in the `Users` table on Railway.
