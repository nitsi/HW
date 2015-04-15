package Part2;
/*  Operation Systems - Ex2
 *  Name:	Matan Gidnian
 *  ID:		200846905
 */
import java.io.File;
import java.util.LinkedList;

/**
 * A scouter thread This thread lists all sub-directories from a given root path. Each sub-directory is enqueued to be searched for files by Searcher threads. 
 */
public class Scouter implements Runnable {
	private File startDir;
	private LinkedList<File> scouterLinkedList;
	private SynchronizedQueue<File> dirQueue;
	
	/**
	 * Construnctor. Initializes the scouter with a queue for the directories to be searched and a root directory to start from.
	 * This method registers the scouter to the directory queue as a producer. 
	 * 
	 * @param directoryQueue A queue for directories to be searched
	 * @param root Root directory to start from
	 */
	public Scouter(SynchronizedQueue<File> directoryQueue, File root) {
		startDir = root;
		dirQueue = directoryQueue;
		
		scouterLinkedList = new LinkedList<File>();
		dirQueue.registerProducer();
	}
	
	/**
	 * Starts the scouter thread. Lists directories under root directory and adds them to queue, then lists directories
	 * in the next level and enqueues them and so on. This method ends by unregistering the scouter (as a producer) from the directory queue.
	 */
	@Override
	public void run() {
		File directory;
		File[] allFiles;
		
		dirQueue.enqueue(startDir);
		scouterLinkedList.addLast(startDir);
		
		while ((directory = scouterLinkedList.poll()) != null) 
		{
			// all nested dirs from our directory
			allFiles = directory.listFiles();
			if (allFiles != null) 
			{
				for (int  i = 0; i < allFiles.length; i++) 
				{
					if (allFiles[i].isDirectory()) 
					{
						dirQueue.enqueue(allFiles[i]);
						scouterLinkedList.addLast(allFiles[i]);
					}
				}
			}
		}
		dirQueue.unregisterProducer();
	}
}
