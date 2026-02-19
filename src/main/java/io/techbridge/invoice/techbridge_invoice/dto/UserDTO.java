package io.techbridge.invoice.techbridge_invoice.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor // use these specific annotations instead of @Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;  // don't need to send the password to the frontend
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean isNotLocked;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
    private String roleName;    // Map the role name and permissions of the user also
    private String permissions;
}
