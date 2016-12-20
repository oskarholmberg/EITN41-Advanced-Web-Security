package ASN1;

import javax.xml.bind.DatatypeConverter;
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
        return value.toByteArray().length > 127 ? encodeLong(value) : encodeShort(value);
    }

    private static byte[] encodeShort(BigInteger value){
        byte[] der = new byte[1+1+value.toByteArray().length];
        der[0] = 0x02;
        der[1] = (byte) value.toByteArray().length;
        System.arraycopy(value.toByteArray(), 0, der, 2, value.toByteArray().length);
        return der;
    }

    private static byte[] encodeLong(BigInteger value){
        if(value.compareTo(new BigInteger("2").pow(1008).subtract(new BigInteger("1"))) < 0) {
            throw new IllegalArgumentException("Encoding error. Integer too large.");
        }
        byte[] valueBytes = value.toByteArray();
        byte[] contentBytes = new BigInteger(Integer.toString(valueBytes.length)).toByteArray();
        if (contentBytes[0] == 0){
            contentBytes = Arrays.copyOfRange(contentBytes, 1, contentBytes.length);
        }
        byte[] der = new byte[1 + 1 + contentBytes.length + valueBytes.length];
        der[0] = 0x02;
        der[1] = (byte) (0x80 | contentBytes.length);
        System.arraycopy(contentBytes, 0, der, 2, contentBytes.length);
        System.arraycopy(valueBytes, 0, der, 2+contentBytes.length, valueBytes.length);

        return der;
    }
}
