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

/**
 * Class Tokens.
 *
 * Implements generation and validation of JSON Web Tokens. For
 * the JWT specification, see jwt.io.
 */
public class Tokens {
    /**
     * Creates a JWT with a single username claim, and an expiry of 1 day.
     * Prints error message and returns null if error occurs in creation of
     * token.
     * @param username - String, The username to encode
     * @return token - String?, The JWT, or null if failed to create token
     */
    public static String createJWT(String username) {
        try {
            // TODO: Better secret, ideally use some cyclic shift/rotation
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

    /**
     * Decodes a JWT and returns the username encoded inside. If the
     * token is not valid or could not be decoded, returns null.
     * @param jwt - String, a valid JWT
     * @return username - String?, a nullable username decoded from the token
     */
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
