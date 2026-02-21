package io.techbridge.invoice.techbridge_invoice.resource;

import io.techbridge.invoice.techbridge_invoice.domain.HttpResponse;
import io.techbridge.invoice.techbridge_invoice.domain.User;
import io.techbridge.invoice.techbridge_invoice.domain.UserPrincipal;
import io.techbridge.invoice.techbridge_invoice.dto.UserDTO;
import io.techbridge.invoice.techbridge_invoice.dtoMapper.UserDTOMapper;
import io.techbridge.invoice.techbridge_invoice.exception.ApiException;
import io.techbridge.invoice.techbridge_invoice.form.LoginForm;
import io.techbridge.invoice.techbridge_invoice.provider.TokenProvider;
import io.techbridge.invoice.techbridge_invoice.service.RoleService;
import io.techbridge.invoice.techbridge_invoice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static io.techbridge.invoice.techbridge_invoice.utils.ExceptionUtils.processError;
import static java.time.LocalDateTime.now;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
@RestController
@RequestMapping(path="/user")
@RequiredArgsConstructor
@Slf4j
public class UserResource {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RoleService roleService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private static final String TOKEN_PREFIX = "Bearer ";

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        Authentication authentication = authenticate(loginForm.getEmail(), loginForm.getPassword());
        UserDTO user = getAuthenticatedUser(authentication);
//        System.out.println("/login authentication: " + authentication);
//        System.out.println("/login getAuthenticatedUser() returns: " + user);
//        System.out.println("/login getAuthenticatedUser() returns user, checking the user email: " + user.getEmail());
        return user.isUsingMfa() ? sendVerificationCode(user) : sendResponse(user);
    }

    private UserDTO getAuthenticatedUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }

    private Authentication authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            // can use the unauthenticated() in UsernamePasswordAuthenticationToken class to replace the new UsernamePasswordAuthenticationToken() like this:
            // authenticationManager.authenticate(unauthenticated(loginForm.getEmail(), loginForm.getPassword()));

            return authentication;
        } catch (Exception exception) {
            processError(httpServletRequest, httpServletResponse, exception);
            throw new ApiException(exception.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDto = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", userDto))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build());
    }

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDTO user = userService.verifyCode(email, code);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", user,
                                "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        UserDTO user = userService.getUserByEmail(authentication.getName());
        System.out.println(authentication);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", user))
                        .message("Profile Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/get/<userId>/")
                .toUriString());
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO user) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", user,
                                "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    private UserPrincipal getUserPrincipal(UserDTO user) {
//        return new UserPrincipal(UserDTOMapper.toUser(userService.getUserByEmail(user.getEmail())), roleService.getRoleByUserId(user.getId()).getPermission());
         return new UserPrincipal(UserDTOMapper.toUser(userService.getUserByEmail(user.getEmail())), roleService.getRoleByUserId(user.getId()));
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", user))
                        .message("Verification Code Sent")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());

    }

    // START - To reset password when user is not logged in

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("Email sent. Please check your email to reset your password")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) {
        UserDTO user = userService.verifyPasswordKey(key);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("user", user))
                        .message("Please enter a new password")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping("/resetpassword/{key}/{password}/{confirmPassword}")
    public ResponseEntity<HttpResponse> resetPasswordWithKey(@PathVariable("key") String key,
                                                          @PathVariable("password") String password,
                                                          @PathVariable("confirmPassword") String confirmPassword) {
        userService.renewPassword(key, password, confirmPassword);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message("Password reset successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    // END - To reset password when user is not logged in

    // START - To verify account
    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable("key") String key) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .message(userService.verifyAccountKey(key).isEnabled() ? "Account already verified" : "Account verified")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    // START - To refresh token
    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        log.info("request HttpHeaders: {}", request.getHeader(HttpHeaders.AUTHORIZATION)); // Bearer eyJhbG...
        if (isHeaderTokenValid(request)) {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(TOKEN_PREFIX.length());
            UserDTO user = userService.getUserByEmail(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(Map.of("user", user,
                                    "access_token", tokenProvider.createAccessToken(getUserPrincipal(user)),
                                    "refresh_token", token))
                            .message("Token refreshed")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build());
        } else {

        return ResponseEntity.badRequest().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .reason("Refresh Token missing or invalid")
                        .developerMessage("Refresh Token missing or invalid")
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
        }
    }

    private boolean isHeaderTokenValid(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null
                && request.getHeader(HttpHeaders.AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && tokenProvider.isTokenValid(
                        tokenProvider.getSubject(request.getHeader(HttpHeaders.AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                        request.getHeader(HttpHeaders.AUTHORIZATION).substring(TOKEN_PREFIX.length())
                );
    }

}
