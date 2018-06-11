import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class FixedXOrTest {
    @Test
    public void fixedXOr() throws Exception {
        String hexStr1 = "1c0111001f010100061a024b53535009181c";
        String hexStr2 = "686974207468652062756c6c277320657965";

        String resultHexStr = "746865206b696420646f6e277420706c6179";

        assertEquals(resultHexStr, FixedXOr.fixedXOr(hexStr1, hexStr2));
    }

    @Test
    // two string have different length
    public void fixedXOrDiffLength() throws Exception {
        try {
            String hexStr1 = "1c0111001f010100061a024b53535009181";
            String hexStr2 = "686974207468652062756c6c277320657965";
            FixedXOr.fixedXOr(hexStr1, hexStr2);
        } catch(Exception e) {
            assertEquals("Two strings need to be the same length.", e.getMessage());
        }

    }

    @Test
    // Invalid hex string
    public void fixedXOrInvalidHexString1() throws Exception {
        try {
            String hexStr1 = "1c0111001f010100061a0c4b53535009181V";
            String hexStr2 = "686974207468652062756c6c277320657965";
            FixedXOr.fixedXOr(hexStr1, hexStr2);
        } catch(Exception e) {
            assertEquals("String 1 is not a valid string.", e.getMessage());
        }

    }

    @Test
    // Invalid hex string
    public void fixedXOrInvalidHexString2() throws Exception {
        try {
            String hexStr1 = "1c0111001f010100061a0c4b53535009181F";
            String hexStr2 = "686974207468652062756c6c27732065796K";
            FixedXOr.fixedXOr(hexStr1, hexStr2);
        } catch(Exception e) {
            assertEquals("String 2 is not a valid string.", e.getMessage());
        }

    }

}