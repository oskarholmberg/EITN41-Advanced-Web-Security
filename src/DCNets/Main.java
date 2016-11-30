package DCNets;


public class Main {
    public static void main(String[] args) {
        if (args.length == 6) {
            int sa = Integer.parseInt(args[0], 16);
            int sb = Integer.parseInt(args[1], 16);
            int da = Integer.parseInt(args[2], 16);
            int db = Integer.parseInt(args[3], 16);
            int m = Integer.parseInt(args[4], 16);
            int b = Integer.parseInt(args[5]);
            if (b == 0) {
                System.out.println(Integer.toHexString(sa ^ sb) + Integer.toHexString(sa ^ sb ^ da ^ db));
            }
            else if (b == 1){
                System.out.println(Integer.toHexString(sa ^ sb ^ m));
            }
            else {
                System.out.println("Invalid input, try again");
            }
        }
        else {
            System.out.println("Not enough input parameters. Was " + args.length + ", should be 6.");
        }
    }
}
