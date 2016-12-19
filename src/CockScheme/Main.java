package CockScheme;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;

/**
 * Created by Oskar on 2016-12-19.
 */
public class Main {
    public static void main(String[] args){
        if(args.length != 4){
            throw new IllegalArgumentException("Not the right amount of parameters. Input should be given as; id p q encryption");
        }
        String id = args[0];
        BigInteger p = new BigInteger(args[1], 16), q = new BigInteger(args[2], 16), input = new BigInteger(args[3], 16);
        System.out.println("id: " + id + "\np: " + DatatypeConverter.printHexBinary(p.toByteArray()).substring(2, p.toByteArray().length*2) + "\nq: " + DatatypeConverter.printHexBinary(q.toByteArray()).substring(2, q.toByteArray().length*2) + "\ninput: " + DatatypeConverter.printHexBinary(input.toByteArray()).substring(2, input.toByteArray().length*2));
    }

    public static int jacobi(int a, int m){
        int j;
        if(m == 0){
            j = 1 ;
        } else if(a == 0){
            j = 0;
        } else if( (a & 1) == 1 ){
            j = jacobi( m%a, a);
            if( (a & 3)==3 &&  (m & 3)==3 )
                j = - j ;
        } else{
            j = jacobi( a >> 1, m);
            if( (m & 7) == 3 || (m & 7) == 5 )
                j = - j ;
        }
        return j;
    }
}
