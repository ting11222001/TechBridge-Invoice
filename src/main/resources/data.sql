-- ============================================================
-- CLEAR EXISTING DATA
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE dev_db_techbridge_invoice.invoice;
TRUNCATE TABLE dev_db_techbridge_invoice.customer;
TRUNCATE TABLE dev_db_techbridge_invoice.UserRoles;
TRUNCATE TABLE dev_db_techbridge_invoice.Users;
TRUNCATE TABLE dev_db_techbridge_invoice.Roles;
SET FOREIGN_KEY_CHECKS = 1;


-- ============================================================
-- TechBridge Invoice — Seed Data
-- ============================================================
-- Password hash for all users: Demo@2026
-- Images: randomuser.me portraits (gender matched to name)
-- Addresses: Adelaide SA suburbs
-- Phone format: 61412XXXXXX (AU mobile)
-- ============================================================


-- ============================================================
-- ROLES
-- ============================================================
INSERT INTO dev_db_techbridge_invoice.Roles (name, permission) VALUES
('ROLE_USER',    'READ:USER,READ:CUSTOMER'), -- Program Assistant
('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'), -- Program Coordinator
('ROLE_ADMIN',   'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'), -- Program Admin
('ROLE_SYSADMIN','READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER'); -- Platform Owner


-- ============================================================
-- USERS (password: Demo@2026)
-- ============================================================
INSERT INTO dev_db_techbridge_invoice.Users (first_name, last_name, email, password, phone, address, title, bio, enabled, non_locked, using_mfa, image_url) VALUES
('Admin', 'User', 'admin@techbridge.com', '$2a$10$ceWNUy2arcSslHxYZwyMwu5QiQJJUZoeVdQ.A8Z8/DKBCr0nfuM8i', '0412000001', '14 Glenelg Esplanade, Glenelg SA 5045', 'Platform Owner', 'Oversees the TechBridge Invoice platform. Manages staff accounts, system configuration, and has full access to all partner and financial records.', TRUE, TRUE, FALSE, 'https://randomuser.me/api/portraits/men/10.jpg'), -- ROLE_SYSADMIN
('Tiffany', 'Chen', 'tiffany@techbridge.com', '$2a$10$ceWNUy2arcSslHxYZwyMwu5QiQJJUZoeVdQ.A8Z8/DKBCr0nfuM8i', '0412000002', '22 Unley Road, Unley SA 5061', 'Program Admin', 'Manages TechBridge partner onboarding, invoice creation, and financial reporting. Coordinates with Business Donors, Refurb Partners, and Request Partners across South Australia.', TRUE, TRUE, FALSE, 'https://randomuser.me/api/portraits/women/44.jpg'), -- ROLE_ADMIN
('Marcus', 'Webb', 'marcus.webb@techbridge.org', '$2a$10$ceWNUy2arcSslHxYZwyMwu5QiQJJUZoeVdQ.A8Z8/DKBCr0nfuM8i', '0412000003', '8 Prospect Road, Prospect SA 5082', 'Program Coordinator', 'Coordinates device donation logistics and manages partner relationships. Reviews and updates partner organisation records and tracks outstanding invoices.', TRUE, TRUE, FALSE, 'https://randomuser.me/api/portraits/men/32.jpg'), -- ROLE_MANAGER
('Sophie', 'Nguyen', 'sophie.nguyen@techbridge.org', '$2a$10$ceWNUy2arcSslHxYZwyMwu5QiQJJUZoeVdQ.A8Z8/DKBCr0nfuM8i', '0412000004', '5 Norwood Parade, Norwood SA 5067', 'Program Assistant', 'Assists with partner organisation record keeping and invoice tracking. Read-only access to staff accounts and partner data.', TRUE, TRUE, FALSE, 'https://randomuser.me/api/portraits/women/68.jpg'); -- ROLE_USER


-- ============================================================
-- USER ROLES  (user_id matches insertion order above)
-- ============================================================
INSERT INTO dev_db_techbridge_invoice.UserRoles (user_id, role_id) VALUES
(1, 4), -- Admin User → ROLE_SYSADMIN
(2, 3), -- Tiffany Chen → ROLE_ADMIN
(3, 2), -- Marcus Webb → ROLE_MANAGER
(4, 1); -- Sophie Nguyen → ROLE_USER


