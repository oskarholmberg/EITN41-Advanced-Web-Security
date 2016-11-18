package MicroMint;
import java.util.SplittableRandom;

public class Main {
    public static void main(String[] args){
        long t1 = System.currentTimeMillis();
        int u = Integer.valueOf(args[0]), k = Integer.valueOf(args[1]), c = Integer.valueOf(args[2]);
        int nbrBins = (int) Math.pow(2, u);
        SplittableRandom random = new SplittableRandom();
        int runtimes = 1;
        if(args.length > 3) runtimes = Integer.valueOf(args[3]);
        double totalIterations = 0;
        System.out.println("u: " + u + ", k: " + k + ", c: " + c);

        for(int i = 0; i < runtimes; i ++){
            int[] results = mintCoins(k, c, nbrBins, random);
            totalIterations += results[0];
        }

        System.out.println("Mean number of iterations is: " + (int)(totalIterations/runtimes));
        System.out.println("Done! Computation took " + (System.currentTimeMillis() - t1) + "ms");
    }

    private static int[] mintCoins(int k, int c, int nbrBins, SplittableRandom random ){
        int[] bins = new int[nbrBins];
        int coinsFound = 0;
        int iterations = 0;

        while(c > coinsFound){
            int rand = random.nextInt(nbrBins);
            bins[rand] += 1;
            if(bins[rand] == k) coinsFound ++;
            iterations++;
        }
        return new int[]{iterations, coinsFound};
    }
}
