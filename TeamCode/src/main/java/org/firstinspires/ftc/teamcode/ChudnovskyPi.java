package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Disabled
public class ChudnovskyPi {

    // Custom square root calculation to support BigDecimal
    private static BigDecimal sqrt(BigDecimal value, MathContext mc) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = value.divide(x0, mc).add(x0).divide(BigDecimal.valueOf(2), mc);
        }
        return x1;
    }

    // Constants used in the Chudnovsky formula
    private static final BigDecimal C = new BigDecimal("426880")
            .multiply(sqrt(new BigDecimal("10005"), new MathContext(100, RoundingMode.HALF_UP)));
    private static final BigDecimal K_CONST = new BigDecimal("545140134");
    private static final BigDecimal X_CONST = new BigDecimal("-262537412640768000");
    private static final BigDecimal SIX = new BigDecimal("6");

    public static void main(String[] args) {
        int digits = 1000; // Set precision here
        BigDecimal pi = calculatePi(digits);
        System.out.println("Calculated value of pi: ");
        System.out.println(pi);
    }

    public static BigDecimal calculatePi(int digits) {
        MathContext mc = new MathContext(digits + 5, RoundingMode.HALF_UP);
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal M = BigDecimal.ONE;
        BigDecimal L = new BigDecimal("13591409");
        BigDecimal X = BigDecimal.ONE;
        int k = 0;

        while (true) {
            BigDecimal term = M.multiply(L).divide(X, mc);
            sum = sum.add(term);

            // Break when the term is smaller than the required precision
            if (term.abs().compareTo(BigDecimal.ONE.scaleByPowerOfTen(-digits)) < 0) {
                break;
            }

            k++;
            BigDecimal kBig = new BigDecimal(k);
            BigDecimal K = SIX.multiply(kBig).subtract(BigDecimal.ONE);
            M = M.multiply(K).multiply(new BigDecimal(6 * k - 5)).multiply(new BigDecimal(6 * k - 1))
                    .divide(new BigDecimal(k * k * k * 24), mc);
            L = L.add(K_CONST);
            X = X.multiply(X_CONST);
        }

        return C.divide(sum, new MathContext(digits, RoundingMode.HALF_UP));
    }
}
