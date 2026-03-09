package io.techbridge.invoice.techbridge_invoice.repository;

import io.techbridge.invoice.techbridge_invoice.domain.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long>, ListCrudRepository<Invoice, Long> {}
