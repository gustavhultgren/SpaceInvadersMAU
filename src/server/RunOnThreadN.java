package server;

import java.util.LinkedList;
/**
 * 
 * Thread pool that executes tasks in the form of a request for the topp 100 score lists.
 * Starts with n Threads.
 * @author Tom Eriksson
 *
 */

public class RunOnThreadN extends Thread {
	private Buffer<Runnable> buffer = new Buffer<Runnable>();
	private LinkedList<Worker> workers;
	private int nbrOfThreads;

	public RunOnThreadN(int n) {
		nbrOfThreads = n;
	}

	//Starts all threads and adds them to the worker list.
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

	// Removes all threads from list.
	public synchronized void stopp() {
		if (workers != null) {
			buffer.clear();
			for (Worker worker : workers) {
				worker.interrupt();
			}
			workers = null;
		}
	}

	// Puts a Runnable into the task buffer to be executed.
	public synchronized void execute(Runnable runnable) {
		buffer.put(runnable);
	}

	// Thread class that executes Runnables from the task buffer.
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
