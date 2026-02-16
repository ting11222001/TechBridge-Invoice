package io.techbridge.invoice.techbridge_invoice.enumeration;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
public enum VerificationType {
    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type.toLowerCase();
    }
}
