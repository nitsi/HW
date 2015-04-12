package Part2;
/*  Operation Systems - Ex2
 *  Name:	Matan Gidnian
 *  ID:		200846905
 */
import java.io.File;

public class Searcher implements Runnable {

	private String ext;
	private SynchronizedQueue<File> dirQueue;
	private SynchronizedQueue<File> resQueue;
	
	/**
	 * Constructor. Initializes the searcher thread and registers it as a producer on the results queue.
	 * 
	 * @param extension Extension to look for
	 * @param directoryQueue A queue with directories to search in (as listed by the scouter)
	 * @param resultsQueue A queue for files found (to be zipped by a zipper)
	 */
	public Searcher(String extension, SynchronizedQueue<File> directoryQueue, SynchronizedQueue<File> resultsQueue) {
		dirQueue = directoryQueue;
		resQueue = resultsQueue;
		
		ext = extension;
		resQueue.registerProducer();
	}
	
	/**
	 * Runs the searcher thread. Thread will fetch a directory to search in from the
	 * directory queue, then filter all files inside it (but will not recursively
	 * search subdirectories!). Files that are found to have the given extension
	 * are enqueued to the results queue.
	 * When finishes, this method unregisters from the results queue.
	 */
	@Override
	public void run() 
	{
		int lastPos;
		File reqDir;
		String fileName;
		File[] allFiles;
		
		while ((reqDir = dirQueue.dequeue()) != null)
		{
			allFiles = reqDir.listFiles();
			if (allFiles != null) {
				for (int  i = 0; i < allFiles.length; i++) 
				{
					fileName = allFiles[i].getName();
					lastPos = fileName.lastIndexOf('.');
					
					if (lastPos > 0 && fileName.substring(lastPos + 1).equals(ext)) 
					{
						resQueue.enqueue(allFiles[i]);
					}
				}
			}
		}	
		resQueue.unregisterProducer();
	}
}
