package io.techbridge.invoice.techbridge_invoice.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
@Data
public class LoginForm {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
