package io.techbridge.invoice.techbridge_invoice.dtoMapper;

import io.techbridge.invoice.techbridge_invoice.domain.User;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
@Component
public class UserDTOMapper {
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}

