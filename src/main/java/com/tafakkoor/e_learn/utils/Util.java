package com.tafakkoor.e_learn.utils;

import com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.domain.Token;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;

public class Util {
    private static final ThreadLocal<Util> UTIL_THREAD_LOCAL = ThreadLocal.withInitial(Util::new);

    public Token buildToken(String token, AuthUser authUser) {
        return Token.builder()
                .token(token)
                .user(authUser)
                .validTill(LocalDateTime.now().plusMinutes(10))
                .build();
    }

public String generateBody(String username, String token){
    String link = Container.BASE_URL + "auth/activate?token=" + token;
    return """
                Subject: Activate Your Account
                                
                Dear %s,
                                
                Thank you for registering on our website. To activate your account, please click on the following link:
                                
                %s
                                
                If you have any questions or need assistance, please contact us at [SUPPORT_EMAIL OR TELEGRAM_BOT].
                                
                Best regards,
                E-Learn LTD.
                """.formatted(username, link);
}



    public String generateBodyForInactiveUsers(String username){
        return """
                Subject: Login to Your Account
                                
                Dear %s,
                                
                // message
                                
                %s
                                
                If you have any questions or need assistance, please contact us at [SUPPORT_EMAIL OR TELEGRAM_BOT].
                                
                Best regards,
                E-Learn LTD.
                """.formatted(username,Container.BASE_URL); // TODO: 13/03/23 write message to users that 3 days inactive
    }


    public String generateBodyForBirthDay(String username){
        return """
                Subject: Happy Birthday
                                
                Dear %s,
                                
                // message
                                
                %s
                                
                If you have any questions or need assistance, please contact us at [SUPPORT_EMAIL OR TELEGRAM_BOT].
                                
                Best regards,
                E-Learn LTD.
                """.formatted(username,Container.BASE_URL); // TODO: 13/03/23 write message to celebrate birthday
    }

    public BigInteger convertToBigInteger(String number) {
        try {
            double doubleValue = Double.parseDouble(number);
            long longValue = (long) doubleValue;
            if (doubleValue == (double) longValue) {
                return BigInteger.valueOf(longValue);
            } else {
                String[] parts = number.split("E");
                BigInteger coefficient = new BigInteger(parts[0].replace(".", ""));
                BigInteger exponent = new BigInteger(parts[1]);
                return coefficient.multiply(BigInteger.TEN.pow(exponent.intValue()));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input string. Must be in scientific notation.");
        }
    }

    public static Util getInstance() {
        return UTIL_THREAD_LOCAL.get();
    }
}
