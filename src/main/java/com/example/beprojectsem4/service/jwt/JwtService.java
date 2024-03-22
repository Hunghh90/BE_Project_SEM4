package com.example.beprojectsem4.service.jwt;

import com.example.beprojectsem4.dtos.authDtos.TokenResponseDto;
import com.example.beprojectsem4.entities.UserEntity;
import com.example.beprojectsem4.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    private static final String secret_key = "XdyUlzjBO1siyuJh";
    private static final String refresh_secret_key = "Q6UcqbeyinGDPdNZ";
    private static final int expired_time = 1800000;

    public TokenResponseDto generateToken(Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        Date expirationDate = new Date(new Date().getTime() + expired_time * 1000);

        String token = Jwts.builder()
                .setSubject(userDetail.getUsername())
                .setExpiration(expirationDate)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
        return new TokenResponseDto(token,expirationDate.getTime());
    }

    public String generateRefreshToken(Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetail.getUsername())
                .setExpiration(null)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, refresh_secret_key)
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

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(refresh_secret_key).parseClaimsJws(refreshToken);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return  Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody().getSubject();
    }
    public UserEntity getMeFromToken(String token){
        String email = getUsernameFromToken(token);
        return userRepository.findUserByEmail(email);
    }

}
