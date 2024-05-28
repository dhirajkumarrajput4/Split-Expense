package com.expense_manager.security;

import com.expense_manager.entities.Person;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter {
   // This should be a secure secret key stored in a configuration file or environment variable
    private static final String SECRET_KEY = "your_secret_key";

    // This should be the token expiration time, e.g., 1 hour
    private static final long EXPIRATION_TIME = 3600000;

    public static String generateToken(Person person) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // Create a map of claims to include in the token
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", person.getFirstName());
        claims.put("lastName", person.getLastName());
        claims.put("emailId", person.getEmailId());
        claims.put("mobileNumber", person.getMobileNumber());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(person.getMobileNumber()) // or any unique identifier for the person
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
