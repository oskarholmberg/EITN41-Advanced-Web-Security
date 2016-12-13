package SignatureCalc;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by oskar on 2016-12-13.
 */
public class Main {
    public static void main(String[] args) {
        String address = "/ha4/addgrade.php?name=" + args[0] + "&grade=" + args[1] + "&signature=";


        try {


            PrintWriter out;
            BufferedReader in;

            String signature = "";

            while (signature.length() < 20) {
                int[] times = new int[16];
                int lastSize = 0;
                int loopCount = 0;
                boolean nextHexFound = false;
                while(!nextHexFound && loopCount < 16){

                    String testHex = Integer.toHexString(loopCount);


                    SSLSocketFactory fac = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    SSLSocket socket = (SSLSocket) fac.createSocket("eitn41.eit.lth.se", 3119);
                    socket.startHandshake();

                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    long currentTime = System.currentTimeMillis();
                    out.println("GET " + address+signature+testHex);
                    out.println();
                    out.flush();

                    String line, result = "";
                    while((line = in.readLine()) != null){
                        result+=line;
                    }
                    out.close();
                    in.close();
                    socket.close();

                    times[loopCount] = (int) (System.currentTimeMillis() - currentTime);

                    for (int i = 0; i < times.length; i++){
                        int count = 0;
                        for (int e = 0; e < times.length; e++){
                            int neg = times[i] - times[e];
                            if (i!=e && neg > 25 && !(neg > 70) && times[e] != 0 && times[i] != 0)
                                count++;
                        }
                        if (count > 5){
                            nextHexFound = true;
                            signature+=Integer.toHexString(i);
                            break;
                        }

                    }
                    loopCount++;
                }
                for (int e = 0; e < times.length; e++){
                    System.out.print(Integer.toHexString(e) + ": " + times[e] + "  ");
                }
                System.out.println();
                System.out.println("Current signature: " + signature);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
