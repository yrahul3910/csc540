package test;

import utils.Tokens;

public class TokensTest {
    public static boolean verifiedEqualsCreated() {
        String tok = Tokens.createJWT("test");

        String val = Tokens.decodeJWT(tok);
        if (val != null)
            return val.equals("test");
        return false;
    }

    public static boolean successfulTokenGeneration() {
        String token = Tokens.createJWT("test");
        return token != null;
    }
}
