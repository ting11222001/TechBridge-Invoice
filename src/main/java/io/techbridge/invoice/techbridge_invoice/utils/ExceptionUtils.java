package io.techbridge.invoice.techbridge_invoice.utils;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.InvalidClaimException;
import io.techbridge.invoice.techbridge_invoice.domain.HttpResponse;
import io.techbridge.invoice.techbridge_invoice.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import tools.jackson.databind.ObjectMapper;

import java.io.OutputStream;

import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

@Slf4j
public class ExceptionUtils {

    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if (exception instanceof ApiException ||
                exception instanceof DisabledException ||
                exception instanceof LockedException ||
                exception instanceof BadCredentialsException ||
                exception instanceof InvalidClaimException ||
                exception instanceof TokenExpiredException) {
            HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), HttpStatus.BAD_REQUEST);
            writeResponse(response, httpResponse);
        } else {
            HttpResponse httpResponse = getHttpResponse(response, "An error occurred. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
            writeResponse(response, httpResponse);
        }
        log.error(exception.getMessage());
    }

    private static HttpResponse getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
        HttpResponse httpResponse = HttpResponse.builder()
                .timestamp(now().toString())
                .reason(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build();

        // Set servlet-level response metadata
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());

        return httpResponse;
    }

    // HttpServletResponse is a server-side object that represents: The HTTP response that will be sent back to the client.
    // It contains: Status code, Headers, Body
    // But the body is not stored as a String. It is written as bytes into a stream.
    private static void writeResponse(HttpServletResponse response, HttpResponse httpResponse) {
        try {
            OutputStream out = response.getOutputStream();

            ObjectMapper mapper = new ObjectMapper();

            // writeValue: Serializes the Java object into JSON and writes it to the HTTP response OutputStream
            mapper.writeValue(out, httpResponse);

            // flush: Makes sure any buffered JSON bytes in the stream are immediately sent to the client
            out.flush();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
