package io.techbridge.invoice.techbridge_invoice.repository;

import io.techbridge.invoice.techbridge_invoice.domain.User;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;

import java.util.Collection;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

public interface UserRepository <T extends User> {
    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
    User getUserByEmail(String email);
    void sendVerificationCode(UserDTO user);
    User verifyCode(String email, String code);
    void resetPassword(String email);
    User verifyPasswordKey (String key);
    void renewPassword(String key, String password, String confirmPassword);
}
