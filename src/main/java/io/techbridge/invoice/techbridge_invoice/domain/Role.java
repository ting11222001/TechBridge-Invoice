package io.techbridge.invoice.techbridge_invoice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Role {
    private Long id;

    @NotEmpty(message = "Role name cannot be empty")
    private String name;

    @NotEmpty(message = "Permission cannot be empty")
    private String permission;
}
