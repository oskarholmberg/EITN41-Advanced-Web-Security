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
        BigInteger v = new BigInteger("1");
        int k = 16;
        String kString = generateRandomKstring(k);

        runProbabilities(v, kString);
    }

    // main loop, tests with bit strings from
    // 0000000000000000 till 1111111111111111
    private static void runProbabilities(BigInteger v, String kString){
        int roofValue = (int) Math.pow(2, 16);
        double bind = 0.0, conc = 0.0;

        for (int i = 0; i < roofValue; i++){
            String testKString = nextKString(i);

            BigInteger yesVote = hashFunction(new BigInteger("1"), testKString);
            BigInteger noVote = hashFunction(new BigInteger("0"), testKString);
            if (yesVote.compareTo(noVote) == 0){
                conc++;
                // this means that the concealment is better
                // might need to be tested 2^16^2 times :S
            }

            BigInteger correctVote = hashFunction(v, testKString);
            // with vote 1-v (0 if 1, 1 if 0)
            BigInteger changeVote = hashFunction((new BigInteger("1").add(v.negate())), testKString);
            if (correctVote.compareTo(changeVote) == 0){
                bind++;
                // this means that alice can claim that her vote was a different one
            }
        }
        System.out.println("Binding was broken " + bind + " times, i.e " + (100*bind/roofValue)+ " percent of the time.");
        System.out.println("Concealment was broken " + conc + " times, i.e " + (100*conc/roofValue)+ " percent of the time.");
    }

    // create k-string, bit length is always 16
    private static String nextKString(int value){
        String kString = Integer.toBinaryString(value);
        while (kString.length() < 16){
            kString = "0" + kString;
        }
        return kString;
    }

    //create random k-string
    private static String generateRandomKstring(int bitLength){
        SplittableRandom random = new SplittableRandom();
        String kString = Integer.toBinaryString(random.nextInt(0, (int) Math.pow(2, bitLength)));
        while(kString.length() < 16){
            kString = "0" + kString;
        }
        System.out.println(kString);
        return kString;
    }

    //simple hash function, big primes in order to be able to use longer truncations
    // (the primes are conjured from nothing, probably change them to something more reasonable (read: smaller))
    private static BigInteger hashFunction(BigInteger v, String kString){
        BigInteger hash = new BigInteger("1601");
        hash = hash.multiply(new BigInteger("1153")).add(v);
        hash = hash.multiply(new BigInteger("2357").add(new BigInteger(kString)));
        String hashString = hash.toString().substring(0, Integer.min(x, hash.toString().length()));
        return new BigInteger(hashString);
    }
}
