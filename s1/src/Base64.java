
public class Base64 {
    // Public fields

    // Don't break lines when encoding (violates strict Base64 specification)
    public final static int DONT_BREAK_LINES = 8;

    // Private fields

    // maximum line length (76) of Base64 output.
    private final static int MAX_LINE_LENGTH = 76;

    // The equals sign (=) as a byte.
    private final static byte EQUALS_SIGN = (byte) '=';

    // The new line character (\n) as a byte.
    private final static byte NEW_LINE = (byte) '\n';

    // The 64 valid Base64 values.
    private final static byte[] ALPHABET = {
            (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
            (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N',
            (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U',
            (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z',
            (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g',
            (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
            (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
            (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
            (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/'
    };

    // translate a Base64 value to either its 6-bit reconstruction value or a negative number
    // indicating some other meaning.
    private final static byte[] DECODABET =
            {
                    -9, -9, -9, -9, -9, -9, -9, -9, -9,                 // Decimal  0 -  8
                    -5, -5,                                      // Whitespace: Tab and Linefeed
                    -9, -9,                                      // Decimal 11 - 12
                    -5,                                         // Whitespace: Carriage Return
                    -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,     // Decimal 14 - 26
                    -9, -9, -9, -9, -9,                             // Decimal 27 - 31
                    -5,                                         // Whitespace: Space
                    -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,              // Decimal 33 - 42
                    62,                                         // Plus sign at decimal 43
                    -9, -9, -9,                                   // Decimal 44 - 46
                    63,                                         // Slash at decimal 47
                    52, 53, 54, 55, 56, 57, 58, 59, 60, 61,              // Numbers zero through nine
                    -9, -9, -9,                                   // Decimal 58 - 60
                    -1,                                         // Equals sign at decimal 61
                    -9, -9, -9,                                      // Decimal 62 - 64
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,            // Letters 'A' through 'N'
                    14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,        // Letters 'O' through 'Z'
                    -9, -9, -9, -9, -9, -9,                          // Decimal 91 - 96
                    26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,     // Letters 'a' through 'm'
                    39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,     // Letters 'n' through 'z'
                    -9, -9, -9, -9                                 // Decimal 123 - 126
        /*,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 127 - 139
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 140 - 152
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 153 - 165
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 166 - 178
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 179 - 191
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 192 - 204
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 205 - 217
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 218 - 230
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,     // Decimal 231 - 243
        -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9         // Decimal 244 - 255 */
            };

    private final static byte BAD_ENCODING = -9; // Indicate error in encoding
    private final static byte WHITE_SPACE_ENC = -5; // Indicate white space in encoding.
    private final static byte EQUALS_SIGN_ENC = -1; // Indicate equals sign in encoding.

    // defeats instantiation.
    private Base64() {
    }

    // Encode methods

    /**
     * Encodes up to three bytes of the array <var>source</var>
     * and writes the resulting four Base64s bytes to <var>destination</var>.
     * The source and destination arrays can be manipulated anywhere along their length by specifying
     * <var>srcOffset</var> and <var>destOffset</var>.
     * This method does not check to make sure your arrays are large enough to accommodate
     * <var>srcOffset</var> + 3 for the <var>source</var> array or <var>destOffset</var> + 4 for the
     * <var>destination</var> array.
     * the actual number of significant bytes in your array is given by <var>numSigBytes</var>
     *
     * @param source      the array to convert.
     * @param srcOffset   the index where conversion begins
     * @param numSigBytes the number of significant bytes in your array
     * @param destination the array to hold the conversion
     * @param destOffset  the index where output will be put
     * @return <var>destination</var> array
     */
    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes,
                                     byte[] destination, int destOffset) {
        //           1         2         3
        // 01234567890123456789012345678901 Bit position
        // --------000000001111111122222222 Array position from threebytes
        // --------|    ||    ||     ||    | Six bit groups to index Alphabate.
        //          >>18  >12    >>6   >>0   Right shift necessary.
        //                0x3f   0x3f  0x3f  Additional AND, remove all the bit after 8th bits.

        // Create buffer with zero-padding if there are only one or two significant bytes passed
        // in the array.
        // We have to shift left 24 in order to flush out the 1's that appear. maybe because each byte in the array
        // have 32 bits. and we only care about the first 8 bits as they are bytes?
        // when Java treats a value as negative that is cast from a byte to an int.
        // what we working out the inBuff here is working the integer value of the three bytes.
        int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0)
                | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0)
                | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);

        switch (numSigBytes) {
            case 3:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)]; // work out the first of the four bytes.
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f]; // work out the second of the four bytes.
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f]; // work out the third of the four bytes.
                destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f]; // work out the forth of the four bytes.
                return destination;

            case 2:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
                destination[destOffset + 3] = EQUALS_SIGN;
                return destination;

            case 1:
                destination[destOffset] = ALPHABET[(inBuff >>> 18)];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
                destination[destOffset + 2] = EQUALS_SIGN;
                destination[destOffset + 3] = EQUALS_SIGN;
                return destination;

            default:
                return destination;
        } // end switch
    } // end encode3to4

