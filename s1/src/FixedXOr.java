import java.io.IOException;

public class FixedXOr {
    static final char HEX_CHARS[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
        'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Take two hex string of same length, and xor them.
     *
     * @param hexStr1 hex string 1
     * @param hexStr2 hex string 2
     * @return result of fix xor string.
     */
    public static String fixedXOr(String hexStr1, String hexStr2) throws IOException{
        // check if the both the string length are equal
        if(hexStr1.length() != hexStr2.length()) {
            throw new RuntimeException("Two strings need to be the same length.");
        }

        // string 1 is valid string
        if(!FixedXOr.validateHexString(hexStr1)) {
            throw new IOException("String 1 is not a valid string.");
        }

        // string 2 is valid string
        if(!FixedXOr.validateHexString(hexStr2)) {
            throw new IOException("String 2 is not a valid string.");
        }

        byte byteArr1[] = Base64.hexStringToByteArray(hexStr1);
        byte byteArr2[] = Base64.hexStringToByteArray(hexStr2);
        int byteLen = byteArr1.length;
        byte outByteArr[] = new byte[byteLen];

        for (int i = 0; i < byteArr1.length; i++ ) {
            outByteArr[i] = (byte) (byteArr1[i] ^ byteArr2[i]);
        }

        return Base64.byteArrayToHexString(outByteArr);
    }

    /**
     * Check if given string is valid hex string.
     *
     * @param hexStr hex string to check
     * @return whether given string is valid hex string or not.
     */
    public static boolean validateHexString(String hexStr) {
        for (int i = 0; i < hexStr.length(); i++) {
            if(new String(HEX_CHARS).indexOf(hexStr.charAt(i)) == -1){
                return false;
            }
        }

        return true;
    }
}
