package io.techbridge.invoice.techbridge_invoice.service.implementation;

import io.techbridge.invoice.techbridge_invoice.domain.User;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;
import io.techbridge.invoice.techbridge_invoice.dtoMapper.UserDTOMapper;
import io.techbridge.invoice.techbridge_invoice.repository.UserRepository;
import io.techbridge.invoice.techbridge_invoice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }
}
