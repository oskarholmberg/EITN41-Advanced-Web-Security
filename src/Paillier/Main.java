package Paillier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SplittableRandom;

/**
 * Created by erik on 30/11/16.
 */
public class Main {
    static int g, n, r;
    public static void main(String[] args){
        int p = Integer.valueOf(args[0]), q = Integer.valueOf(args[1]);
        g = Integer.valueOf(args[2]);
        SplittableRandom random = new SplittableRandom();
        n = p*q;
        r = random.nextInt(1, 1000);
        System.out.println("Voting results: " + countVotes("src/files/pailler.txt"));
    }

    public static int countVotes(String fileName){
        BufferedReader br;
        int cryptoSum = 1;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while(line!=null){
                cryptoSum*=Integer.valueOf(line);
                line=br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("CryptoSum=" + cryptoSum);
        System.out.println("Vtot=" + cryptoSum%Math.pow(n, 2));
        System.out.println("test=" + 52 % n);
        return (int) ((cryptoSum%Math.pow(n, 2))%n);

    }

    public static int generateSinglePrime(int lowerBound, int upperBound){
        SplittableRandom random = new SplittableRandom();
        ArrayList<Integer> primes = generatePrimes(lowerBound, upperBound);
        return primes.get(random.nextInt(0, primes.size()));
    }

    public static ArrayList<Integer> generatePrimes(int lowerBound, int upperBound){
        ArrayList<Integer> list = new ArrayList<>();
        // fill arraylist
        for (int i = 1; i <= upperBound; i++){
            list.add(i);
        }
        // remove non-primes
        int i = 0;
        boolean prime;
        while(i < list.size()){
            int e = 1;
            prime=true;
            while(prime && e < i){
                if (list.get(i)%list.get(e)==0){
                    list.remove(i);
                    prime=false;
                    i--;
                }
                e++;
            }
            i++;
        }

        //trim lowerbound
        while(list.get(0)<=lowerBound){
            list.remove(0);
        }
        return list;
    }
}
