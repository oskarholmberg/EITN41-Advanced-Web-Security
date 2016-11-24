package Ecash;


public class Functions {

    public static final int MODN = 37*53;
    public static final int ALICEID = 1337;

    public static int hashFunction(int a, int b){
        int hash = 23;

        hash = hash * 37 + a;
        hash = hash * 37 + b;

        return hash;
    }

    public static int fFunction(int x, int y){
        return x*y / 3;
    }


}
