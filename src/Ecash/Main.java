package Ecash;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        Alice alice = new Alice();
        Bank bank = new Bank();

        int k = 10;

        // alice generates 2k quadruples, x & y hashes as well as 2k B values
        alice.generatreQuads(2 * k);
        alice.hashQuads();
        alice.generateBValues();

        int[] bValues = alice.getBValues();

        // bank generates a list of random k (half) indices out of alices provided indices
        ArrayList<Integer> kIndices = bank.getHalfIndices(bValues);

        // fetch k (half) of the indices quadruples
        HashMap<Integer, int[]> indexedQuads = alice.getIndexedQuads(kIndices);

        // check if ID is ok through calculating B with provided quadruples
        if (bank.checkBValues(bValues, indexedQuads)){
            System.out.println("ID is ok for all");
            // bank computes coin serial using the other half of indices
            int bankSerial = bank.computeCoinSerial(bValues);
            System.out.println("bankserial: " + bankSerial);

            // alice extracts the coin serial
            System.out.println("computed coin serial: " + alice.computeCoinSerial(bankSerial, bank.getOtherHalf()));
        } else {
            System.out.println("ID does not correspond");
        }
    }
}
