import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parallelisierung.Worker;
import parallelisierung.data.DataBase;
import parallelisierung.resultout.ResultEntry;
import parallelisierung.resultout.ResultReciever;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class WorkerTest {



    @Test //3*5+1=16 4^2
    public void testPrimeFactor() throws Exception {
        DataBase dataBase= new DataBase();
        ResultReciever resultReciever=new ResultReciever(null);
        Worker worker= new Worker(new CyclicBarrier(1),dataBase,1,resultReciever,new BigInteger("1"),0, new BigInteger("10"));

        List<BigInteger> bigIntegers = worker.primeFactorSplitting(new BigInteger("15"));
        Assertions.assertEquals(2,bigIntegers.size());
        Assertions.assertTrue(bigIntegers.contains(BigInteger.valueOf(3)));
        Assertions.assertTrue(bigIntegers.contains(BigInteger.valueOf(5)));
    }
}
