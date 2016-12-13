package SignatureCalc;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by oskar on 2016-12-13.
 */
public class Main {
    public static void main(String[] args) {
        String address = "https://eitn41.eit.lth.se:3119/ha4/addgrade.php?name=" + args[0] + "&grade=" + args[1] + "&signature=";
        URL url;
        try {

            HttpsURLConnection con;
            url = new URL(address);
            con = (HttpsURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
