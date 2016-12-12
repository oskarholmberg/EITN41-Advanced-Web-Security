package OAEP;

/**
 * Created by erik on 12/12/16.
 */
public class Main {
    public static void main(String[] args){
        String mfgSeed = args[0];
        int maskLen = Integer.valueOf(args[1]);


    }

    private String mgf1(String mfgSeed, int maskLen){
        if (maskLen > Math.pow(2, 32)){
            throw new IllegalArgumentException("Mask Length too long");
        }

        String T = "";

        return "";
    }
}
