package OAEP;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by erik on 13/12/16.
 */
public class B4Main {
    static int k = 128;
    public static void main(String[] args){
        switch (args[0]){
            case "encode":
                try {
                    encodeMessage(DatatypeConverter.parseHexBinary(args[1]), DatatypeConverter.parseHexBinary(args[2]));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case "decode":
                try {
                    decodeMessage(DatatypeConverter.parseHexBinary(args[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalArgumentException("Input parameters wrong.");
        }


    }

    private static void encodeMessage(byte[] message, byte[] seed) throws NoSuchAlgorithmException {
        int mLen = message.length;
        byte[] L = new byte[0];

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        sha1.update(L);
        byte[] lHash = sha1.digest();
        int hLen = lHash.length;
        byte[] PS = generateZeroPadding(k - mLen - 2*hLen - 2);
        byte[] DB = Mgf1.concatBytes(lHash, PS, DatatypeConverter.parseHexBinary("01"), message);
        byte[] dbMask = Mgf1.mgf1(seed, k - hLen - 1);
        byte[] maskedDB = xorByteArrays(DB, dbMask);
        byte[] seedMask = Mgf1.mgf1(maskedDB, hLen);
        byte[] maskedSeed = xorByteArrays(seed, seedMask);
        byte[] EM = Mgf1.concatBytes(generateZeroPadding(1), maskedSeed, maskedDB);

        System.out.println("OAEP_encode=" + DatatypeConverter.printHexBinary(EM));
    }

    private static void decodeMessage(byte[] encodedMessage) throws Exception {
        byte[] lHash = new byte[0];

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        sha1.update(lHash);
        lHash = sha1.digest();
        int hLen = lHash.length;
        byte[] Y = new byte[1];
        byte[] maskedSeed = new byte[hLen];
        byte[] maskedDB = new byte[k-hLen-1];
        System.arraycopy(encodedMessage, 0, Y, 0, Y.length);
        System.arraycopy(encodedMessage, Y.length, maskedSeed, 0, maskedSeed.length);
        System.arraycopy(encodedMessage, Y.length+maskedSeed.length, maskedDB, 0, maskedDB.length);
        byte[] seedMask = Mgf1.mgf1(maskedDB, hLen);
        byte[] seed = xorByteArrays(maskedSeed, seedMask);
        byte[] dbMask = Mgf1.mgf1(seed, k-hLen-1);
        byte[] DB = xorByteArrays(maskedDB, dbMask);
        byte[] lHashPrime = new byte[hLen];
        System.arraycopy(DB, 0, lHashPrime, 0, hLen);

        int i = hLen;
        while(DB[i]==0){
            i++;
        }
        byte[] PS = new byte[i-hLen];
        byte[] separationByte = new byte[1];
        byte[] message = new byte[DB.length-i-1];
        System.arraycopy(DB, hLen, PS, 0, PS.length);
        System.arraycopy(DB, i, separationByte, 0, separationByte.length);
        System.arraycopy(DB, i+1, message, 0, message.length);

        if (!Arrays.equals(lHash, lHashPrime)){
            throw new Exception("Decoding error: lHash!=lHashPrime");
        } else if (Y[0]!=0){
            throw new Exception("Decoding error: Y nonzero");
        } else if (separationByte[0]!=DatatypeConverter.parseHexBinary("01")[0]){
            throw new Exception("Decoding error: no separation byte");
        }

        System.out.println("OAEP_decode=" + DatatypeConverter.printHexBinary(message));
    }

    private static byte[] xorByteArrays(byte[] a, byte[] b){
        if (a.length > b.length) {
            byte[] tmp = b;
            b = a;
            a = tmp;
        }
        for (int i = 0; i < a.length; i++) {
            b[i] ^= a[i];
        }
        return b;
    }

    private static byte[] generateZeroPadding(int paddingLength){
        if (paddingLength > 0)
            return new byte[paddingLength];
        return new byte[0];
    }
}