    /**
     * Encodes a byte array into Base64 notation.
     *
     * @param source  The data to convert
     * @param off     Offset in array where conversion should begin
     * @param len     Length of data to convert
     * @param options Specified options
     * @see Base64#GZIP
     * @see Base64#DONT_BREAK_LINES
     * @since 2.0
     */
    public static String encodeBytes(byte[] source, int off, int len, int options) {
        // isolate option
        int dontBreakLines = (options & DONT_BREAK_LINES);
        // convert option to boolean in way that code like it.
        boolean breakLines = dontBreakLines == 0;

        int len43 = len * 4 / 3;
        byte[] outBuff = new byte[
                (len43)  // main 4:3
                        + ((len % 3) > 0 ? 4 : 0) // Account for padding
                        + (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)
                ]; // new lines

        int d = 0;
        int e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        for (; d < len2; d += 3, e += 4) {
            encode3to4(source, d + off, 3, outBuff, e);
            lineLength += 4;
            if (breakLines && lineLength == MAX_LINE_LENGTH) {
                outBuff[e + 4] = NEW_LINE;
                e++;
                lineLength = 0;
            }

        }

        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e);
            e += 4;
        }
        return new String(outBuff, 0, e);
    }

    /**
     * Convert hex string to byte array.
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToByteArray(String hexString) {
        // Get the length of hex string.
        int len = hexString.length();

        // work out the size of byte array, by dividing hex string length by 2. as 2 hex char construct a byte.
        byte[] data = new byte[len / 2];

        // loop through hex string, 2 in a interval.
        for (int i = 0; i < len; i += 2) {
            // i / 2 here work out the index of the byte
            // Character.digit work out the numeric value of the numeric char, with provided radix
            // the first character shift 4 to the right as it is more significant number.
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Convert hex string to base64 String
     *
     * @param hexString hex string to be converted.
     * @return base64 string.
     */
    public static String encodeHexString(String hexString) {
        // convert hexString to byte array.
        byte[] byteArr = Base64.hexStringToByteArray(hexString);

        // convert byte array to base64 string.
        int byteArrSize = byteArr.length;

        return Base64.encodeBytes(byteArr, 0, byteArrSize, 1);

    } // end of encodeHexString

    // Decode methods

    /**
     * Decode four bytes from array <var>source</var>
     * and writes the resulting Base64s bytes(to up 4) to <var>destination</var>.
     * The source and destination arrays can be manipulated anywhere along their length by specifying
     * <var>srcOffset</var> and <var>destOffset</var>.
     * This method does not check to make sure your arrays are large enough to accommodate
     * <var>srcOffset</var> + 4 for the <var>source</var> array or <var>destOffset</var> + 3 for the
     * <var>destination</var> array.
     * the actual number of bytes that were converted from the base64 encoding
     *
     * @param source      the array to convert.
     * @param srcOffset   the index where conversion begins
     * @param destination the array to hold the conversion
     * @param destOffset  the index where output will be put
     * @return number of decoded byte converted
     */
    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset) {
        // Example Dk==
        if (source[srcOffset + 2] == EQUALS_SIGN) {
            int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
                    | ((DECODABET[source[srcOffset + 1]] << 24) >>> 12);

            destination[destOffset] = (byte) (outBuff >>> 16);

            return 1;
        }

        // Example DkL=
        else if (source[srcOffset + 3] == EQUALS_SIGN) {
            int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
                    | ((DECODABET[source[srcOffset + 1]] << 24) >>> 12)
                    | ((DECODABET[source[srcOffset + 2]] << 24) >>> 18);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);

            return 2;
        }

        // Example DkLE
        else {
            try {
                int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
                        | ((DECODABET[source[srcOffset + 1]] << 24) >>> 12)
                        | ((DECODABET[source[srcOffset + 2]] << 24) >>> 18)
                        | ((DECODABET[source[srcOffset + 3]] << 24) >>> 24);

                destination[destOffset] = (byte) (outBuff >>> 16);
                destination[destOffset + 1] = (byte) (outBuff >>> 8);
                destination[destOffset + 2] = (byte) (outBuff);

                return 3;
            } catch(Exception e) {
                System.out.println("" + source[srcOffset] + ": " + (DECODABET[source[srcOffset]]));
                System.out.println("" + source[srcOffset + 1] + ": " + (DECODABET[source[srcOffset + 1]]));
                System.out.println("" + source[srcOffset + 2] + ": " + (DECODABET[source[srcOffset + 2]]));
                System.out.println("" + source[srcOffset + 3] + ": " + (DECODABET[source[srcOffset + 3]]));
                return -1;
            }
        }
    } // end decode4to3

    /**
     * Very low-level to decoding ASCII characters in the form of byte array.
     * Does not support gunzip or any other "fancy" feature.
     * @param source the base64 encoded data.
     * @param off the offset of where to begin decoding
     * @param len the length of characters to decode.
     * @return
     */
    public static byte[] decode (byte[] source, int off, int len) {

        int len34 = len * 3 / 4; // the ratio is 3 / 4, every 4 bytes of base 64 should be 3 bytes of original bytes.
        byte[] outBuff = new byte[len34]; // upper limit of the size of the output.
        int outBuffPosn = 0;

        byte[] b4 = new byte[4];
        int b4Posn = 0;
        int i  = 0;
        byte sbiCrop = 0;
        byte sbiDecode= 0;

        for(i = off; i < len; i++){
            sbiCrop = (byte)(source[i] & 0x7f); // only the low 7 bits;
            sbiDecode = DECODABET[sbiCrop];

            if(sbiDecode >= WHITE_SPACE_ENC) { // White space, Equal sign or better.

                if(sbiDecode >= EQUALS_SIGN_ENC) {
                    b4[b4Posn ++] = sbiCrop;

                    if (b4Posn > 3) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
                        b4Posn = 0;

                        // if that was the equals sign, break out of 'for' loop.
                        if(sbiCrop == EQUALS_SIGN) {
                            break;
                        }
                    }
                }
            } else {
                System.err.println("Bad base 64 input character at " + i + ": " + source[i] + "(decimal).");
                return null;
            }
        }

        byte [] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    /**
     * Convert byte array to hexString.
     *
     * @param byteArr byte arr to convert
     * @return hex string
     */
    public static String byteArrayToHexString(byte[] byteArr) {
        StringBuilder hexString = new StringBuilder();

        for ( byte b : byteArr) {
            hexString.append(Integer.toHexString(b));
        }

        return hexString.toString();
    }

    /**
     * Convert base64 string to byte array.
     *
     * @param base64String base64 string.
     * @return byte array.
     */
    public static byte[] base64StringToByteArray(String base64String) {
        // Get the length of the base64 string.
        int len = base64String.length();

        // work out the byte array size we need. each character should corresponding to one byte. so it is easy.
        byte[] data = new byte[len];

        for (int i = 0; i < len; i += 1) {
            data[i] = (byte) base64String.charAt(i);
        }

        return data;
    }

    /**
     * Convert bas64 string to hex string.
     *
     * @param base64String
     * @return
     */
    public static String decodeHexString(String base64String) {

        // convert base64String to byte array.
        byte[] byteArr = Base64.base64StringToByteArray(base64String);

        // convert to normal byte array, 4 bytes back to 3 bytes.
        byte[] nbyteArr = Base64.decode(byteArr, 0, byteArr.length);

        // convert byte array to hex string.
        return Base64.byteArrayToHexString(nbyteArr);
    }

}
