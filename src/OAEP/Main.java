package OAEP;

/**
 * Created by erik on 12/12/16.
 */
public class Main {
    public static void main(String[] args) {
        String mfgSeed = args[0];
        int maskLen = Integer.valueOf(args[1]);

    }

    private String mgf1(String mfgSeed, int maskLen) throws IllegalArgumentException {
        if (maskLen > Math.pow(2, 32)) {
            throw new IllegalArgumentException("Mask Length too long");
        }
        String T = "";

        return "";
    }

    /**
     * Converts a nonnegative integer to an octet string of a specified length
     *
     * @param x    , nonnegative integer to be converted
     * @param xLen , length of the resulting octed string
     * @return x's corresponding octet string of length xLen
     */
    private static byte[] I2OSP(int x, int xLen) {
        if (x >= Math.pow(256, xLen)) throw new IllegalArgumentException("Integer too large");
        byte[] output = new byte[xLen];
        for (int i = xLen - 1; i >= 0; i--) {
            int shift = 8*i;
            output[i] = (byte) (x >>> shift);
        }
        return output;
    }
}
