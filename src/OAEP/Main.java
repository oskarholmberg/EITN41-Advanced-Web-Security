package OAEP;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by erik on 12/12/16.
 */
public class Main {
    public static void main(String[] args){
        String mfgSeed = args[0];
        int maskLen = Integer.valueOf(args[1]);
        System.out.println("mfgSeed: " + mfgSeed);
        System.out.println("maskLen: " + maskLen);
        System.out.println("C59E86CAA7340844C56BAEA42056F6F61A7BA9E1".getBytes().length);

        try {
            System.out.println(mgf1(mfgSeed, maskLen));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private static String mgf1(String mfgSeed, int maskLen) throws NoSuchAlgorithmException {
        if (maskLen > Math.pow(2, 32)) {
            throw new IllegalArgumentException("Mask Length too long");
        }


        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        sha1.update("dummystring".getBytes());
        int hlen = sha1.digest().length;
        String counter = "dummystring";
        byte[] T = new byte[hlen];
        byte[] result = new byte[hlen];
        System.out.println("amount of loops: " + (maskLen/hlen-1));

        for (int i = 0; i <= (maskLen/hlen - 1); i++){
            System.out.println("Nbr run: " + i);
            sha1.reset();
            sha1.update(mfgSeed.getBytes());
            sha1.update(counter.getBytes());
            byte[] digest = sha1.digest();
            result = new byte[digest.length + T.length];
            System.arraycopy(T, 0, result, 0, T.length);
            System.arraycopy(digest, 0, result, T.length, digest.length);
        }

        T = new byte[hlen];
        System.out.println("T length: " + T.length);
        System.arraycopy(result, 0, T, 0, maskLen);
        return bytesToHex(T);
    }

    private static String bytesToHex(byte[] bytes){
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
