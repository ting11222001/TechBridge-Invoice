package io.techbridge.invoice.techbridge_invoice.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.twilio.rest.api.v2010.account.Message.creator;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */

@Service
@Slf4j
public class SmsService {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    public void sendSMS(String to, String messageBody) {
        Twilio.init(accountSid, authToken);

        String formattedAustralianPhoneNumber = normalizeAustralianNumber(to);
        Message message = creator(
                new PhoneNumber(formattedAustralianPhoneNumber),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();
        log.info(String.valueOf(message));
    }

    private String normalizeAustralianNumber(String phoneNumber) {
        phoneNumber = phoneNumber.trim();

        if (phoneNumber.startsWith("61")) {
            return "+" + phoneNumber;
        }

        if (phoneNumber.startsWith("0")) {
            return "+61" + phoneNumber.substring(1);
        }

        return "+61" + phoneNumber;
    }

}
