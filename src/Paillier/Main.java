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
    static int g, n, lambda;
    static double mu;
    public static void main(String[] args){
        int p = Integer.valueOf(args[0]), q = Integer.valueOf(args[1]);
        g = Integer.valueOf(args[2]);
        n = p*q;
        lambda = lcm(p-1, q-1);
        mu = mmi((int) (L(Math.pow(g, lambda)%Math.pow(n, 2))), n);
        System.out.println("n: " + n);
        System.out.println("Mu: " + mu);
        System.out.println("Lambda: " + lambda);
        System.out.println("Voting results: " + countVotes("src/files/pailler.txt"));
    }

    public static int countVotes(String fileName){
        BufferedReader br;
        int crypto = 1;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while(line!=null){
                crypto*=Integer.valueOf(line);
                line=br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Crypto mod n^2: " + crypto%Math.pow(n, 2));
        System.out.println("Message: " + decrypt(crypto));
        return (int) ((crypto%Math.pow(n, 2))%n);

    }

    public static double decrypt(int c){
        System.out.println("Decrypt " + Math.pow(c, lambda));
        return (L(Math.pow(c, lambda)%Math.pow(n, 2)));
    }

    public static int mmi(int x, int n){
        if(gcd(x, n) == 1) {
            System.out.println("x: " + x);
            BigInteger b1 = new BigInteger(Integer.toString(x));
            BigInteger mod = b1.modInverse(new BigInteger(Integer.toString(n)));
            return mod.intValue();
        }
        return -1;
    }

    public static int lcm(int a, int b){
        if(a==0 && b == 0){
            return 0;
        }
        return (a*b)/(gcd(a, b));
    }

    public static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b, a % b);
    }

    public static int L(double x){
        return (int) Math.round((x-1)/n);
    }
}
