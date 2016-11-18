package Ecash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

/**
 * Created by erik on 14/11/16.
 */
public class Alice {
    private int[][] quads;
    private int[][] xyHashes;
    private int[] bValues;


    // quads: [ai, ci, di, ri]
    public void generatreQuads(int amount) {
        SplittableRandom random = new SplittableRandom();
        quads = new int[amount][4];

        for (int i = 0; i < quads.length; i++){
            for (int j = 0; j < quads[i].length; j++){
                quads[i][j] = random.nextInt(10000) % Functions.MODN;
            }
        }
    }

    public void hashQuads(){
        xyHashes = new int[quads.length][2];
        for (int i = 0; i < quads.length; i++){
            xyHashes[i][0] = Functions.hashFunction(quads[i][0], quads[i][1]);
            xyHashes[i][1] = Functions.hashFunction(quads[i][0]^Functions.ALICEID, quads[i][2]);
        }
    }

    public void generateBValues(){
        bValues = new int[quads.length];

        for (int i = 0; i < quads.length; i++){
            bValues[i] = ((int) Math.pow(quads[i][3], 3) * Functions.fFunction(xyHashes[i][0], xyHashes[i][1])) % Functions.MODN;
        }
    }

    public int[] getBValues(){
        return bValues;
    }

    public HashMap<Integer,int[]> getIndexedQuads(ArrayList<Integer> kIndices) {
        HashMap<Integer, int[]> indexedQuads = new HashMap<>();
        for (int i : kIndices){
            indexedQuads.put(i, quads[i]);
        }
        return indexedQuads;
    }

    public double computeCoinSerial(int bankSereal, ArrayList<Integer> otherHalf){
        double invertedR = 1;
        for (int i = 0; i < otherHalf.size(); i++){
            invertedR = invertedR * quads[otherHalf.get(i)][3];
        }
        invertedR = 1 / invertedR;

        return (bankSereal * invertedR) % Functions.MODN;
    }
}
