package OAEP;

import java.security.NoSuchAlgorithmException;

import static OAEP.Mgf1.mgf1;

/**
 * Created by erik on 13/12/16.
 */
public class B3Main {
    public static void main(String[] args) {
        String mfgSeed = args[0];
        int maskLen = Integer.valueOf(args[1]);
        System.out.println("mfgSeed: " + mfgSeed);
        System.out.println("maskLen: " + maskLen);

        try {
            System.out.println("Mask: " + mgf1(mfgSeed, maskLen));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
