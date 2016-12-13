package OAEP;

/**
 * Created by erik on 13/12/16.
 */
public class B4Main {
    public static void main(String[] args){
        boolean encode = args[0].equals("encode");
        String M, seed, EM;
        if (encode) {
            M = args[1];
            seed = args[2];
        } else {
            EM = args[1];
        }

        
    }
}
