package DisclosureAttack;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

public class Main {
    private static SplittableRandom random = new SplittableRandom();
    private static int amountUsers, aliceAmountRecs;


    public static void main(String[] args) {


        amountUsers = Integer.valueOf(args[0]);
        int aliceFreq = Integer.valueOf(args[1]);
        aliceAmountRecs = Integer.valueOf(args[2]);
        ArrayList<BitSet> mdBatches = new ArrayList<>();
        User[] users = new User[amountUsers];
        for (int i = 0; i < amountUsers - 1; i++) {
            users[i] = new User(i, getRecievers(false), random.nextInt(6000, 12000), random);
        }
        users[amountUsers - 1] = new User(amountUsers - 1, getRecievers(true), aliceFreq, random);
        System.out.println("System has " + amountUsers + " users. Alice sends messages to " + aliceAmountRecs + " other users.");
        long t1, t2 = System.currentTimeMillis();
        int mailsSent = 0, counter = 0, stage=1, amountBatches = 0;
        boolean aliceSent = false;
        BitSet batch = new BitSet(amountUsers);
        ArrayList<BitSet> batches = new ArrayList<>();
        while (stage!=3) {
            t1 = System.currentTimeMillis();
            while (System.currentTimeMillis() < (t1 + 1)) {
                for (int i = 0; i < amountUsers - 1; i++) {
                    int recID = users[i].message();
                    if (recID != -1) {
                        batch.set(recID);
                        mailsSent++;
                    }
                }
                int aliceRec = users[amountUsers - 1].message();
                if (aliceRec != -1) {
                    batch.set(aliceRec);
                    aliceSent = true;
                    mailsSent++;
                }
            }
            amountBatches++;
            if(aliceSent){
                if (stage==1){
                    batches.add(batch);
                } else {
                    batches = checkExclusion(mdBatches, batch);
                    if (checkLengths(mdBatches)){
                        stage=3;
                    }
                }
                if (System.currentTimeMillis() > (t2+3000)){
                    System.out.println("Mails sent: " + mailsSent);
                    t2=System.currentTimeMillis();
                    for(BitSet b : mdBatches){
                        System.out.print(b.cardinality() + " ");
                    }
                    System.out.println();
                }
            }
            aliceSent = false;
            batch = new BitSet(amountUsers);
            counter++;
            if (counter > 100 && stage==1){
                ArrayList<Integer> indices = mutuallyDisjoint(batches, aliceAmountRecs);
                if (!indices.isEmpty()) {
                    stage = 2;
                    mdBatches.addAll(indices.stream().map(batches::get).collect(Collectors.toList()));
                }
                counter=0;
            }
        }
        System.out.println("Done! Total amount of mails sent: " + mailsSent + " avarage amount mails per batch " + mailsSent/amountBatches);
        System.out.println("Hacked IDs: " + mdBatches + " What alice actually had:");
        int[] aliceRealRec = users[amountUsers-1].comPart;
        for (int anAliceRealRec : aliceRealRec) {
            System.out.print(anAliceRealRec + " ");
        }
    }

    private static boolean checkLengths(ArrayList<BitSet> mdBatches){
        for (BitSet b : mdBatches){
            if (b.cardinality()>1){
                return false;
            }
        }
        return true;
    }

    private static ArrayList<BitSet> checkExclusion(ArrayList<BitSet> mdBatches, BitSet batch){
        boolean andIt = true;
        for (int i = 0; i < mdBatches.size(); i++){
            if (mdBatches.get(i).intersects(batch)) {
                for (int j = 0; j < mdBatches.size(); j++) {
                    if (i != j && mdBatches.get(j).intersects(batch))
                        andIt = false;
                }
                if (andIt){
                    mdBatches.get(i).and(batch);
                    return mdBatches;
                }
                andIt = true;
            }
        }
        return mdBatches;
    }

    private static int[] getRecievers(boolean alice) {
        int length = random.nextInt(5, 10);
        if (alice) length = aliceAmountRecs;
        int[] rec = new int[length];
        for (int i = 0; i < rec.length; i++) {
            rec[i] = random.nextInt(0, amountUsers);
        }
        return rec;
    }

    private static ArrayList<Integer> mutuallyDisjoint(ArrayList<BitSet> batches, int m){
        ArrayList<Integer> indices = new ArrayList<>();
        boolean disjoint = true;
        for(int i= 0; i < batches.size(); i++){
            indices.add(i);
            for(int j = 0; j < batches.size(); j++){
                if(i!=j){
                    for (Integer k : indices) {
                        if (batches.get(k).intersects(batches.get(j))) {
                            disjoint = false;
                        }
                    }
                    if (disjoint)
                        indices.add(j);
                    disjoint=true;
                }
            }
            if(indices.size() >= m){
                return indices;
            }
            indices.clear();
        }
        return indices;
    }

}
