package LaunchCodes;


public class Main {
    //ENTER ARGS AS FOLLOWING:
    //k, n, degree, your participation number,
    //private polynomial coefficients (degree + 1),
    //other participants shared polynomials (n - 1),
    //other participants number and master shares ( (k - 1)*2 )
    public static void main(String[] args){
        int k = Integer.valueOf(args[0]), n = Integer.valueOf(args[1]), degree = Integer.valueOf(args[2]),
                participationNbr = Integer.valueOf(args[3]);
        int[] polynomial = new int[degree+1];
        int offset = 4;
        int myMasterShare = 0;

        //participantnbr vector and master point vector
        int[] ji = new int[k];
        int[] fi = new int[k];

        //gather polynomial koefficients
        for (int i = 0; i < degree + 1; i++){
            int coeff = Integer.valueOf(args[offset]);
            polynomial[i] = coeff;
            myMasterShare += (int) (coeff * Math.pow(participationNbr, i));
            offset++;
        }


        //gather shares from other participants and sum
        for (int i = 0; i < n-1; i++){
            myMasterShare+=Integer.valueOf(args[offset]);
            offset++;
        }

        //add myself to the calculations
        ji[0] = participationNbr;
        fi[0] = myMasterShare;

        //gather shared master points
        for (int i = 1; i < k; i++){
            ji[i] = Integer.valueOf(args[offset]);
            fi[i] = Integer.valueOf(args[offset + 1]);
            offset +=2;
        }

        //print out as a check
        for(int i = 0; i < fi.length; i++){
            System.out.println("j: " + ji[i] + " f(x): " + fi[i]);
        }

        //calculate code
        double code = 0;
        for (int i = 0; i < fi.length; i++){
            double upper = 1;
            double lower = 1;
            for (int j = 0; j < ji.length; j++){
                if (j!=i){
                    upper*=ji[j];
                    lower*=(ji[j] - ji[i]);
                }
            }
            code += (fi[i]*upper)/lower;
        }
        System.out.println("Code!: " + Math.round(code));
    }
}
