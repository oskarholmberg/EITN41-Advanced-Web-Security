package CommitmentScheme;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by erik on 30/11/16.
 */
public class Main {

    private static int x;
    private static int combinations = (int) Math.pow(2, 16);
    private static String[] v0 = new String[combinations], v1 = new String[combinations];
    private static HashMap<String, int[]> v0v1 = new HashMap<>();
    
    public static void main(String[] args){
        x = Integer.min(160, Integer.valueOf(args[0]));
        System.out.println("Truncating to " + x + " bits. Example hash: " + hashFunction(1, nextKString(1200)));
        populate();
        changedCommitment();
        concealBroken();
    }

    private static void changedCommitment(){
        double count = 0;
        for(int i = 0; i < combinations; i++){
            if(v0[i].equals(v1[i])){
                count++;
            }
        }
        System.out.println("Binding can be broken at " + count + " places, i.e " + (100*count/combinations)+ "% of the time.");
    }

    private static void concealBroken(){
        double count = 0;
        int min = Integer.MAX_VALUE;
        Iterator itr = v0v1.keySet().iterator();
        while(itr.hasNext()){
            String key = (String) itr.next();
            if(v0v1.get(key)[0] < 1 || v0v1.get(key)[1] < 1){
                count++;
            }
            if(min > (v0v1.get(key)[0] + v0v1.get(key)[1]) ){
                min = v0v1.get(key)[0] + v0v1.get(key)[1];
            }
        }
        System.out.println("Guessing at the easiest target has a " + (100.0/min) + "% chance of success. " + 100*count/v0v1.size() + "% of the hashes are compromised.");
    }

    private static void populate(){
        for(int i = 0; i < combinations; i++){
            String h0 = hashFunction(0, nextKString(i));
            String h1 = hashFunction(1, nextKString(i));
            v0[i] = h0;
            v1[i] = h1;
            if(!v0v1.containsKey(h0)){
                v0v1.put(h0, new int[]{1, 0});
            }else{
                int[] temp = v0v1.get(h0);
                temp[0]++;
                v0v1.put(h0, temp);
            }
            if(!v0v1.containsKey(h1)){
                v0v1.put(h1, new int[]{0, 1});
            }else{
                int[] temp = v0v1.get(h1);
                temp[1]++;
                v0v1.put(h1, temp);
            }
        }
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
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(kString.getBytes());
            sha1.update(String.valueOf(v).getBytes());
            byte[] digest = sha1.digest();
            String bitHash = "";
            for (byte b : digest){
                bitHash += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }
            return bitHash.substring(0, Integer.min(x, bitHash.length()));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
