package parallelisierung;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class Application {
    private FileWriter fileWriter;
    // Ein Ryzen 1700x schafft in 15 min die Berechnugen von 0-82000;
    private int min=0;
    private int max=82000;

    private String filepath = "data/out.txt";

    public static void main(String[] args) {
        Application application = new Application();
        application.start();

    }

    public void start() {
        long startpoint=System.currentTimeMillis();
        try {
            File file = new File(filepath);
            file.createNewFile();
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int cores = Runtime.getRuntime().availableProcessors();
        CyclicBarrier barrier = new CyclicBarrier(cores,() -> {

            try {

                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("It took around "+((System.currentTimeMillis()-startpoint)/1000.0));
        });
        ArrayList<BigInteger> list = new ArrayList<>();
        int count = 0;
        int number = min;
        while (number < max) {
            BigInteger prime = new BigInteger("" + number);
            if (BigMath.returnPrime(prime)) {
                list.add(prime);
                count++;
            }
            number++;
        }
        System.out.println("There are "+count+"Primes to go");
        Timer stop= new Timer();
        for (int i = 0; i < cores; i++) {
            Worker worker = new Worker(barrier, i, cores, list, fileWriter);
            worker.start();

            stop.schedule(new TimerTask() {
                @Override
                public void run() {
                    worker.stopthread();
                }
            }, 900000);
        }
    }
}
