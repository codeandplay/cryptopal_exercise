import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public class SingleByteXorCipher {
    private String hexString = "";
    private final static String SAMPLE_TXT = "A TreeMap is a Map that maintains its entries in ascending order, sorted according to the keys' natural ordering, or according to a Comparator provided at the time of the TreeMap constructor argument.The TreeMap class is efficient for traversing the keys in a sorted order. The keys can be sorted using the Comparable interface or the Comparator interface. SortedMap is a subinterface of Map, which guarantees that the entries in the map are sorted. Additionally, it provides the methods firstKey() and lastKey() for returning the first and last keys in the map, and headMap(toKey) and tailMap(fromKey) for returning a portion of the map whose keys are less than toKey and greater than or equal to fromKey";
    private Map<String, Integer> engFreq;

    public SingleByteXorCipher(String hexString) {
        this.hexString = hexString;
        this.getEngTxFreq();
    }

    public String decryptedHexString() throws Exception {

        Map<String, Integer> encryptedFreq = SingleByteXorCipher.buildHexCharFrequency(this.hexString);
        Map.Entry<String, Integer> encryptedFreqInt = encryptedFreq.entrySet().iterator().next();
        String cipherHex = encryptedFreqInt.getKey();

        Iterator<Map.Entry<String, Integer>> engFreqIterator = this.engFreq.entrySet().iterator();

        String mostLikly = "";


        // try the top 5
        for(int i = 0 ; i < 5; i ++){
            String engFreqHex = engFreqIterator.next().getKey();
           String hexCipher = FixedXOr.fixedXOr(engFreqHex, cipherHex );
           if(hexCipher.length() == 1){
               hexCipher = "0" + hexCipher;
           }
           StringBuilder decrypteString = new StringBuilder();

           // decrypted the whole hexString
            for(int j = 0; j < this.hexString.length(); j+=2 ){
                String decryptedHex = FixedXOr.fixedXOr((this.hexString.substring(j, j+1) + this.hexString.substring(j+1, j+2)), hexCipher);
                decrypteString.append((char) Integer.parseInt(decryptedHex, 16));
            }
            System.out.println(decrypteString);

            if(i == 0){
                mostLikly = decrypteString.toString();
            }
        }

        return mostLikly;
    }

    private void getEngTxFreq() {
        byte[] byteArr = new byte[SingleByteXorCipher.SAMPLE_TXT.length()];
        for (int i = 0; i < SingleByteXorCipher.SAMPLE_TXT.length(); i++) {
            byteArr[i] = (byte) SingleByteXorCipher.SAMPLE_TXT.charAt(i);
        }
        String hexString = Base64.byteArrayToHexString(byteArr);
        this.engFreq = SingleByteXorCipher.buildHexCharFrequency(hexString);
    }

    public static Map<String, Integer> buildHexCharFrequency(String str) {
        int hexLen = str.length();

        TreeMap<String, Integer> freqMap = new TreeMap<String, Integer>();

        for (int i = 0; i < hexLen; i += 2) {

            String byteString = String.valueOf(str.charAt(i)) +
                    String.valueOf(str.charAt(i + 1));

            if (freqMap.containsKey(byteString)) {
                freqMap.put(byteString, freqMap.get(byteString) + 1);
            } else {
                freqMap.put(byteString, 0);
            }
        }

        return (SingleByteXorCipher.crunchifySortByValue(freqMap));
    }

    // Let's sort HashMap by Value
    public static <K, V extends Comparable<? super V>> Map<K, V> crunchifySortByValue(Map<K, V> crunchifyMap) {

        Map<K, V> crunchifyResult = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> sequentialStream = crunchifyMap.entrySet().stream();

        // comparingByValue() returns a comparator that compares Map.Entry in natural order on value.
        sequentialStream.sorted(SingleByteXorCipher.comparingByValue()).forEachOrdered(c -> crunchifyResult.put(c.getKey(), c.getValue()));
        return crunchifyResult;
    }

    public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K,V>> comparingByValue() {
        return (Comparator<Map.Entry<K, V>> & Serializable)
                (c1, c2) -> c2.getValue().compareTo(c1.getValue());
    }
}


