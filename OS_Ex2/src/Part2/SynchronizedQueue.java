package Part2;
/*  Operation Systems - Ex2
 *  Name:	Matan Gidnian
 *  ID:		200846905
 */

/**
 * A synchronized bounded-size queue for multithreaded producer-consumer applications.
 * 
 * @param <T> Type of data items
 */
public class SynchronizedQueue<T> {

	private T[] buffer;
	private int producers;
	
	private int startPosition;
	private int capacity;
	private int size;
	
	// Objects as locks for sync
	private Object procsLock;
	private Object qLock;
	
	/**
	 * Constructor. Allocates a buffer (an array) with the given capacity and
	 * resets pointers and counters.
	 * @param capacity Buffer capacity
	 */
	@SuppressWarnings("unchecked")
	public SynchronizedQueue(int capacity) {
		this.buffer = (T[])(new Object[capacity]);
		startPosition = 0;
		producers = 0;
		size = 0;
		this.capacity = capacity;
		procsLock = new Object();
		qLock = new Object();
	}
	
	/**
	 * Dequeues the first item from the queue and returns it.
	 * If the queue is empty but producers are still registered to this queue, 
	 * this method blocks until some item is available.
	 * If the queue is empty and no more items are planned to be added to this 
	 * queue (because no producers are registered), this method returns null.
	 * 
	 * @return The first item, or null if there are no more items
	 * @see #registerProducer()
	 * @see #unregisterProducer()
	 */
	public T dequeue() {
		synchronized (qLock) 
		{
			while (size == 0) 
			{
				if (producers == 0) 
				{
					return null;
				}
				try 
				{
					qLock.wait();
				} 
				catch (InterruptedException e) {
					// Ignore
				}
			}
			
			// size > 0
			T element = buffer[startPosition];
			size--;
			startPosition = (startPosition + 1) % capacity;
			qLock.notifyAll();
			return element;
		}
	}

	/**
	 * Enqueues an item to the end of this queue. If the queue is full, this 
	 * method blocks until some space becomes available.
	 * 
	 * @param item Item to enqueue
	 */
	public void enqueue(T item) {
		synchronized (qLock) {
			while (size == capacity) {
				try {
					qLock.wait();
				} catch (InterruptedException e) {
					// Ignore
				}
			}
			
			// size < capacity
			buffer[(startPosition + size) % capacity] = item;
			size++;
			qLock.notifyAll();
		}
	}

	/**
	 * Returns the capacity of this queue
	 * @return queue capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Returns the current size of the queue (number of elements in it)
	 * @return queue size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Registers a producer to this queue. This method actually increases the
	 * internal producers counter of this queue by 1. This counter is used to
	 * determine whether the queue is still active and to avoid blocking of
	 * consumer threads that try to dequeue elements from an empty queue, when
	 * no producer is expected to add any more items.
	 * Every producer of this queue must call this method before starting to 
	 * enqueue items, and must also call <see>{@link #unregisterProducer()}</see> when
	 * finishes to enqueue all items.
	 * 
	 * @see #dequeue()
	 * @see #unregisterProducer()
	 */
	public synchronized void registerProducer() {
		this.producers++;
	}

	/**
	 * Unregisters a producer from this queue. See <see>{@link #registerProducer()}</see>.
	 * 
	 * @see #dequeue()
	 * @see #registerProducer()
	 */
	public synchronized void unregisterProducer() {
		if (producers > 0) 
		{
			producers--;
			synchronized (qLock) {
				qLock.notifyAll();
			}
		}			
	}
}
