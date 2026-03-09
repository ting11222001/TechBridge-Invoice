package io.techbridge.invoice.techbridge_invoice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity  // for JPA
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // set up primary key
    private Long id;

    private String name;
    private String email;
    private String type;
    private String status;
    private String address;
    private String phone;
    private String imageUrl;
    private Date createdAt;

    // One customer can have many invoices
    // mappedBy = "customer" means map to Invoice entity's "customer" field
    // fetch = EAGER means whenever we fetch a customer, fetch all its invoices
    // cascade = ALL means whenever we delete a customer, delete all its invoices
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL) // may need to change to fetch = FetchType.LAZY later
    private Collection<Invoice> invoices;
}
