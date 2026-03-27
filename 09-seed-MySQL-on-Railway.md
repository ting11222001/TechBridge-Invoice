# Railway

Spin up a MySQL container.

Go to `Database` tab.

Click `Connect

# DBeaver

In DBeaver, click `New Connection`, choose `MySQL`.

In the Connection Settings window, fill in:
```
Host: mainline.proxy.rlwy.net
Port: 50452
Database: railway
Username: root
Password: click "show" on Railway to reveal it
```

Then, click `Test Connection`. Once successful, click `OK`.


# Queries to seed the database for Roles, Users, Customer, Invoice tables

Follow the `data.sql`.

Drop the `dev_db_techbridge_invoice` or `railway` prefix in `data.sql`.

Spring Boot runs it against whichever database is configured in application.properties, so hardcoding the schema name can cause errors in other environments.

# Postman

Create a new user using postman.

Add a `post` method with this URL:
```
https://techbridge-invoice-production.up.railway.app/user/register
```

A new entry should show up in the `Users` table on Railway.