package io.techbridge.invoice.techbridge_invoice.service;

import io.techbridge.invoice.techbridge_invoice.domain.User;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
public interface UserService {
    UserDTO createUser(User user);
}
