package ASN1;

import java.math.BigInteger;

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
        BigInteger contentBytes = new BigInteger(Integer.toString(value.toByteArray().length));
        BigInteger lengthOctets = new BigInteger(Integer.toString(contentBytes.toByteArray().length));
        byte[] der = new byte[1+1+lengthOctets.intValue() + contentBytes.intValue()];
        der[0] = 0x02;
        der[1] = (byte) (0x80 | lengthOctets.intValue());
        System.arraycopy(contentBytes.toByteArray(), 0, der, 2, contentBytes.toByteArray().length);
        System.arraycopy(value.toByteArray(), 0, der, 1+1+contentBytes.toByteArray().length, value.toByteArray().length);

        return der;
    }
}
