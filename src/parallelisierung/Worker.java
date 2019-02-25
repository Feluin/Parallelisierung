package parallelisierung;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
	private CyclicBarrier barrier;
	private  volatile boolean timestop=false;
	private int number;
	private int cores;
	private int position;
	private ArrayList<BigInteger> list;
	private FileWriter fileOutput;

	public Worker(CyclicBarrier barrier, int number, int cores, ArrayList<BigInteger> list, FileWriter fileOutput) {
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
				if(!timestop){
                    doRecursion(new BigInteger[] { list.get(position) });
                }
				position += cores;
			}
			barrier.await();
			fileOutput.write(number+" has finished;\n");
		} catch (InterruptedException | BrokenBarrierException ex) {
			System.out.println(ex);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doRecursion(BigInteger[] bigIntegers) {

		BigInteger prod = BigInteger.ONE;
		for (int i = 0; i < bigIntegers.length; i++) {
			prod = prod.multiply(bigIntegers[i]);
		}
		for (int i = 0; i < list.get(list.size()-1).intValue(); i++) {
            if(!timestop){
			BigInteger prime = new BigInteger("" + i);
			if (BigMath.returnPrime(prime)) {
				BigInteger value = prod.multiply(prime).add(new BigInteger("1"));
				BigInteger sqrt = BigMath.sqrt(value);
				if (sqrt.multiply(sqrt).equals(value)) {
					BigInteger[] list = new BigInteger[bigIntegers.length + 1];
					for (int j = 0; j < bigIntegers.length; j++) {
						list[j] = bigIntegers[j];
						try {
							fileOutput.write(list[j] + "*");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					try {
						fileOutput.write(prime + "+1=" + value + " " + sqrt.intValue() + "^2\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
					list[list.length - 1] = prime;
					doRecursion(list);
				}
			}
		}}
	}

	public void stopthread() {
		this.timestop =true;
	}
}
