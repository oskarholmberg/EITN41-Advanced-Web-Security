package ASN1;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;

/**
 * Created by oskar on 2016-12-20.
 */
public class Main {
    public static void main(String[] args){
        System.out.println(DatatypeConverter.printHexBinary(DER.encode(new BigInteger(args[0].replace(" ", "")))));
    }
}
