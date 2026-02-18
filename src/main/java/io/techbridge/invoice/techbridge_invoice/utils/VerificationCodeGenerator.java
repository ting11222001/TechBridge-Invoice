package io.techbridge.invoice.techbridge_invoice.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
@Component
public class VerificationCodeGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom random = new SecureRandom();

    public String generateVerificationCode(int length) {
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            result.append(ALPHABET.charAt(index));
        }

        return result.toString();
    }
}
