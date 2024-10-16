package com.expense_manager.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {

        // try {
        // Thread.sleep(500);
        // } catch (InterruptedException e) {
        // throw new RuntimeException(e);
        // }
        // Authorization

        String requestHeader = request.getHeader("Authorization");
        // Bearer 2352345235sdfrsfgsdfsdf
        logger.info(" Header :  {}", requestHeader);
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            // looking good
            token = requestHeader.substring(7);
            try {

                username = this.jwtHelper.getUsernameFromToken(token);

            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            logger.info("Invalid Header Value !! ");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // fetch user detail from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {
                // set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("Validation fails !!");
            }

        }

        filterChain.doFilter(request, response);

    }
}

/*
 * import com.expense_manager.entities.Person;
 * import io.jsonwebtoken.Jwts;
 * import io.jsonwebtoken.SignatureAlgorithm;
 * 
 * import java.util.Date;
 * import java.util.HashMap;
 * import java.util.Map;
 * 
 * public class JwtAuthenticationFilter {
 * // This should be a secure secret key stored in a configuration file or
 * environment variable
 * private static final String SECRET_KEY = "your_secret_key";
 * 
 * // This should be the token expiration time, e.g., 1 hour
 * private static final long EXPIRATION_TIME = 3600000;
 * 
 * public static String generateToken(Person person) {
 * Date now = new Date();
 * Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
 * 
 * // Create a map of claims to include in the token
 * Map<String, Object> claims = new HashMap<>();
 * claims.put("firstName", person.getFirstName());
 * claims.put("lastName", person.getLastName());
 * claims.put("emailId", person.getEmailId());
 * claims.put("mobileNumber", person.getMobileNumber());
 * 
 * return Jwts.builder()
 * .setClaims(claims)
 * .setSubject(person.getMobileNumber()) // or any unique identifier for the
 * person
 * .setIssuedAt(now)
 * .setExpiration(expiryDate)
 * .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
 * .compact();
 * }
 * 
 * 
 * }
 */
