package Paillier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.SplittableRandom;

/**
 * Created by erik on 30/11/16.
 */
public class Main {
    static BigInteger g, n, lambda, neg1, mu;
    public static void main(String[] args){
        BigInteger p = new BigInteger(args[0]);
        BigInteger q = new BigInteger(args[1]);
        neg1 = new BigInteger("1").negate();
        g = new BigInteger(args[2]);
        n = p.multiply(q);
        lambda = lcm(p.add(neg1), q.add(neg1));
        mu = (L(g.pow(lambda.intValue()).mod(n.pow(2)))).modInverse(n);
        System.out.println("Voting results: " + countVotes("src/files/pailler.txt"));
    }

    public static int countVotes(String fileName){
        BufferedReader br;
        BigInteger crypto = new BigInteger("1");
        int votes = 0;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while(line!=null){
                crypto = crypto.multiply(new BigInteger(line));
                votes++;
                line=br.readLine();
            }
            System.out.println("Crypto: " + crypto);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int result = decrypt(crypto).mod(n).intValue();
        return result > votes ? result-n.intValue() : result;
    }

    public static BigInteger decrypt(BigInteger c){
        return L(c.modPow(lambda, n.pow(2))).multiply(mu).mod(n);
    }

    public static BigInteger lcm(BigInteger a, BigInteger b){
        if(a.intValue()==0 && b.intValue() == 0){
            return new BigInteger("0");
        }
        return a.multiply(b).divide(a.gcd(b));
    }

    public static BigInteger L(BigInteger x){
        return x.add(neg1).divide(n);
    }
}
