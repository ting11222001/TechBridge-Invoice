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


# Queries

Follow the `02-sql-script.md`.

Replace `dev_db_techbridge_invoice` with `railway` this database name in DBeaver.

E.g. 
```
INSERT INTO railway.Roles (name, permission)
VALUES ('ROLE_USER', 'READ:USER,READ:CUSTOMER'),
('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
('ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
('ROLE_SYSADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');
```

Seed with these queries in the `02-sql-script.md`:
- Populate the Roles table
- Populate the Customers table
- Populate the Invoice table


# Postman

Create a new user using postman.

Add a `post` method with this URL:
```
https://techbridge-invoice-production.up.railway.app/user/register
```

A new entry should show up in the `Users` table on Railway.