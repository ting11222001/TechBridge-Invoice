package io.techbridge.invoice.techbridge_invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@Entity // for JPA
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // set up primary key
    private Long id;

    private String invoiceNumber;
    private String services;
    private Date date;
    private String status;
    private double total;

    @ManyToOne // Many invoices belonged to one customer
    @JoinColumn(name = "customer_id", nullable = false)  // Each invoice will have this column showing its customer id
    @JsonIgnore // prevent infinite recursion when serializing Invoice -> Customer -> Invoices
    private Customer customer;
}
