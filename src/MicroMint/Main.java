package MicroMint;

import java.util.SplittableRandom;

public class Main {
    public static void main(String[] args){
        long t1 = System.currentTimeMillis();
        int u = Integer.valueOf(args[0]), k = Integer.valueOf(args[1]), c = Integer.valueOf(args[2]);
        int nbrBins = (int) Math.pow(2, u);
        SplittableRandom random = new SplittableRandom();
        System.out.println("u: " + u + ", k: " + k + ", c: " + c);
        System.out.println(mintCoins(k , c, nbrBins, random));

        System.out.println("Done! Computation took " + (System.currentTimeMillis() - t1) + "ms");
    }

    private static String mintCoins(int k, int c, int nbrBins, SplittableRandom random ){
        int[] bins = new int[nbrBins];
        int coinsFound = 0;
        int check = 0;
        int iterations = 0;
        double chance = 0.5;
        int checkFreq = (int) Math.sqrt(k * nbrBins * 0.5);

        while(c > coinsFound){
            int rand = random.nextInt(nbrBins);
            if(bins[rand] != -1) bins[rand] += 1;
            if(check >= checkFreq) {
                coinsFound += loot(bins, k);
                chance = 0.5 * (1 - (double) coinsFound/c);
                checkFreq = (int) Math.sqrt(k * nbrBins * chance);
                check = 0;
            }
            check ++;
            iterations++;
        }
        return "Found " + coinsFound + " in " + iterations + " iterations.";
    }

    private static int loot(int[] bins, int k){
        int coins = 0;
        for(int i = 0; i < bins.length; i++) {
            if(bins[i] >= k) {
                bins[i] = -1;
                coins ++;
            }
        }
        return coins;
    }
}
