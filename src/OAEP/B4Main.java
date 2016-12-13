package OAEP;

/**
 * Created by erik on 13/12/16.
 */
public class B4Main {
    public static void main(String[] args){
        switch (args[0]){
            case "encode":
                String M = args[1];
                String seed = args[2];
                break;
            case "decode":
                String EM = args[1];
                break;
            default:
                throw new IllegalArgumentException("Input parameters wrong.");

        }

        
    }
}
