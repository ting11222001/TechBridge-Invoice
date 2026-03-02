package io.techbridge.invoice.techbridge_invoice.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
@Getter
@Setter
public class UpdateForm {
    @NotNull(message = "Id cannot be null or empty")
    private Long id;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message="Invalid Phone number")
    private String phone;

    private String address;
    private String title;
    private String bio;
}
