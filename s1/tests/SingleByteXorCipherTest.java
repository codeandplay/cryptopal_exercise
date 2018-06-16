import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingleByteXorCipherTest {
    @Test
    public void decryptedHexString() throws Exception {

        SingleByteXorCipher singleByteXorCipher = new SingleByteXorCipher("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736");

        Assert.assertEquals(singleByteXorCipher.decryptedHexString(), "Cooking MC's like a pound of bacon");
    }

}