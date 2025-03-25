package com.mycompany.flightapp.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    // Secret key
    private final Key key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // Token validation period
    private final long jwtExpirations= 10*60*60*1000;

    //Generate token based on username
    public String generateJwtToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", "ROLE_" + role.toUpperCase()) // Add role to the JWT payload
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirations))
                .signWith(key)
                .compact();
    }

    //Extract username from JwtToken
    public String getUserNameFromJwtToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    //Extract Expiration date
    public Date getExpirationDateFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration();
    }

    //Extract role from JwtToken
    public String getRoleFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role", String.class);
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromJwtToken(token);
        return expiration.before(new Date());
    }

    //Validate Jwt Token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromJwtToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
