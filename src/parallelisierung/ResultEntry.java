package parallelisierung;

import java.util.List;

public class ResultEntry {
    private List<Integer> factors;
    private Integer value;
    private Integer sqrt;
    private Integer last;


    public ResultEntry(List<Integer> factors, Integer last, Integer value, Integer sqrt) {
        this.factors = factors;
        this.value = value;
        this.sqrt = sqrt;
        this.last = last;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        factors.forEach(integer -> builder.append(integer).append("*"));
        return builder.toString() + last + "+1=" + value + " " + sqrt + "^2\n";
    }
}
