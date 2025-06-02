package com.cava.AuthService.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cava.AuthService.model.TokenPayload;
import com.cava.AuthService.model.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

@Service
public class TokenService {

    private static final String SECRET_KEY_STRING = "f7Tg9kW3nVpY4Lm0qZ7BvH2sE5RcD8Xj";
    private static final byte[] SECRET_KEY_BYTES = SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8);


    public String generateToken(Users user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_STRING);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(geExpirationTime())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error creating token", exception);
        }
    }


    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_STRING);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();


        } catch (JWTVerificationException exception){
            return "Erro!";
        }
    }

    private Instant geExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
