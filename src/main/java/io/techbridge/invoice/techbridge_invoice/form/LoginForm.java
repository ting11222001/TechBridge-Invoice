package io.techbridge.invoice.techbridge_invoice.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
@Getter
@Setter
public class LoginForm {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
