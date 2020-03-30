import utils.Tokens;
import test.TokensTest;

public class Main {
    public static void main(String[] args) {
        assert TokensTest.successfulTokenGeneration();
        assert TokensTest.verifiedEqualsCreated();

        System.out.println("All tests passed.");
    }
}
