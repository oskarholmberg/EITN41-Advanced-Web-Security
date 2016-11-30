package CommitmentScheme;

import java.util.Random;

/**
 * Created by erik on 30/11/16.
 */
public class Main {
    public static void main(String[] args){
        boolean v = Boolean.valueOf(args[0]);
        int k = 16;
        byte[] kString = generateKString(k);


        System.out.println(kString + " " + kString.length);
    }

    private static byte[] generateKString(int bitLength){
        Random random = new Random();
        byte[] kString = new byte[bitLength/8];
        random.nextBytes(kString);
        return kString;
    }
}
