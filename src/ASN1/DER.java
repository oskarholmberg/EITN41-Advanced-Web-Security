package ASN1;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by oskar on 2016-12-20.
 */
public class DER {

    /**
     * Encodes the integer value to ASN.1 DER-format.
     * @param value integer to be encoded.
     * @return DER encoding of integer.
     */
    public static byte[] encode(BigInteger value){
        String code = value.toString();
        if(code.length() > Math.pow(2, 1008)-1){
            throw new IllegalArgumentException("Encoding error. Integer too large.");
        }
        String hexcode = value.toString(16);
        int contentOctets = hexcode.length()/2;
        int lengthOctets = (int) Math.ceil(Integer.toString(contentOctets, 16).length()/2.0);
        byte[] der = new byte[1+1+lengthOctets+contentOctets];
        der[0] = 0x02;
        der[1] = (byte) (0x80 | lengthOctets);
        BigInteger contentBytes = new BigInteger(Integer.toString(contentOctets));
        System.out.println(contentBytes.toByteArray().length);
        System.out.println(value.toByteArray().length);
        System.arraycopy(contentBytes.toByteArray(), 0, der, 2, contentBytes.toByteArray().length);
        System.arraycopy(value.toByteArray(), 0, der, 1+1+contentBytes.toByteArray().length, value.toByteArray().length);

        return der;
    }
}
