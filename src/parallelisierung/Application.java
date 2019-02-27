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

    private ResultReciever reciever;
    // Ein Ryzen 1700x schafft in 15 min die Berechnugen von 0-82000;
    private int min=0;
    private int max=5200;
    private String filepath = "data/out.txt";

    public static void main(String[] args) {
        Application application = new Application();
        application.start();

    }

    public void start() {
        long startpoint=System.currentTimeMillis();
        Timer stop= new Timer();
        try {
            File file = new File(filepath);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
           reciever=new ResultReciever(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int cores = Runtime.getRuntime().availableProcessors();
        CyclicBarrier barrier = new CyclicBarrier(cores,() -> {
            stop.cancel();
            reciever.flush();
            System.out.println("It took around "+((System.currentTimeMillis()-startpoint)/1000.0));
        });
        ArrayList<Integer> list = new ArrayList<>();
        int count = 0;
        Integer number = min;
        while (number < max) {
            BigInteger prime = new BigInteger(String.valueOf(number));
            if (BigMath.returnPrime(prime)) {
                list.add(number);
                count++;
            }
            number++;
        }
        System.out.println("There are "+count+"Primes to go");

        for (int i = 0; i < cores; i++) {
            Worker worker = new Worker(barrier, i, cores, list, reciever);
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