-- ============================================================
-- CUSTOMERS
-- ============================================================
INSERT INTO dev_db_techbridge_invoice.customer (name, email, type, status, address, phone, image_url, created_at) VALUES
('Optus Business Solutions', 'partnerships@optus.com.au', 'BUSINESS', 'ACTIVE', '80 King William St, Adelaide SA 5000', '0412100001', 'https://randomuser.me/api/portraits/men/12.jpg', NOW()), -- Business Donor
('Telstra Enterprise SA', 'enterprise.sa@telstra.com.au', 'BUSINESS', 'ACTIVE', '127 Rundle Mall, Adelaide SA 5000', '0412100002', 'https://randomuser.me/api/portraits/women/14.jpg', NOW()), -- Business Donor
('Harvey Norman Adelaide', 'accounts@harveynorman.com.au', 'BUSINESS', 'PENDING', '3 Marion Rd, Oaklands Park SA 5046', '0412100003', 'https://randomuser.me/api/portraits/men/18.jpg', NOW()), -- Business Donor
('GreenTech Recyclers', 'info@greentechrecyclers.com.au', 'BUSINESS', 'ACTIVE', '41 Port Rd, Hindmarsh SA 5007', '0412200001', 'https://randomuser.me/api/portraits/men/23.jpg', NOW()), -- Refurb Partner
('SA IT Refurb Co.', 'hello@saitrefurb.com.au', 'BUSINESS', 'ACTIVE', '9 Anzac Hwy, Everard Park SA 5035', '0412200002', 'https://randomuser.me/api/portraits/women/27.jpg', NOW()), -- Refurb Partner
('CircularTech Australia', 'ops@circulartech.com.au', 'BUSINESS', 'INACTIVE', '55 Commercial Rd, Port Adelaide SA 5015', '0412200003', 'https://randomuser.me/api/portraits/men/31.jpg', NOW()), -- Refurb Partner
('Mitcham Primary School', 'admin@mitchamprimary.sa.edu.au', 'BUSINESS', 'ACTIVE', '12 Belair Rd, Mitcham SA 5062', '0412300001', 'https://randomuser.me/api/portraits/women/33.jpg', NOW()), -- Request Partner
('Salisbury High School', 'finance@salisburyhs.sa.edu.au', 'BUSINESS', 'ACTIVE', '60 Salisbury Hwy, Salisbury SA 5108', '0412300002', 'https://randomuser.me/api/portraits/men/37.jpg', NOW()), -- Request Partner
('UniCare Youth Services', 'accounts@unicareyouth.org.au', 'BUSINESS', 'PENDING', '27 Sturt St, Adelaide SA 5000', '0412300003', 'https://randomuser.me/api/portraits/women/41.jpg', NOW()), -- Request Partner
('Hutt St Centre', 'ops@huttstcentre.org.au', 'BUSINESS', 'ACTIVE', '260 Hutt St, Adelaide SA 5000', '0412300004', 'https://randomuser.me/api/portraits/men/45.jpg', NOW()), -- Request Partner
('James Okafor', 'james.okafor@techbridge.org', 'INDIVIDUAL', 'ACTIVE', '33 Henley Beach Rd, Henley Beach SA 5022', '0412400001', 'https://randomuser.me/api/portraits/men/55.jpg', NOW()), -- Individual
('Rachel Kim', 'rachel.kim@greentechrecyclers.com.au', 'INDIVIDUAL', 'ACTIVE', '19 Churchill Rd, Kilburn SA 5084', '0412400002', 'https://randomuser.me/api/portraits/women/22.jpg', NOW()), -- Individual
('Daniel Torres', 'daniel.torres@saitrefurb.com.au', 'INDIVIDUAL', 'INACTIVE', '7 Goodwood Rd, Goodwood SA 5034', '0412400003', 'https://randomuser.me/api/portraits/men/71.jpg', NOW()), -- Individual
('Priya Sharma', 'priya.sharma@unicareyouth.org.au', 'INDIVIDUAL', 'PENDING', '45 Prospect Rd, Blair Athol SA 5084', '0412400004', 'https://randomuser.me/api/portraits/women/36.jpg', NOW()), -- Individual
('Ben Walters', 'ben.walters@huttstcentre.org.au', 'INDIVIDUAL', 'ACTIVE', '88 Fullarton Rd, Myrtle Bank SA 5064', '0412400005', 'https://randomuser.me/api/portraits/men/48.jpg', NOW()); -- Individual


