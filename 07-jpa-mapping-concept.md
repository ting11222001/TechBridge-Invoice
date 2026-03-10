# Mental model

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

The database never sees anything until `save()`, e.g.:
```
invoiceRepository.save(invoice);
```

# Example

When your method receives this:

```
public Invoice createInvoice(Invoice invoice)
```
`invoice` is simply a Java object in memory.

Think of it like:
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
It is not in the database yet. It only lives in the JVM.

2. So when you do this:
```
   invoice.setInvoiceNumber("INV-" + RandomStringUtils.secure().nextAlphanumeric(8).toUpperCase());
```
You are simply modifying that object in memory.

After that line the object becomes:
```
Invoice object
------------------------
id = null
invoiceNumber = "INV-A8F4K2L1"
services = "Consulting"
...
```
Nothing database-related has happened yet.

3. Then you pass that object to JPA
```
   invoiceRepository.save(invoice);
```

Spring Data JPA then:
```
Takes your current state of the object

Maps it to SQL

Inserts it into the database
```
Conceptually it becomes something like:
```
INSERT INTO invoice
(invoice_number, services, date, status, total, customer_id)
VALUES
('INV-A8F4K2L1', 'Consulting', '2026-03-10', 'PENDING', 200, 1);
```
So whatever values exist on the object at save time get persisted.