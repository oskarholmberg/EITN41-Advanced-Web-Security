package SignatureCalc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by oskar on 2016-12-13.
 */
public class Main {
    public static void main(String[] args){
        String address = "https://eitn41.eit.lth.se:3119/ha4/addgrade.php?name=" + args[0]+"&grade="+args[1]+"&signature=";
        try {
            URL url =  new URL(address);
            URLConnection connection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
