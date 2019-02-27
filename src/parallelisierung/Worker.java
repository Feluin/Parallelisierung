package parallelisierung;

import parallelisierung.data.DataBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
    private final ResultReciever reciever;
    private BigInteger startVal;
    private CyclicBarrier barrier;
    private DataBase dataBase;
    private volatile boolean timestop = false;
    private int cores;
    private int offset;
    private BigInteger stepWidth;
    private BigInteger stepstop;

    public Worker(final CyclicBarrier barrier, DataBase dataBase, int cores, ResultReciever reciever, final BigInteger startVal, final int offset, BigInteger stepWidth) {
        this.barrier = barrier;
        this.dataBase = dataBase;

        this.cores = cores;
        this.reciever = reciever;
        this.startVal = startVal;
        this.offset = offset;
        this.stepWidth = stepWidth;
    }

    public void run() {
        try {
            startVal = startVal.add(BigInteger.valueOf(offset));
            while (!timestop) {
                BigInteger square = startVal.multiply(startVal);
                BigInteger result = square.subtract(BigInteger.ONE);
                List<BigInteger> factors = primeFactorSplitting(result);
                reciever.recieve(new ResultEntry(factors,square,startVal));
                startVal = startVal.add(BigInteger.valueOf(cores));
                if (startVal.compareTo(stepstop) > 0) {
                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException ignored) {
                        break;
                    }
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void nextStep() {
        stepstop = startVal.add(stepWidth);
    }

    private List<BigInteger> primeFactorSplitting(final BigInteger result) throws Exception {
        BigInteger current = new BigInteger(String.valueOf(result));
        List<BigInteger> factorlist = new ArrayList<>();
        BigInteger product = BigInteger.ONE;
        while (!dataBase.isPrime(current)) {
            if (dataBase.contains(current)) {
                factorlist.addAll(dataBase.get(current));
                dataBase.addEntry(result, factorlist);
                return factorlist;
            }
            List<BigInteger> primes = dataBase.getPrimesasList();
            int iterator = 0;
            while (iterator < primes.size()) {

                BigInteger prime = primes.get(iterator);
                if (current.remainder(prime).equals(BigInteger.ZERO)) {
                    factorlist.add(prime);
                    product = product.multiply(prime);
                    current = current.divide(prime);
                    dataBase.addEntry(product, factorlist);
                    break;
                }
                iterator++;
            }
            if (iterator == primes.size()) {
                throw new Exception();
            }
        }
        factorlist.add(current);
        dataBase.addEntry(result, factorlist);
        return factorlist;
    }

    public void stopthread() {
        this.timestop = true;
    }
}
