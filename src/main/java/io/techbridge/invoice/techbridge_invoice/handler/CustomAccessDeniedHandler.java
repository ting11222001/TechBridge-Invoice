package io.techbridge.invoice.techbridge_invoice.handler;

import io.techbridge.invoice.techbridge_invoice.domain.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HttpResponse httpResponse = HttpResponse.builder()
                .timestamp(now().toString())
                .reason("You do not have permission to access this.")
                .status(HttpStatus.FORBIDDEN)
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();

        // servlet-level response handling
        response.setContentType(APPLICATION_JSON_VALUE); // Content-Type: application/json
        response.setStatus(HttpStatus.FORBIDDEN.value()); // HTTP/1.1 403 Forbidden
        OutputStream out = response.getOutputStream();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }
}
