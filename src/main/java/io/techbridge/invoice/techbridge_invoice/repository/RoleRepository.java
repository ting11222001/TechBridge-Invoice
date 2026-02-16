package io.techbridge.invoice.techbridge_invoice.repository;

import io.techbridge.invoice.techbridge_invoice.domain.Role;

import java.util.Collection;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

public interface RoleRepository <T extends Role> {
    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
    void addRoleToUser(Long userId, String roleName);
    Role getRoleByUserId(Long userId);
    Role getRoleByUserEmail(String email);
    void updateUserRole(Long userId, String roleName);
}
