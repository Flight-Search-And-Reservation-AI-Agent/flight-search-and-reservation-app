package com.mycompany.flightapp.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
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
    public String generateJwtToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+jwtExpirations))
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


    //Validate Jwt Token
    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            log.error("authentication failed",e);
        }
        return false;
    }
}
