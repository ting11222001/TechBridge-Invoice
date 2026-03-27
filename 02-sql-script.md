# Derived tables

Tried this which is called a derived table - a temporary table created from a subquery:
```
SELECT COUNT(*) total_customers FROM customer;
```

And each of these produce a single-row table:
```
(SELECT COUNT(*) total_customers FROM customer) c
(SELECT COUNT(*) total_invoices FROM invoice) i
(SELECT ROUND(SUM(total)) total_billed FROM invoice) inv
```

Then I can cross join them in the `FROM` clause like this:
```
FROM
(subquery) c,
(subquery) i,
(subquery) inv
```

So I can write this:
```
SELECT 
    c.total_customers, 
    i.total_invoices, 
    inv.total_billed
FROM
    (SELECT COUNT(*) total_customers FROM customer) c, 
    (SELECT COUNT(*) total_invoices FROM invoice) i, 
    (SELECT ROUND(SUM(total)) total_billed FROM invoice) inv;
```

or cleaner like this:
```
SELECT 
	(SELECT COUNT(*) FROM customer) AS total_customers,
	(SELECT COUNT(*) FROM invoice) AS total_invoices,
	(SELECT ROUND(SUM(total)) FROM invoice) AS total_billed;
```