package io.techbridge.invoice.techbridge_invoice.service;

import io.techbridge.invoice.techbridge_invoice.domain.Role;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
public interface RoleService {
    Role getRoleByUserId(Long id);
}
