package com.bezbednost.ftn.bsep.security;

import com.bezbednost.ftn.bsep.utils.TimeProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class TokenUtils {

    @Value("bsep")
    private String APP_NAME;

    @Value("somesecret")
    public String SECRET;

    @Value("86400000")
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    static final String AUDIENCE_UNKNOWN = "unknown";
    static final String AUDIENCE_WEB = "web";
    static final String AUDIENCE_MOBILE = "mobile";
    static final String AUDIENCE_TABLET = "tablet";

    @Autowired
    private TimeProvider timeProvider;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    // Funkcija za generisanje JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setAudience(generateAudience())
                .setIssuedAt(timeProvider.now())
                .setExpiration(generateExpirationDate())
                // .claim("role", role) //postavljanje proizvoljnih podataka u telo JWT tokena
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    private Date generateExpirationDate() {
        return new Date(timeProvider.now().getTime() + EXPIRES_IN);
    }

    private String generateAudience() {
        return AUDIENCE_WEB;
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }

}
