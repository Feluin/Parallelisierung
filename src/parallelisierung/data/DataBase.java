package parallelisierung.data;

import parallelisierung.BigMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DataBase
{
    private HashMap<BigInteger, List<BigInteger>> database = new HashMap<>();
    private TreeSet<BigInteger> primeSet = new TreeSet<>();
    private BigInteger currentStepForPrime=BigInteger.ONE;

    public void addEntry(BigInteger product,
        List<BigInteger> primefactors)
    {
        if (!database.containsKey(product))
        {
            database.put(product, primefactors);
        }
    }

    public boolean contains(BigInteger factor)
    {
        return database.containsKey(factor);
    }

    public void addPrimes(Set<BigInteger> primes)
    {
        primeSet.addAll(primes);

    }

    public List<BigInteger> getPrimesasList()
    {
        return new ArrayList<>(primeSet);
    }

    public boolean isPrime(final BigInteger integer)
    {
        return primeSet.contains(integer);

    }

    public List<BigInteger> get(final BigInteger integer)
    {
        return database.get(integer);
    }

    public void calcNextPrimes(final BigInteger stepWidth)
    {
        BigInteger max =currentStepForPrime.add(stepWidth.multiply(BigInteger.TWO));
        Set<BigInteger> primes = new HashSet<>();
        while (currentStepForPrime.compareTo(max) < 0)
        {
            if (BigMath.returnPrime(currentStepForPrime))
            {
                primes.add(currentStepForPrime);
            }
            currentStepForPrime= currentStepForPrime.add(BigInteger.ONE);
        }
       primeSet.addAll(primes);
    }
    public DataBase(){
        calcNextPrimes(BigInteger.valueOf(50));
    }
}
