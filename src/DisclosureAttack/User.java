package DisclosureAttack;

import java.util.SplittableRandom;

/**
 * Created by oskar on 2016-11-24.
 */
public class User {

    private SplittableRandom random;
    private int[] comPart;
    private int id, freq;

    public User(int id, int[] comPart, int freq, SplittableRandom random){
        this.id = id;
        this.freq = freq;
        this.comPart = comPart;
        this.random = random;
    }

    public int message(){
        if(random.nextInt(1, freq) == 1){
            return comPart[random.nextInt(0, comPart.length - 1)];
        }
        return -1;
    }
}
