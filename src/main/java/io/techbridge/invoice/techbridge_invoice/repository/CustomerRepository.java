package io.techbridge.invoice.techbridge_invoice.repository;

import io.techbridge.invoice.techbridge_invoice.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, ListCrudRepository<Customer, Long> { // JPA is handling this repo's implementation
    Page<Customer> findByNameContaining(String name, Pageable pageable);
}
