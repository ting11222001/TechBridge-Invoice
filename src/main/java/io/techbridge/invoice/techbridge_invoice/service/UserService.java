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
    UserDTO getUserByEmail(String email);
    void sendVerificationCode(UserDTO user);
    UserDTO verifyCode(String email, String code);
    void resetPassword(String email);
    UserDTO verifyPasswordKey(String key);
    void renewPassword(String key, String password, String confirmPassword);
    UserDTO verifyAccountKey(String key);
}
