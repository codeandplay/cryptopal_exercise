import org.junit.Assert;
import org.junit.Test;

public class DetectSingleCharXOrTest {
    @Test
    public void detectEncrypted() throws Exception {
        DetectSingleCharXOr detectSingleCharXOr = new DetectSingleCharXOr("/Users/raymac/Sites/cryptopal/s1/tests/detectSingleCharXOr.txt");

        Assert.assertEquals("Now that the party is jumping\n", detectSingleCharXOr.detectEncrypted());
    }
}