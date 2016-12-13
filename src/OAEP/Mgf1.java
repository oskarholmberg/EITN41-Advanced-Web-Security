package OAEP;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by erik on 12/12/16.
 */
public class Mgf1 {
    public static byte[] mgf1(byte[] mgfSeed, int maskLen) throws NoSuchAlgorithmException {
        if (maskLen > Math.pow(2, 32)) {
            throw new IllegalArgumentException("Mask Length too long");
        }

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] result = new byte[0];


        // as (maskLen/hLen - 1) is (almost) always a too small number, a while loop is used instead
        int i = 0;
        while(result.length<maskLen){
            sha1.update(concatBytes(mgfSeed, I2OSP(i, 4)));
            byte[] digest = sha1.digest();
            result = concatBytes(result, digest);
            i++;
        }

        byte[] T = new byte[maskLen];
        System.arraycopy(result, 0, T, 0, maskLen);
        return T;
    }

    private static byte[] concatBytes(byte[] a, byte[] b){
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static byte[] concatBytes(byte[] ... bytes){
        byte[] result = new byte[0];
        for (int i = 0; i < bytes.length; i++){
            result = concatBytes(result, bytes[i]);
        }
        return result;
    }
    /**
     * Converts a nonnegative integer to an octet string of a specified length
     *
     * @param x    , nonnegative integer to be converted
     * @param xLen , length of the resulting octed string
     * @return x's corresponding octet string of length xLen
     */
    public static byte[] I2OSP(int x, int xLen) {
        if (x >= Math.pow(256, xLen)) throw new IllegalArgumentException("Integer too large");
        byte[] output = new byte[xLen];
        for (int i = xLen - 1; i >= 0; i--) {
            int shift = 8*i;
            output[xLen-i-1] = (byte) (x >>> shift);
        }
        return output;
    }
}
