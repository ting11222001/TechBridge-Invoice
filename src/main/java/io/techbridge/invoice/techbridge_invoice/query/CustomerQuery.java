package io.techbridge.invoice.techbridge_invoice.query;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
public class CustomerQuery {
    public static final String STATS_QUERY = """
        SELECT
            (SELECT COUNT(*) FROM customer) AS total_customers,
            (SELECT COUNT(*) FROM invoice) AS total_invoices,
            (SELECT ROUND(SUM(total)) FROM invoice) AS total_billed
        """;
}
