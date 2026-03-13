# application.yml

This is the base file - put everything that should be referenced along with either dev or prod environment here:
```
spring:
  profiles:
    active: @spring.profiles.active@

server:
  port: ${PORT:8080}

application:
  title: TechBridge-Invoices
  version: 1.0

twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}  → add these values in the intellij > Edit Configurations > Environment Variables (TWILIO_ACCOUNT_SID=xxx;TWILIO_AUTH_TOKEN=xxx)
  auth-token: ${TWILIO_AUTH_TOKEN}
  from-number: ${TWILIO_FROM_NUMBER}

jwt:
  secret: ${MY_JWT_SECRET}  → add this in the intellij > Edit Configurations > Environment Variables (MY_JWT_SECRET=xxx)
```

# How Spring decides which YAML to use

In the `pom.xml` file, when profile = dev, it specifies to run application-dev.yml first:
```
<profiles>
		<profile>
			<id>dev</id>
			<activation>
				<activeByDefault>true</activeByDefault> → here sets dev to active
			</activation>
			<properties>
				<spring.profiles.active>dev</spring.profiles.active>
			</properties>
		</profile>
		...
	</profiles>
```

So locally, Spring loads:
```
application.yml
application-dev.yml
```

## Production build

When Railway builds, I've activated prod profile with:
```
SPRING_PROFILES_ACTIVE=prod
```

Then Spring loads:
```
application.yml
application-prod.yml
```

# application-dev.yml

```
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/dev_db_techbridge_invoice  → 3306 should be the same port as the MySQL container you have locally and dev_db_techbridge_invoice should be the same name as the database name in Beaver
        username: ${DB_USERNAME}  → add these values in the intellij > Edit Configurations > Environment Variables (DB_USERNAME=xxx;DB_PASSWORD=xxx)
        password: ${DB_PASSWORD}

    jpa:
        hibernate:
            ddl-auto: update
        show-sql: true
    
    sql:
        init:
            mode: never    → change this to "always" when you want to run the schema.sql file to populate the database again (remember to delete all the existing tables first).
            chema-locations: classpath:schema.sql    → explicitly tell it to use this database script to initialise some tables
            continue-on-error: false
```

# application-prod.yml

Make sure Railway has backend container running with these variables:
- e.g. `SPRING_DATASOURCE_URL` which should have the same value from the MySQL database container on Railway
- 
```
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
        mode: ${SQL_INITIALISE_MODE}    → on Railway, set this to 'always' for the first time, and then set it back to 'never'
        schema-locations: classpath:schema-prod.sql     → this one removed the specific local database name as on Railway's MySQL database it will just have one database
        continue-on-error: false
```

# Railway for backend spring boot container

It should make sure all the env variables are added in the settings across:
- application.yml
- application-prod.yml

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

# Others

Online Spring Boot Banner Generator:
https://devops.datenkollektiv.de/banner.txt/index.html