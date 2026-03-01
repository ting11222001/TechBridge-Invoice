package io.techbridge.invoice.techbridge_invoice.utils;

import io.techbridge.invoice.techbridge_invoice.domain.UserPrincipal;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;
import org.springframework.security.core.Authentication;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 03/2026
 */
public class UserUtils {

    public static UserDTO getAuthenticatedUser(Authentication authentication) {
        return ((UserDTO) authentication.getPrincipal());
    }

    public static UserDTO getLoggedInUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }
}
