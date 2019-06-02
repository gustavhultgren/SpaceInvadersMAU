package server;

import java.util.LinkedList;

/**
 * Synchronized buffer with methods to put and retreive elements from the buffer. Acts acording to FIFO. 
 * @author Tom Eriksson
 *
 * @param <T>
 */
public class Buffer<T> {
	private LinkedList<T> buffer = new LinkedList<T>();

	public synchronized void put(T obj) {
		buffer.addLast(obj);
		notifyAll();
	}

	public synchronized T get() throws InterruptedException {
		while (buffer.isEmpty()) {
			wait();
		}
		return buffer.removeFirst();
	}

	public int size() {
		return buffer.size();
	}

	public void clear() {
		for (T elem : buffer) {
			buffer.remove();
		}
	}
}
