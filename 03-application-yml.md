```
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/dev_db_techbridge_invoice
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
```

Online Spring Boot Banner Generator:
https://devops.datenkollektiv.de/banner.txt/index.html