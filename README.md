# TechBridge: Invoice Module (Backend)

TechBridge is a device donation platform connecting businesses, refurbishers, and schools. This repo is the backend for the Invoice module. It provides the REST API, JWT authentication, and database layer used by the Angular frontend.

![Backend CI](https://github.com/ting11222001/TechBridge-Invoice/actions/workflows/backend-ci.yml/badge.svg)

[About the TechBridge project](https://your-landing-page-url) · [Invoice Module Live Demo](https://tech-bridge-invoice-app.vercel.app/) · [Invoice Module (Frontend repo)](https://github.com/ting11222001/TechBridge-Invoice-app) · Donation Module (coming soon)

> Full project docs (architecture, deployment, role/permission reference) are in the [frontend repo](https://github.com/ting11222001/TechBridge-Invoice-app).

<!-- --- -->

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 4.0.2 (Java 17), Spring Security, Lombok |
| Auth | JWT (auth0 java-jwt), Twilio MFA/SMS |
| Database | MySQL 8.0 |
| Deploy | Railway (backend + DB), GitHub Actions CI |

<!-- --- -->

## Getting Started

**Prerequisites:** Java 17, IntelliJ IDEA, Docker

**1. Start the local MySQL container**
```bash
docker run --name mysql-techbridge \
  -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
  -e MYSQL_DATABASE=dev_db_techbridge_invoice \
  -p 3306:3306 -d mysql:8.0
```

**2. Set environment variables in IntelliJ**

Go to `Run > Edit Configurations > Environment Variables` and add:
```
DB_USERNAME=root
DB_PASSWORD=<your-root-password>
MY_JWT_SECRET=<your-secret>
TWILIO_ACCOUNT_SID=<sid>
TWILIO_AUTH_TOKEN=<token>
TWILIO_FROM_NUMBER=<number>
```

**3. Run the app**

Run `TechBridgeInvoiceApplication` from IntelliJ. The API starts at `http://localhost:8080`.

> For Railway deployment and environment variable setup, see [docs/DEPLOYMENT.md](https://github.com/ting11222001/TechBridge-Invoice-app/blob/main/docs/DEPLOYMENT.md) in the frontend repo.