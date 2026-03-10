package io.techbridge.invoice.techbridge_invoice.resource;

import io.techbridge.invoice.techbridge_invoice.domain.Customer;
import io.techbridge.invoice.techbridge_invoice.domain.HttpResponse;
import io.techbridge.invoice.techbridge_invoice.domain.Invoice;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;
import io.techbridge.invoice.techbridge_invoice.service.CustomerService;
import io.techbridge.invoice.techbridge_invoice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static java.time.LocalDateTime.now;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
@RestController
@RequestMapping(path="/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerResource {
    private final UserService userService;
    private final CustomerService customerService;

    // START - customers
    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getCustomers(@AuthenticationPrincipal UserDTO user, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getCustomers(page, size)))
                        .message("Customers Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCustomer(@AuthenticationPrincipal UserDTO user, @RequestBody Customer customer) {
        return ResponseEntity.created(URI.create(""))
                .body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                    "customer", customerService.createCustomer(customer)))
                            .message("Customer Created")
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .build()
                );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.getCustomer(id)))
                        .message("Customer Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchCustomers(@AuthenticationPrincipal UserDTO user, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.searchCustomers(name, page, size)))
                        .message("Customers Found")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCustomer(@AuthenticationPrincipal UserDTO user, @RequestBody Customer customer) {
        return ResponseEntity.ok()
                .body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                    "customer", customerService.updateCustomer(customer)))
                            .message("Customer Updated")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
                );
    }

    // START - invoices
    @PostMapping("/invoice/create")
    public ResponseEntity<HttpResponse> createInvoice(@AuthenticationPrincipal UserDTO user, @RequestBody Invoice invoice) {
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timestamp(now().toString())
                                .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                        "invoice", customerService.createInvoice(invoice)))
                                .message("Invoice Created")
                                .status(HttpStatus.CREATED)
                                .statusCode(HttpStatus.CREATED.value())
                                .build()
                );
    }

    @PostMapping("/invoice/new")
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok()
                .body(
                        HttpResponse.builder()
                                .timestamp(now().toString())
                                .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                        "customers", customerService.getCustomers()))
                                .message("Customers Retrieved")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }


    @GetMapping("/invoice/list")
    public ResponseEntity<HttpResponse> getInvoices(@AuthenticationPrincipal UserDTO user, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                "invoices", customerService.getInvoices(page, size)))
                        .message("Invoices Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/invoice/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long invoiceId) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", customerService.getInvoice(invoiceId)))
                        .message("Invoice Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }


    @PutMapping("/invoice/addToCustomer/{id}")
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long customerId, @RequestBody Invoice invoice) {
        customerService.addInvoiceToCustomer(customerId, invoice);
        return ResponseEntity.ok()
                .body(
                        HttpResponse.builder()
                                .timestamp(now().toString())
                                .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                        "customer", customerService.getCustomers()))
                                .message("Customers Retrieved")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .build()
                );
    }
}
