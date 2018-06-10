import org.junit.Test;

import static org.junit.Assert.*;

public class Base64Test {
    @Test
    public void decodeHexString() throws Exception {
        String hexString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        String base64String = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";

        assertEquals(hexString, Base64.decodeHexString(base64String));
    }

    @Test
    public void base64StringToByteArray() throws Exception {
        String base64String = "TWFu";

        byte byteArray[] = new byte[4];

        byteArray[0] = (byte) 'T';
        byteArray[1] = (byte) 'W';
        byteArray[2] = (byte) 'F';
        byteArray[3] = (byte) 'u';

        assertArrayEquals(byteArray, Base64.base64StringToByteArray(base64String));
    }

    @Test
    public void encodeHexString() throws Exception {
        String hexString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        String base64String = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";

        assertEquals(base64String, Base64.encodeHexString(hexString));
    }

    @Test
    public void hexStringToByteArray() throws Exception {
        String hexString = "01EA230f";

        byte byteArray[] = new byte[4];

        byteArray[0] = (byte) 0x01;
        byteArray[1] = (byte) 0xea;
        byteArray[2] = (byte) 0x23;
        byteArray[3] = (byte) 0x0f;

        assertArrayEquals(byteArray, Base64.hexStringToByteArray(hexString));
    }
}