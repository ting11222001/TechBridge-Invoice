package io.techbridge.invoice.techbridge_invoice.service.implementation;

import io.techbridge.invoice.techbridge_invoice.domain.Customer;
import io.techbridge.invoice.techbridge_invoice.domain.Invoice;
import io.techbridge.invoice.techbridge_invoice.repository.CustomerRepository;
import io.techbridge.invoice.techbridge_invoice.repository.InvoiceRepository;
import io.techbridge.invoice.techbridge_invoice.rowMapper.StatsRowMapper;
import io.techbridge.invoice.techbridge_invoice.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static io.techbridge.invoice.techbridge_invoice.query.CustomerQuery.STATS_QUERY;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final NamedParameterJdbcTemplate jdbc;

    // Customer functions
    @Override
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(new Date());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public Page<Customer> searchCustomers(String name, int page, int size) {
        return customerRepository.findByNameContaining(name, PageRequest.of(page, size));
    }

    // Invoice functions
    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNumber("INV-" + RandomStringUtils.secure().nextAlphanumeric(8).toUpperCase());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Page<Invoice> getInvoices(int page, int size) {
        return invoiceRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    @Override
    public void addInvoiceToCustomer(Long customerId, Invoice invoice) {
        invoice.setInvoiceNumber("INV-" + RandomStringUtils.secure().nextAlphanumeric(8).toUpperCase());
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found with customer id: " + customerId));
        invoice.setCustomer(customer);
        invoiceRepository.save(invoice);
    }

    @Override
    public Object getStats() {
        return jdbc.queryForObject(STATS_QUERY, Map.of(), new StatsRowMapper());
    }
}
