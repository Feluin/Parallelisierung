package parallelisierung;

import parallelisierung.data.DataBase;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Test
{
    public static void main(String[] args)
    {
        DataBase dataBase=new DataBase();
        Set<BigInteger> primes = new HashSet<>();
        BigInteger lastprime =BigInteger.valueOf(0);
        BigInteger max=BigInteger.valueOf(10000);
        while (lastprime.compareTo(max) < 0)
        {
            if (BigMath.returnPrime(lastprime))
            {
                primes.add(lastprime);
            }
            lastprime = lastprime.add(BigInteger.ONE);
        }
        dataBase.addPrimes(primes);

        //Worker worker=new Worker(barrier, dataBase,1,null, startVal, i);
        //worker.load(BigInteger.valueOf(3), 0,BigInteger.valueOf(1000));
        //worker.start();
    }
}
