package parallelisierung;

import java.util.List;

public class DataSetEntry {
    private List<Integer> factorprimes;
    private Integer product;

    public DataSetEntry(List<Integer> factorprimes, Integer product) {
        this.factorprimes = factorprimes;
        this.product = product;
    }

    public Integer getProduct() {
        return product;
    }

    public List<Integer> getFactorprimes() {
        return factorprimes;
    }
}
