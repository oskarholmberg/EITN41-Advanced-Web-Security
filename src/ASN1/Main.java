package ASN1;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by oskar on 2016-12-20.
 */
public class Main {
    private static BigInteger ONE = new BigInteger("1");
    public static void main(String[] args){
        if (args.length==1) {
            System.out.println(DatatypeConverter.printHexBinary(DER.encode(new BigInteger(args[0].replace(" ", "")))));
        } else {
            BigInteger p = new BigInteger(args[0]);
            BigInteger q = new BigInteger(args[1]);
            BigInteger e = new BigInteger("65537");
            BigInteger n = p.multiply(q);
            BigInteger phi = (p.subtract(ONE)).multiply((q.subtract(ONE)));
            BigInteger d = e.modInverse(phi);
            BigInteger exp1 = d.mod(p.subtract(ONE));
            BigInteger exp2 = d.mod(q.subtract(ONE));
            BigInteger coeff = q.modInverse(p);
            ArrayList<byte[]> derCodes = new ArrayList<>();
            derCodes.add(DER.encode(n));
            derCodes.add(DER.encode(e));
            derCodes.add(DER.encode(d));
            derCodes.add(DER.encode(p));
            derCodes.add(DER.encode(q));
            derCodes.add(DER.encode(exp1));
            derCodes.add(DER.encode(exp2));
            derCodes.add(DER.encode(coeff));
            int totLength = 0;
            for (byte[] b : derCodes){
                totLength+=b.length;
            }
            byte[] content = new byte[5+totLength];
            content[0] = 0x30;
            content[1] = (byte) (3+totLength);
            content[2] = 0x02;
            content[3] = 0x01;
            content[4] = 0x00;
            int offset = 5;
            for (byte[] b : derCodes){
                System.arraycopy(b, 0, content, offset, b.length);
                offset+=b.length;
            }
            System.out.println(DatatypeConverter.printHexBinary(content));
            System.out.println(DatatypeConverter.printBase64Binary(content));

        }
    }
}