-- ============================================================
-- INVOICES
-- ============================================================
INSERT INTO dev_db_techbridge_invoice.invoice (invoice_number, services, date, status, total, customer_id) VALUES
('INV-2026-001', 'Annual partner membership', '2026-01-10', 'PAID', 500.00, 1), -- Optus (Business Donor)
('INV-2026-002', 'Donation impact report + tax receipt package', '2026-02-14', 'PAID', 150.00, 1), -- Optus (Business Donor)
('INV-2026-003', 'Annual partner membership', '2026-01-20', 'PAID', 500.00, 2), -- Telstra (Business Donor)
('INV-2026-004', 'Donation impact report + tax receipt package', '2026-03-05', 'PENDING', 150.00, 2), -- Telstra (Business Donor)
('INV-2026-005', 'Annual partner membership', '2026-02-01', 'OVERDUE', 500.00, 3), -- Harvey Norman (Business Donor)
('INV-2026-006', 'Annual verified partner listing fee', '2026-01-15', 'PAID', 300.00, 4), -- GreenTech (Refurb Partner)
('INV-2026-007', 'Platform onboarding and compliance check', '2026-01-15', 'PAID', 200.00, 4), -- GreenTech (Refurb Partner)
('INV-2026-008', 'Annual verified partner listing fee', '2026-02-20', 'PENDING', 300.00, 5), -- SA IT Refurb (Refurb Partner)
('INV-2026-009', 'Platform onboarding and compliance check', '2026-02-20', 'PENDING', 200.00, 5), -- SA IT Refurb (Refurb Partner)
('INV-2025-010', 'Annual verified partner listing fee', '2025-06-01', 'OVERDUE', 300.00, 6), -- CircularTech (Refurb Partner)
('INV-2026-011', 'Annual school/NGO registration fee', '2026-01-28', 'PAID', 100.00, 7), -- Mitcham Primary (Request Partner)
('INV-2026-012', 'Device request processing fee', '2026-03-10', 'PENDING', 75.00, 7), -- Mitcham Primary (Request Partner)
('INV-2026-013', 'Annual school/NGO registration fee', '2026-01-30', 'PAID', 100.00, 8), -- Salisbury High (Request Partner)
('INV-2026-014', 'Device request processing fee', '2026-02-28', 'PAID', 75.00, 8), -- Salisbury High (Request Partner)
('INV-2026-015', 'Annual school/NGO registration fee', '2026-03-01', 'OVERDUE', 100.00, 9), -- UniCare (Request Partner)
('INV-2026-016', 'Annual school/NGO registration fee', '2026-02-10', 'PAID', 100.00, 10), -- Hutt St Centre (Request Partner)
('INV-2026-017', 'Device request processing fee', '2026-03-15', 'PENDING', 75.00, 10), -- Hutt St Centre (Request Partner)
('INV-2026-018', 'Annual partner membership', '2026-02-05', 'PAID', 500.00, 11), -- James Okafor (Individual)
('INV-2026-019', 'Annual verified partner listing fee', '2026-02-18', 'PAID', 300.00, 12), -- Rachel Kim (Individual)
('INV-2025-020', 'Annual verified partner listing fee', '2025-07-01', 'OVERDUE', 300.00, 13), -- Daniel Torres (Individual)
('INV-2026-021', 'Annual school/NGO registration fee', '2026-03-12', 'PENDING', 100.00, 14), -- Priya Sharma (Individual)
('INV-2026-022', 'Annual school/NGO registration fee', '2026-01-25', 'PAID', 100.00, 15), -- Ben Walters (Individual)
('INV-2026-023', 'Device request processing fee', '2026-03-18', 'PENDING', 75.00, 15); -- Ben Walters (Individual)