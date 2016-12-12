package OTRChatt;


import java.net.*;
import java.io.*;
import java.math.*;
import java.util.*;

class Client {
    public static void main(String[] args) {
        new Client().run();
    }

    void run() {
        String serverName = "eitn41.eit.lth.se";
        int port = 1337;
        Random rnd = new Random();
        // the p shall be the one given in the manual
        BigInteger p = new BigInteger(
                "FFFFFFFF FFFFFFFF C90FDAA2 2168C234 C4C6628B 80DC1CD1" +
                "29024E08 8A67CC74 020BBEA6 3B139B22 514A0879 8E3404DD" +
                "EF9519B3 CD3A431B 302B0A6D F25F1437 4FE1356D 6D51C245" +
                "E485B576 625E7EC6 F44C42E9 A637ED6B 0BFF5CB6 F406B7ED" +
                "EE386BFB 5A899FA5 AE9F2411 7C4B1FE6 49286651 ECE45B3D" +
                "C2007CB8 A163BF05 98DA4836 1C55D39A 69163FA8 FD24CF5F" +
                "83655D23 DCA3AD96 1C62F356 208552BB 9ED52907 7096966D" +
                "670C354E 4ABC9804 F1746C08 CA237327 FFFFFFFF FFFFFFFF");
        BigInteger g = new BigInteger("2");

        try {
            Socket client = new Socket(serverName, port);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // receive g**x1 and convert to a number
            String g_x1_str = in.readLine();
            System.out.println("g**x1: " + g_x1_str);
            BigInteger g_x1 = new BigInteger(g_x1_str, 16);

            // generate g**x2, x2 shall be a random number
            BigInteger x2 = new BigInteger("0");
            // calculate g**x2 mod p
            BigInteger g_x2 = g.modPow(x2, p);
            // convert to hex-string and send.
            out.println(g_x2.toString(16));
            // read the ack/nak. This should yield a nak due to x2 being 0
            System.out.println("\nsent g_x2: " + in.readLine());

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
