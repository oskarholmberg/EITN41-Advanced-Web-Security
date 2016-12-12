package OTRChatt;


import com.sun.org.apache.xpath.internal.SourceTree;

import java.net.*;
import java.io.*;
import java.math.*;
import java.security.MessageDigest;
import java.sql.SQLOutput;
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
        String sp =(
                "FFFFFFFF FFFFFFFF C90FDAA2 2168C234 C4C6628B 80DC1CD1" +
                "29024E08 8A67CC74 020BBEA6 3B139B22 514A0879 8E3404DD" +
                "EF9519B3 CD3A431B 302B0A6D F25F1437 4FE1356D 6D51C245" +
                "E485B576 625E7EC6 F44C42E9 A637ED6B 0BFF5CB6 F406B7ED" +
                "EE386BFB 5A899FA5 AE9F2411 7C4B1FE6 49286651 ECE45B3D" +
                "C2007CB8 A163BF05 98DA4836 1C55D39A 69163FA8 FD24CF5F" +
                "83655D23 DCA3AD96 1C62F356 208552BB 9ED52907 7096966D" +
                "670C354E 4ABC9804 F1746C08 CA237327 FFFFFFFF FFFFFFFF").replace(" ", "");
        BigInteger p = new BigInteger(sp, 16);
        BigInteger g = new BigInteger("2");

        try {
            Socket client = new Socket(serverName, port);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // receive g**x1 and convert to a number
            String g_x1_str = in.readLine();
            System.out.println("RECEIVED: g**x1 = " + g_x1_str);
            BigInteger g_x1 = new BigInteger(g_x1_str, 16);

            // generate g**x2, x2 shall be a random number
            BigInteger x2 = BigInteger.probablePrime(16, rnd);
            // calculate g**x2 mod p
            BigInteger g_x2 = g.modPow(x2, p);
            // convert to hex-string and send.
            out.println(g_x2.toString(16));
            System.out.println("SENT: g_x2 = " + g_x2.toString(16));
            // read the ack/nak. This should yield a nak due to x2 being 0
            checkAck(in.readLine());

            // SMP negotiation
            // Get g1_a2
            BigInteger g1_a2 = new BigInteger(in.readLine(),16);
            System.out.println("RECEIVED: g1_a2 = " + g1_a2);
            // Send g1_b2
            BigInteger b2 = BigInteger.probablePrime(16, rnd);
            BigInteger g1_b2 = g.modPow(b2, p);
            out.println(g1_b2.toString(16));
            System.out.println("SENT: g1_b2 = " + g1_b2);
            checkAck(in.readLine());
            //Get g1_a3
            BigInteger g1_a3 = new BigInteger(in.readLine(), 16);
            System.out.println("RECEIVED: g1_a3 = " + g1_a3);
            //Send g1_b3
            BigInteger b3 = BigInteger.probablePrime(16, rnd);
            BigInteger g1_b3 = g.modPow(b3, p);
            out.println(g1_b3.toString(16));
            System.out.println("SENT: g1_b3 = " + g1_b3);
            checkAck(in.readLine());
            //Get Pa
            String Pa = in.readLine();
            System.out.println("RECEIVED: Pa = " + Pa);
            //Send Pb
            BigInteger g2 = g1_a2.modPow(b2, p);
            BigInteger g3 = g1_a3.modPow(b3, p);
            BigInteger b = BigInteger.probablePrime(16, rnd);
            BigInteger Pb = g3.modPow(b, p);
            out.println(Pb.toString(16));
            System.out.println("SENT: Pb = " + Pb);
            checkAck(in.readLine());
            //Get Qa
            BigInteger Qa = new BigInteger(in.readLine(), 16);
            System.out.println("RECEIVED: Qa = " + Qa);
            //Send Qb
            //Generate shared secret
            byte[] gxy = toUnassigned(g_x1.modPow(x2, p).toByteArray());
            byte[] pass = "eitn41 <3".getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(concat(gxy, pass));
            BigInteger y = new BigInteger(1, md.digest());
            BigInteger Qb = g.modPow(b, p).multiply(g2.modPow(y, p));
            out.println(Qb.toString(16));
            System.out.println("SENT: Qb = " + Qb);
            checkAck(in.readLine());
            BigInteger QaQb1 = new BigInteger(in.readLine(), 16);
            System.out.println("RECEIVED: QaQb1 = " + QaQb1);

            BigInteger QaQb2 = Qa.multiply(Qb.modInverse(p)).modPow(b3, p);
            out.println(QaQb2.toString(16));
            System.out.println("SENT: QaQb2 = " + QaQb2);
            checkAck(in.readLine());
            checkAck(in.readLine());
            System.out.println("Authentication successful!");


            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAck(String response) throws Exception {
        if(response.equals("ack")) System.out.println("RECEIVED: " + response);
        else throw new Exception("Got nack");
    }

    private byte[] concat(byte[] a, byte[] b){
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    private byte[] toUnassigned(byte[] a){
        return a;
    }
}
