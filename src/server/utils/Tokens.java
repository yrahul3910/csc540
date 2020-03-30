package utils;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class Tokens {
    public static String createJWT(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("shhh");

            return JWT.create()
                    .withClaim("exp", Date.from(
                            LocalDate.now()
                                    .plusDays(1)
                                    .atStartOfDay()
                                    .toInstant(ZoneOffset.UTC)
                    ))
                    .withClaim("username", username)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            System.out.println("Error: failed to create JWT:\n" + e.toString());
            return null;
        }
    }

    public static String decodeJWT(String jwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("shhh");
            JWTVerifier verifier = JWT.require(algorithm)
                                    .build();
            DecodedJWT token = verifier.verify(jwt);
            return token.getClaim("username").asString();
        } catch (JWTVerificationException e) {
            System.out.println("Error: invalid JWT:\n" + e.toString());
            return null;
        }
    }
}
