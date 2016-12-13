package OAEP;

import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;

import static OAEP.Mgf1.mgf1;

/**
 * Created by erik on 13/12/16.
 */
public class B3Main {
    public static void main(String[] args) {
        byte[] mfgSeed = DatatypeConverter.parseHexBinary(args[0]);
        int maskLen = Integer.valueOf(args[1]);
        System.out.println("mfgSeed: " + DatatypeConverter.printHexBinary(mfgSeed));
        System.out.println("maskLen: " + maskLen);

        try {
            System.out.println("Mask: " + DatatypeConverter.printHexBinary(mgf1(mfgSeed, maskLen)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
