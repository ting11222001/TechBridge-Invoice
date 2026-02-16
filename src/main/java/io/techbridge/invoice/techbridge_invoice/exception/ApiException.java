package io.techbridge.invoice.techbridge_invoice.exception;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
