package DisclosureAttack;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.SplittableRandom;

/**
 * Created by oskar on 2016-11-24.
 */
public class Main {
    private static SplittableRandom random = new SplittableRandom();
    private static int amountUsers, aliceAmountRecs;


    public static void main(String[] args) {
        amountUsers = Integer.valueOf(args[0]);
        int aliceFreq = Integer.valueOf(args[1]);
        aliceAmountRecs = Integer.valueOf(args[2]);
        User[] users = new User[amountUsers];
        for (int i = 0; i < amountUsers - 1; i++) {
            users[i] = new User(i, getRecievers(false), random.nextInt(20, 30), random);
        }
        users[amountUsers - 1] = new User(amountUsers - 1, getRecievers(true), aliceFreq, random);
        System.out.println("System has " + amountUsers + " users. Alice sends messages to " + aliceAmountRecs + " other users.");
        long t1;
        boolean aliceSent = false;
        BitSet batch = new BitSet(amountUsers);
        ArrayList<BitSet> batches = new ArrayList<>();
        while (mutualyDisjoint(batches, aliceAmountRecs).isEmpty()) {
            t1 = System.currentTimeMillis();
            while (System.currentTimeMillis() < (t1 + 1)) {
                for (int i = 0; i < amountUsers - 1; i++) {
                    int recID = users[i].message();
                    if (recID != -1) {
                        batch.set(recID);
                    }
                }
                int aliceRec = users[amountUsers - 1].message();
                if (aliceRec != -1) {
                    batch.set(aliceRec);
                    aliceSent = true;
                }
            }
            if(aliceSent){
                batches.add(batch);
            }
            aliceSent = false;
            batch = new BitSet(amountUsers);
        }
    }

    public static int[] getRecievers(boolean alice) {
        int length = random.nextInt(2, 20);
        if (alice) length = aliceAmountRecs;
        int[] rec = new int[length];
        for (int i = 0; i < rec.length; i++) {
            rec[i] = random.nextInt(0, amountUsers);
        }
        return rec;
    }

    public static ArrayList<Integer> mutualyDisjoint(ArrayList<BitSet> batches, int m){
        ArrayList<Integer> indices = new ArrayList<>();
        for(int i= 0; i < batches.size(); i++){
            indices.add(i);
            for(int j = 0; j < batches.size(); j++){
                if(i!=j){
                    if(!batches.get(i).intersects(batches.get(j))){
                        indices.add(j);
                    }
                }
            }
            if(indices.size() >= m){
                for(int k : indices){
                    System.out.println(batches.get(k).toString());
                }
                return indices;
            }
            indices.clear();
        }
        return indices;
    }

}
