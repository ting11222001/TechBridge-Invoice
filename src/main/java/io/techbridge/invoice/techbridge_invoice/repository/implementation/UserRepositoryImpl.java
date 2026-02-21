package io.techbridge.invoice.techbridge_invoice.repository.implementation;

import com.twilio.rest.numbers.v3.HostedNumberOrder;
import io.techbridge.invoice.techbridge_invoice.domain.Role;
import io.techbridge.invoice.techbridge_invoice.domain.User;
import io.techbridge.invoice.techbridge_invoice.domain.UserPrincipal;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;
import io.techbridge.invoice.techbridge_invoice.enumeration.VerificationType;
import io.techbridge.invoice.techbridge_invoice.exception.ApiException;
import io.techbridge.invoice.techbridge_invoice.repository.RoleRepository;
import io.techbridge.invoice.techbridge_invoice.repository.UserRepository;
import io.techbridge.invoice.techbridge_invoice.rowMapper.UserRowMapper;
import io.techbridge.invoice.techbridge_invoice.utils.SmsService;
import io.techbridge.invoice.techbridge_invoice.utils.VerificationCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

import static io.techbridge.invoice.techbridge_invoice.enumeration.RoleType.*;
import static io.techbridge.invoice.techbridge_invoice.query.UserQuery.*;
import static io.techbridge.invoice.techbridge_invoice.query.UserQuery.INSERT_PASSWORD_VERIFICATION_BY_USER_ID_QUERY;
import static org.apache.commons.lang3.time.DateUtils.addDays;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final PasswordEncoder encoder;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final SmsService smsService;

    @Override
    public User create(User user) {
        // Check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Email already in use. Please use a different email.");
        }
        // Save new user
        try {
            KeyHolder holder = new GeneratedKeyHolder(); // get all the pk
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());

            // Add role to the user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name()); // if there's no role, this could give us empty result set

            // Send verification URL
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), VerificationType.ACCOUNT.getType());
            // Save URL in the verification table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));

            // Send email to user with the verification URL
            // emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);

            // Return the newly created user
            return user;
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String uuid, String type) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/verify/" + type + "/" + uuid)
                .toUriString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);

        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database");
//            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()));
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            User user = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No User found by email: " + email);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = verificationCodeGenerator.generateVerificationCode(8);

        try {
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, Map.of("userId", user.getId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, Map.of("code", verificationCode, "userId", user.getId(), "expirationDate", expirationDate));
            // This will trigger Twilio to send SMS text, so comment it when just testing the code.
            // smsService.sendSMS(user.getPhone(), "From: TechBridge Invoices \n Verification code\n" + verificationCode);
            // Instead, I can just log the generated verification code in the code while manually setting the using_mfa=1 in the Users table (it will trigger the login() in UserResource)
            log.info("Verification Code: {}", verificationCode);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public User verifyCode(String email, String code) {
        try {
            if (isVerificationCodeExpired(code)) { throw new ApiException("The code has expired. Please log in again."); }

            User userByCode = jdbc.queryForObject(SELECT_USER_BY_USER_CODE_QUERY, Map.of("code", code), new UserRowMapper());
            User userByEmail = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());

            if (userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())) {
                // Once we can confirm the verification code gives us the same user, then verification is complete, so we delete the code to prevent the same code used again
                jdbc.update(DELETE_CODE, Map.of("code", code));
                return userByCode;
            } else {
                throw new ApiException("Code is invalid. Please try again.");
            }

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("Could not find the record.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    private boolean isVerificationCodeExpired(String code) {
        try {
            return jdbc.queryForObject(SELECT_CODE_EXPIRATION_QUERY, Map.of("code", code), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("The code has expired. Please log in again.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    public void resetPassword(String email) {
        if (getEmailCount(email.trim().toLowerCase()) <= 0) throw new ApiException("There is no account for this email");
        try {
            String expirationDate = DateFormatUtils.format(addDays(new Date(), 1), DATE_FORMAT);
            User user = getUserByEmail(email);
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), VerificationType.PASSWORD.getType());
            log.info("Verification URL: {}", verificationUrl);
            // Delete the existing one
            jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, Map.of("userId", user.getId()));
            // Insert the new one
            jdbc.update(INSERT_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, Map.of("userId", user.getId(), "url", verificationUrl, "expirationDate", expirationDate));
            // TODO: Send email with url to user

        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public User verifyPasswordKey(String key) {
        if (isLinkExpired(key, VerificationType.PASSWORD)) throw new ApiException("This link has expired. Please reset your password again.");
        try {
            User user = jdbc.queryForObject(SELECT_USER_BY_PASSWORD_URL_QUERY, Map.of("url", getVerificationUrl(key, VerificationType.PASSWORD.getType())), new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("This link is not valid. Please reset your password again.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    private boolean isLinkExpired(String key, VerificationType password) {
        try {
            return jdbc.queryForObject(SELECT_EXPIRATION_BY_URL, Map.of("url", getVerificationUrl(key, password.getType())), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("This link is not valid. Please reset your password again.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }


    @Override
    public void renewPassword(String key, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) throw new ApiException("Passwords don't match. Please try again.");
        try {
            jdbc.update(UPDATE_USER_PASSWORD_BY_URL_QUERY, Map.of("password", encoder.encode(password), "url", getVerificationUrl(key, VerificationType.PASSWORD.getType())));
            jdbc.update(DELETE_VERIFICATION_BY_URL_QUERY, Map.of("url", getVerificationUrl(key, VerificationType.PASSWORD.getType()))); // to clean up
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }

}
