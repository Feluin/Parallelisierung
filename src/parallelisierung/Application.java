package parallelisierung;

import parallelisierung.data.DataBase;
import parallelisierung.resultout.ResultReciever;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CyclicBarrier;

public class Application {

    private ResultReciever reciever;

    private BigInteger startVal = BigInteger.TWO;
    private BigInteger stepWidth = new BigInteger("1000");
    private String filepath = "./data/out.txt";
    private List<Worker> allworkers = new ArrayList<>();

    private DataBase dataBase = new DataBase();

    public static void main(String[] args) {
        Application application = new Application();
        application.start();

    }

    public void start() {
        Timer stop = new Timer();
        try {
            File file = new File(filepath);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            reciever = new ResultReciever(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int cores = Runtime.getRuntime().availableProcessors();

        CyclicBarrier barrier = new CyclicBarrier(cores, () -> {
            reciever.flush();
            dataBase.calcNextPrimes(stepWidth);
            allworkers.forEach(Worker::nextStep);

        });
        for (int i = 0; i < cores; i++) {
            Worker worker = new Worker(barrier, dataBase, cores, reciever, startVal, i, stepWidth);
            allworkers.add(worker);
        }
        stop.schedule(new TimerTask() {
            @Override
            public void run() {
               allworkers.forEach(Worker::stopThread);
               barrier.reset();
            }
        },90000);
        dataBase.calcNextPrimes(stepWidth);
        for (int i = 0; i < allworkers.size(); i++) {
            final Worker worker = allworkers.get(i);
            worker.nextStep();
            worker.start();
        }
    }
}
