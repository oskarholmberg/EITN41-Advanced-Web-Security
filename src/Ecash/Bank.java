package Ecash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

/**
 * Created by erik on 14/11/16.
 */
public class Bank {

    private ArrayList<Integer> otherHalf;


    public ArrayList<Integer> getHalfIndices(int[] bValues){
        otherHalf = new ArrayList<>();
        ArrayList<Integer> kIndices = new ArrayList<>();
        for (int i = 0; i < bValues.length; i++){
            kIndices.add(i);
        }

        SplittableRandom random = new SplittableRandom();
        while (kIndices.size() > bValues.length / 2){
            otherHalf.add(kIndices.remove(random.nextInt(kIndices.size())));
        }
        return kIndices;
    }

    public boolean checkBValues(int[] bValues, HashMap<Integer, int[]> quads){
        for (int i : quads.keySet()){
            int xhash = Functions.hashFunction(quads.get(i)[0], quads.get(i)[1]);
            int yhash = Functions.hashFunction(quads.get(i)[0]^Functions.ALICEID, quads.get(i)[2]);
            int currentBValue = ((int) Math.pow(quads.get(i)[3], 3) * Functions.fFunction(xhash, yhash)) % Functions.MODN;
            if (currentBValue!=bValues[i])
                return false;
        }
        return true;
    }

    public int computeCoinSerial(int[] bValues){
        double serial = 1;
        for (int i = 0; i < otherHalf.size(); i++){
            serial = serial * Math.cbrt(bValues[otherHalf.get(i)]);
        }
        return (int) (serial % Functions.MODN);
    }

    public ArrayList<Integer> getOtherHalf(){
        return otherHalf;
    }
}
