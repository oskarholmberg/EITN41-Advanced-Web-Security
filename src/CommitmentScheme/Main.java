package CommitmentScheme;

import java.math.BigInteger;
import java.util.SplittableRandom;

/**
 * Created by erik on 30/11/16.
 */
public class Main {
    // enda inputen är om valet är 0 eller 1, bit-längden är fix 16
    public static void main(String[] args){
        BigInteger v = new BigInteger(args[0]);
        int k = 16;
        String kString = generateRandomKstring(k);

        runProbabilities(v, kString);
    }

    // här körs allting och data ska samlas, testa mellan alla bit-strängar från
    // 0000000000000000 till 1111111111111111
    private static void runProbabilities(BigInteger v, String kString){
        BigInteger originalHash = hashFunction(v, kString);
        int roofValue = (int) Math.pow(2, 16);

        for (int i = 0; i < roofValue; i++){
            String testKString = nextKString(i);

            BigInteger yesVote = hashFunction(new BigInteger("1"), testKString);
            BigInteger noVote = hashFunction(new BigInteger("0"), testKString);
            if (yesVote.compareTo(noVote) == 0){
                // this means that the concealement is better
            }

            BigInteger correctVote = hashFunction(v, testKString);
            // with vote 1-v (0 if 1, 1 if 0)
            BigInteger changeVote = hashFunction((new BigInteger("1").add(v.negate())), testKString);
            if (correctVote.compareTo(changeVote) == 0){
                // detta betyder att alice kan säga att hennes röst var en annan
            }
        }
    }

    private static int getNewTruncation(int prevTruncation){
        return prevTruncation*10;
    }

    // skapa nästa Kstring att testa mot (minsta längd alltid 16 bitar)
    private static String nextKString(int value){
        String kString = Integer.toBinaryString(value);
        while (kString.length() > 16){
            kString = "0" + kString;
        }
        return kString;
    }

    //skapa random kstring att hasha med
    private static String generateRandomKstring(int bitLength){
        SplittableRandom random = new SplittableRandom();
        String kString = Integer.toBinaryString(random.nextInt(0, (int) Math.pow(2, bitLength)));
        while(kString.length() > 16){
            kString = "0" + kString;
        }
        return kString;
    }

    //simpel hash funktion. stora primtal för att kunna använda längre trunkeringar
    // (storleken är dragna ur röven, kanske ska ändra storleken till något rimligare (read: mindre))
    private static BigInteger hashFunction(BigInteger v, String kString){
        BigInteger hash = new BigInteger("1601");
        hash = hash.multiply(new BigInteger("1153")).add(v);
        hash = hash.multiply(new BigInteger("2357").add(BigInteger.valueOf(kString.hashCode())));
        return hash;
    }
}
