package parallelisierung;

import java.math.BigInteger;
import java.util.List;

public class ResultEntry {
    private List<BigInteger> factors;
    private BigInteger square;
    private BigInteger root;



    public ResultEntry(List<BigInteger> factors, BigInteger square, BigInteger root) {
        this.factors = factors;
        this.square = square;
        this.root = root;

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        factors.forEach(integer -> builder.append(integer).append("*"));
        return builder.toString().substring(0,builder.toString().length()-1)+ "+1=" + square + " " + root + "^2\n";
    }
}
