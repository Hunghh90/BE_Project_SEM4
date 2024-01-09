package com.example.beprojectsem4.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String secret_key = "1234567890";
    private static final Long expired_time = 1234567832L;

    public String generateToken(Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetail.getUsername())
                .setExpiration(new Date(new Date().getTime() + expired_time * 1000))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return  Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody().getSubject();
    }

}
