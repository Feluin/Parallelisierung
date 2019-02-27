package parallelisierung;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
    private final ResultReciever reciever;
    private CyclicBarrier barrier;
    private volatile boolean timestop = false;
    private int number;
    private int cores;
    private int position;
    private ArrayList<Integer> list;


    public Worker(CyclicBarrier barrier, int number, int cores, ArrayList<Integer> list, ResultReciever reciever) {
        this.barrier = barrier;
        this.number = number;
        this.position = number;
        this.cores = cores;
        this.list = list;
        this.reciever =reciever;
    }

    public void run() {
        try {
            System.out.println(number + " starts");
            while (position < list.size()) {
                if (!timestop) {
                    doRecursion(List.of(list.get(position)));
                }
                position += cores;
            }
            System.out.println(number + " has finished;");
            barrier.await();

        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.println(ex);
        }
    }

    private void doRecursion(List<Integer> integers) {
        Integer prod = 1;
        for (Integer integer : integers) {
            prod = prod * integer;
        }
        for (Integer i = 0; i < list.get(list.size() - 1); i++)
            if (!timestop) if (BigMath.returnPrime(new BigInteger(String.valueOf(i)))) {
                Integer value = prod * i + 1;
                Integer sqrt = (int) Math.sqrt(value);
                if (value.equals(sqrt * sqrt)) {
                    List<Integer> list =new ArrayList<>(integers);
                    list.add(value);
                    reciever.recieve(new ResultEntry(integers, i, value, sqrt));
                    doRecursion(list);
                }
            }
    }

    public void stopthread() {
        this.timestop = true;
    }
}
