package io.techbridge.invoice.techbridge_invoice.service;

import io.techbridge.invoice.techbridge_invoice.domain.Customer;
import io.techbridge.invoice.techbridge_invoice.domain.Invoice;
import org.springframework.data.domain.Page;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
public interface CustomerService {
    // Customer functions
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Page<Customer> getCustomers(int page, int size);
    Iterable<Customer> getCustomers();
    Customer getCustomer(Long id);
    Page<Customer> searchCustomers(String name, int page, int size);

    // Invoice functions
    Invoice createInvoice(Invoice invoice);
    Page<Invoice> getInvoices(int page, int size);
    Invoice getInvoice(Long id);
    void addInvoiceToCustomer(Long customerId, Invoice invoice);

    Object getStats();
}
