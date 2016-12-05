package CommitmentScheme;

import java.math.BigInteger;
import java.util.SplittableRandom;

/**
 * Created by erik on 30/11/16.
 */
public class Main {

    private static int x;
    // only input is if the vote was 0 or 1 from start, k-bit length is fix to 16
    public static void main(String[] args){
        x = Integer.valueOf(args[0]);
        int v = 1;
        System.out.println("Hash truncated to: " + x + " bits. Example hash: " + hashFunction(v, nextKString(1200)));
        runProbabilities(v);
    }

    // main loop, tests with bit strings from
    // 0000000000000000 till 1111111111111111
    private static void runProbabilities(int v){
        int roofValue = (int) Math.pow(2, 16);
        double bind = 0.0, conc = 0.0;

        for (int i = 0; i < roofValue; i++){
            String testKString = nextKString(i);

            String correctVote = hashFunction(v, testKString);
            // with vote 1-v (0 if 1, 1 if 0)
            String changeVote = hashFunction((1-v), testKString);
            if (correctVote.equals(changeVote)){
                bind++;
                // this means that alice can claim that her vote was a different one
            }

            String yesVote = hashFunction(1, testKString);
            String noVote = hashFunction(0, nextKString(i));
                if (yesVote.equals(noVote)) {
                    conc++;
                    // this means that the concealment is better
                    // might need to be tested 2^16^2 times :S
            }


        }
        System.out.println("Binding was broken " + bind + " times, i.e " + (100*bind/roofValue)+ " percent of the time.");
        System.out.println("Concealment was broken " + conc + " times, i.e " + (1.0/conc) + " percent of the time.");
    }

    // create k-string, bit length is always 16
    private static String nextKString(int value){
        String kString = Integer.toBinaryString(value);
        while (kString.length() < 16){
            kString = "0" + kString;
        }
        return kString;
    }

    //simple hash function, big primes in order to be able to use longer truncations
    // (the primes are conjured from nothing, probably change them to something more reasonable (read: smaller))
    private static String hashFunction(int v, String kString){
        int hash = 53 * Math.abs((kString + v).hashCode());
        String s = Integer.toBinaryString(hash);
        return s.substring(0, Integer.min(x+1, s.length()));
    }
}
