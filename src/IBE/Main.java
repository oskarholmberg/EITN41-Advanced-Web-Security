package IBE;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Oskar on 2016-12-19.
 */
public class Main {

    private static BigInteger ZERO = new BigInteger("0"), ONE = new BigInteger("1"), TWO = new BigInteger("2"), THREE = new BigInteger("3"), FOUR = new BigInteger("4"), FIVE = new BigInteger("5"), SEVEN = new BigInteger("7"), EIGHT = new BigInteger("8");

    public static void main(String[] args){
        String id = args[0];
        BigInteger p = new BigInteger(args[1], 16), q = new BigInteger(args[2], 16);
        BigInteger[] bits = new BigInteger[args.length-3];
        for(int i = 0; i < bits.length; i ++){
            bits[i] = new BigInteger(args[i+3], 16);
        }
        BigInteger m = p.multiply(q);
        System.out.println("id: " + id + "\np: " + DatatypeConverter.printHexBinary(p.toByteArray()).substring(2, p.toByteArray().length*2) + "\nq: " + DatatypeConverter.printHexBinary(q.toByteArray()).substring(2, q.toByteArray().length*2));
        BigInteger a = ZERO;

        try {
            a = new BigInteger(DatatypeConverter.printHexBinary(idToHash(id.getBytes(), p.multiply(q))), 16);
            System.out.println("a: " + DatatypeConverter.printHexBinary(a.toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger r = calcR(a, p, q);
        System.out.println("r: " + DatatypeConverter.printHexBinary(r.toByteArray()).substring(2, r.toByteArray().length * 2)); //Prolly ugliest code evva.
        System.out.print("Message: ");
        for(BigInteger bit : bits){
            System.out.print(decrypt(bit, m, r) == 1 ? ("1") : ("0"));
        }
    }

    public static int decrypt(BigInteger s, BigInteger m, BigInteger r){
        return jacobi(s.add(r.multiply(TWO)), m);
    }

    public static BigInteger calcR(BigInteger a, BigInteger p, BigInteger q){
        BigInteger m = p.multiply(q);
        return a.modPow((m.add(FIVE).subtract(p.add(q))).divide(EIGHT), m);
    }

    public static int jacobi(BigInteger a, BigInteger n){
        int j = 0;
        if(a.equals(ZERO)){
            j = (n.equals(ONE) ? 1 : 0);
        }
        else if (a.equals(TWO)){
            switch (n.mod(EIGHT).intValue()){
                case 1:
                case 7:
                    j = 1;
                    break;
                case 3:
                case 5:
                    j = -1;
                    break;
            }
        }
        else if(a.compareTo(n) >= 0){
            j = jacobi(a.mod(n), n);
        }
        else if(a.mod(TWO).equals(ZERO)){
            j = jacobi(TWO, n)*jacobi(a.divide(TWO), n);
        }
        else{
            j = (a.mod(FOUR).equals(THREE) && n.mod(FOUR).equals(THREE) ? -jacobi(n, a):jacobi(n,a));
        }
        return j;
    }

    public static byte[] idToHash(byte[] id, BigInteger m) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(id);
        byte[] hash = md.digest();
        if(jacobi(new BigInteger(DatatypeConverter.printHexBinary(hash), 16), m) == 1) {
            return hash;
        }
        return idToHash(hash, m);
    }
}
