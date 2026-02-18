package io.techbridge.invoice.techbridge_invoice.service.implementation;

import io.techbridge.invoice.techbridge_invoice.domain.Role;
import io.techbridge.invoice.techbridge_invoice.repository.RoleRepository;
import io.techbridge.invoice.techbridge_invoice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
        private final RoleRepository<Role> roleRepository;

    @Override
    public Role getRoleByUserId(Long id) {
        return roleRepository.getRoleByUserId(id);
    }
}
