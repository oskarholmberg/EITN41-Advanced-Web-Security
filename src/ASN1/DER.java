package ASN1;

/**
 * Created by oskar on 2016-12-20.
 */
public class DER {

    /**
     * Encodes the integer to ASN.1 DER-format
     * @param value hex code to be encoded
     * @return DER encoding of integer
     */
    public static byte[] encode(int value){
        String code = String.valueOf(value);
        if(code.length() > Math.pow(2, 1008)-1){
            throw new IllegalArgumentException("Encoding error. Integer too large.");
        }


    }
}
