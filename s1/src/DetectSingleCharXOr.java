import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetectSingleCharXOr {
    String fileLocation = "";
    List<String> lines = new ArrayList<String>();

    public DetectSingleCharXOr(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String detectEncrypted() {
        this.readFile();
        String theLine = "";

        Iterator<String> ite = this.lines.iterator();
        int i = 1;
        while (ite.hasNext()) {
            String thisLine = ite.next();

            SingleByteXorCipher singleByteXorCipher = new SingleByteXorCipher(thisLine);
            try {
                String decrypted = singleByteXorCipher.decryptedHexString();
                String mostFreq = SingleByteXorCipher.getMostFrequent(decrypted);
                int mostFreqNum = SingleByteXorCipher.getMostFrequentNum(decrypted);
                if (mostFreq.equals("20") && mostFreqNum > 3) {
                    theLine = SingleByteXorCipher.hexStringToAsciiString(decrypted);
                    System.out.println(theLine);
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
            i++;
        }

        return theLine;
    }

    private void readFile() {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(this.fileLocation));

            String line = reader.readLine();

            while (line != null) {
                this.lines.add(line);
//                System.out.println(line);
                line = reader.readLine();
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
