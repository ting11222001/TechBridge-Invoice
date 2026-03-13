# application.yml

This is the base file:
```
spring:
  profiles:
    active: @spring.profiles.active@

  server:
    port: ${PORT:8080}  → added this for Railway as Railway runs my container on a dynamic port, not always 8080. So locally if PORT not defined → 8080
    
  application:
    title: TechBridge-Invoices
    version: 1.0
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
            mode: never → change this to "always" when you want to run the schema.sql file to populate the database again (remember to delete all the existing tables first).
            continue-on-error: false
            
twilio:
    account-sid: ${TWILIO_ACCOUNT_SID} → add these values in the intellij > Edit Configurations > Environment Variables (TWILIO_ACCOUNT_SID=xxx;TWILIO_AUTH_TOKEN=xxx)
    auth-token: ${TWILIO_AUTH_TOKEN}
    from-number: ${TWILIO_FROM_NUMBER}
    
jwt:
  secret: ${MY_JWT_SECRET}  → add this in the intellij > Edit Configurations > Environment Variables (MY_JWT_SECRET=xxx)
```

# application-prod.yml

Make sure Railway has backend container running with these variables (e.g. `SPRING_DATASOURCE_URL` which should have the same value from the MySQL database container on Railway):
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
      mode: never
      continue-on-error: false
```

Online Spring Boot Banner Generator:
https://devops.datenkollektiv.de/banner.txt/index.html