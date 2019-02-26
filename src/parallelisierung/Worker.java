package parallelisierung;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
    private CyclicBarrier barrier;
    private volatile boolean timestop = false;
    private int number;
    private int cores;
    private int position;
    private ArrayList<Integer> list;
    private FileWriter fileOutput;

    public Worker(CyclicBarrier barrier, int number, int cores, ArrayList<Integer> list, FileWriter fileOutput) {
        this.barrier = barrier;
        this.number = number;
        this.position = number;
        this.cores = cores;
        this.list = list;
        this.fileOutput = fileOutput;
    }

    public void run() {
        try {
            fileOutput.write(number + " starts\n");
            while (position < list.size()) {
                if (!timestop) {
                    doRecursion(List.of(list.get(position)));
                }
                position += cores;
            }
            barrier.await();
            fileOutput.write(number + " has finished;\n");
        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
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
                    List<Integer> list = new ArrayList<>();
                    StringBuilder builder = new StringBuilder();
                    integers.forEach(integer -> builder.append(integer).append("*"));
                    try {
                        fileOutput.write(builder.toString() + i + "+1=" + value + " " + sqrt + "^2\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    list.add(value);
                    doRecursion(list);
                }
            }
    }

    public void stopthread() {
        this.timestop = true;
    }
}
