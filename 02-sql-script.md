# Populate the Roles table
```
INSERT INTO dev_db_techbridge_invoice.Roles (name, permission)
VALUES ('ROLE_USER', 'READ:USER,READ:CUSTOMER'),
('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
('ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
('ROLE_SYSADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');
```

# Populate the Customers table
```

INSERT INTO dev_db_techbridge_invoice.customer
(address, created_at, email, image_url, name, phone, status, type)
VALUES
('12 King William St, Adelaide','2026-03-11 10:00:00','oliver@gmail.com','https://cdn-icons-png.flaticon.com/512/149/149071.png','Oliver Brown','0400000001','ACTIVE','BUSINESS'),
('25 Rundle Mall, Adelaide','2026-03-11 10:01:00','charlotte@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922510.png','Charlotte Wilson','0400000002','ACTIVE','BUSINESS'),
('78 Grenfell St, Adelaide','2026-03-11 10:02:00','liam@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922561.png','Liam Johnson','0400000003','ACTIVE','PRIVATE'),
('14 Hindley St, Adelaide','2026-03-11 10:03:00','amelia@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922688.png','Amelia Davis','0400000004','ACTIVE','PRIVATE'),
('55 North Terrace, Adelaide','2026-03-11 10:04:00','noah@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922656.png','Noah Miller','0400000005','ACTIVE','BUSINESS'),
('103 Pulteney St, Adelaide','2026-03-11 10:05:00','sophia@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922701.png','Sophia Anderson','0400000006','ACTIVE','PRIVATE'),
('90 Wakefield St, Adelaide','2026-03-11 10:06:00','james@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922515.png','James Taylor','0400000007','ACTIVE','BUSINESS'),
('33 Frome Rd, Adelaide','2026-03-11 10:07:00','isabella@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922668.png','Isabella Thomas','0400000008','ACTIVE','PRIVATE'),
('16 Halifax St, Adelaide','2026-03-11 10:08:00','benjamin@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922573.png','Benjamin Moore','0400000009','ACTIVE','BUSINESS'),
('82 Grote St, Adelaide','2026-03-11 10:09:00','mia@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922566.png','Mia Martin','0400000010','ACTIVE','PRIVATE'),
('44 Gouger St, Adelaide','2026-03-11 10:10:00','lucas@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922682.png','Lucas White','0400000011','ACTIVE','BUSINESS'),
('21 Currie St, Adelaide','2026-03-11 10:11:00','harper@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922522.png','Harper Thompson','0400000012','ACTIVE','PRIVATE'),
('62 Waymouth St, Adelaide','2026-03-11 10:12:00','henry@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922578.png','Henry Garcia','0400000013','ACTIVE','BUSINESS'),
('75 Morphett St, Adelaide','2026-03-11 10:13:00','evelyn@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922560.png','Evelyn Martinez','0400000014','ACTIVE','PRIVATE'),
('19 Hutt St, Adelaide','2026-03-11 10:14:00','alexander@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922651.png','Alexander Robinson','0400000015','ACTIVE','BUSINESS'),
('48 King William Rd, Adelaide','2026-03-11 10:15:00','abigail@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922685.png','Abigail Clark','0400000016','ACTIVE','PRIVATE'),
('34 Payneham Rd, Adelaide','2026-03-11 10:16:00','daniel@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922526.png','Daniel Rodriguez','0400000017','ACTIVE','BUSINESS'),
('89 Magill Rd, Adelaide','2026-03-11 10:17:00','emily@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922705.png','Emily Lewis','0400000018','ACTIVE','PRIVATE'),
('51 Prospect Rd, Adelaide','2026-03-11 10:18:00','matthew@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922570.png','Matthew Lee','0400000019','ACTIVE','BUSINESS'),
('28 Unley Rd, Adelaide','2026-03-11 10:19:00','scarlett@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922664.png','Scarlett Walker','0400000020','ACTIVE','PRIVATE'),
('102 Glen Osmond Rd, Adelaide','2026-03-11 10:20:00','david@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922587.png','David Hall','0400000021','ACTIVE','BUSINESS'),
('71 Goodwood Rd, Adelaide','2026-03-11 10:21:00','grace@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922520.png','Grace Allen','0400000022','ACTIVE','PRIVATE'),
('9 Kensington Rd, Adelaide','2026-03-11 10:22:00','joseph@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922680.png','Joseph Young','0400000023','ACTIVE','BUSINESS'),
('58 Port Rd, Adelaide','2026-03-11 10:23:00','chloe@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922568.png','Chloe Hernandez','0400000024','ACTIVE','PRIVATE'),
('86 Main North Rd, Adelaide','2026-03-11 10:24:00','andrew@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922654.png','Andrew King','0400000025','ACTIVE','BUSINESS'),
('17 Brighton Rd, Adelaide','2026-03-11 10:25:00','zoe@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922689.png','Zoe Wright','0400000026','ACTIVE','PRIVATE'),
('43 Marion Rd, Adelaide','2026-03-11 10:26:00','joshua@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922517.png','Joshua Scott','0400000027','ACTIVE','BUSINESS'),
('67 Henley Beach Rd, Adelaide','2026-03-11 10:27:00','hannah@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922662.png','Hannah Green','0400000028','ACTIVE','PRIVATE'),
('22 Tapleys Hill Rd, Adelaide','2026-03-11 10:28:00','christopher@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922575.png','Christopher Adams','0400000029','ACTIVE','BUSINESS'),
('31 Grange Rd, Adelaide','2026-03-11 10:29:00','victoria@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922563.png','Victoria Baker','0400000030','ACTIVE','PRIVATE'),
('11 Semaphore Rd, Adelaide','2026-03-11 10:30:00','nathan@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922700.png','Nathan Nelson','0400000031','ACTIVE','BUSINESS'),
('59 Salisbury Hwy, Adelaide','2026-03-11 10:31:00','lily@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922529.png','Lily Carter','0400000032','ACTIVE','PRIVATE'),
('93 Grand Junction Rd, Adelaide','2026-03-11 10:32:00','ryan@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922576.png','Ryan Mitchell','0400000033','ACTIVE','BUSINESS'),
('15 Regency Rd, Adelaide','2026-03-11 10:33:00','ella@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922658.png','Ella Perez','0400000034','ACTIVE','PRIVATE'),
('40 Churchill Rd, Adelaide','2026-03-11 10:34:00','aaron@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922686.png','Aaron Roberts','0400000035','ACTIVE','BUSINESS'),
('26 Prospect Rd, Adelaide','2026-03-11 10:35:00','samantha@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922514.png','Samantha Turner','0400000036','ACTIVE','PRIVATE'),
('18 Payneham Rd, Adelaide','2026-03-11 10:36:00','justin@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922667.png','Justin Phillips','0400000037','ACTIVE','BUSINESS'),
('60 Magill Rd, Adelaide','2026-03-11 10:37:00','ava@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922572.png','Ava Campbell','0400000038','ACTIVE','PRIVATE'),
('73 Kensington Rd, Adelaide','2026-03-11 10:38:00','tyler@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922567.png','Tyler Parker','0400000039','ACTIVE','BUSINESS'),
('36 Unley Rd, Adelaide','2026-03-11 10:39:00','lucy@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922681.png','Lucy Evans','0400000040','ACTIVE','PRIVATE'),
('12 Glen Osmond Rd, Adelaide','2026-03-11 10:40:00','adam@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922521.png','Adam Edwards','0400000041','ACTIVE','BUSINESS'),
('48 Goodwood Rd, Adelaide','2026-03-11 10:41:00','natalie@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922580.png','Natalie Collins','0400000042','ACTIVE','PRIVATE'),
('29 Brighton Rd, Adelaide','2026-03-11 10:42:00','brandon@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922652.png','Brandon Stewart','0400000043','ACTIVE','BUSINESS'),
('83 Marion Rd, Adelaide','2026-03-11 10:43:00','zara@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922687.png','Zara Sanchez','0400000044','ACTIVE','PRIVATE'),
('7 Henley Beach Rd, Adelaide','2026-03-11 10:44:00','kyle@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922516.png','Kyle Morris','0400000045','ACTIVE','BUSINESS'),
('55 Tapleys Hill Rd, Adelaide','2026-03-11 10:45:00','madison@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922660.png','Madison Rogers','0400000046','ACTIVE','PRIVATE'),
('14 Grange Rd, Adelaide','2026-03-11 10:46:00','connor@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922571.png','Connor Reed','0400000047','ACTIVE','BUSINESS'),
('68 Semaphore Rd, Adelaide','2026-03-11 10:47:00','bella@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922569.png','Bella Cook','0400000048','ACTIVE','PRIVATE'),
('91 Salisbury Hwy, Adelaide','2026-03-11 10:48:00','dylan@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922683.png','Dylan Morgan','0400000049','ACTIVE','BUSINESS'),
('23 Grand Junction Rd, Adelaide','2026-03-11 10:49:00','layla@gmail.com','https://cdn-icons-png.flaticon.com/512/2922/2922524.png','Layla Bell','0400000050','ACTIVE','PRIVATE');
```