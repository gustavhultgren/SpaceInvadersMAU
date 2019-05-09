package server;

import java.util.LinkedList;
//gdgd
public class RunOnThreadN extends Thread {
	private Buffer<Runnable> buffer = new Buffer<Runnable>();
	private LinkedList<Worker> workers;
	private int nbrOfThreads;

	public RunOnThreadN(int n) {
		nbrOfThreads = n;
	}

	public synchronized void start() {
		Worker worker;
		if (workers == null) {
			workers = new LinkedList<Worker>();
			for (int i = 0; i < nbrOfThreads; i++) {
				worker = new Worker();
				worker.start();
				workers.add(worker);
			}
		}
	}

	public synchronized void stopp() {
		if (workers != null) {
			buffer.clear();
			for (Worker worker : workers) {
				worker.interrupt();
			}
			workers = null;
		}
	}

	public synchronized void execute(Runnable runnable) {
		buffer.put(runnable);
	}

	private class Worker extends Thread {
		public void run() {
			try {
				while (!Thread.interrupted()) {
					buffer.get().run();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
