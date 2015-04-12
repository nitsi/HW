package Part2;

import java.io.File;

/*  Operation Systems - Ex2
 *  Name:	Matan Gidnian
 *  ID:		200846905
 */

/**
 * Main application class. This application searches for all files under some given path that contain a given textual pattern.
 * All files found are copied to some specific directory. 
 */
public class DiskSearcher {
	
	private static final String ARGS_AMOUNT = "";
	private static final String SEARCH_AMOUNT_ERR = "";
	private static final String WORKERS_AMOUNT_ERR = "";
	private static final String INVALID_NUM_ARG = "";
	
	public static void main(String[] args) 
	{
		int searchAmount = 0;
		int copyWorkersAmount = 0;
		
		if (args.length != 5) 
		{
			System.err.println(ARGS_AMOUNT);
			System.exit(1);
		}
		
		String extension = args[0];
		File startDir = new File(args[1]);
		File destDir = new File(args[2]);
		
		if (!destDir.isDirectory())
		{
			destDir.mkdir();
		}
		
		try
		{
			searchAmount = Integer.parseInt(args[3]);
			copyWorkersAmount = Integer.parseInt(args[4]);
		}
		catch (NumberFormatException e)
		{
			System.err.println(INVALID_NUM_ARG);
			System.exit(1);
		}
		
		validate(searchAmount, copyWorkersAmount);
		
		SynchronizedQueue<File> dirQueue = new SynchronizedQueue<File>(1000);
		SynchronizedQueue<File> resQueue = new SynchronizedQueue<File>(1000);
		
		Searcher[] searcherWorkers = new Searcher[searchAmount];
		Copier[] copyWorkers = new Copier[copyWorkersAmount];
		
		Scouter scouter = new Scouter(dirQueue, startDir);
		Thread scoutingThread = new Thread(scouter);
		scoutingThread.start();
		
		Thread[] searchPool = new Thread[searchAmount];
		Thread[] copyPool = new Thread[copyWorkersAmount];
		
		for (int i = 0; i < searchAmount; i++) 
		{
			searcherWorkers[i] = new Searcher(extension, dirQueue, resQueue);
			searchPool[i] = new Thread(searcherWorkers[i]);
			searchPool[i].start();
		}
		
		for (int i = 0; i < copyWorkersAmount; i++) 
		{
			copyWorkers[i] = new Copier(destDir, resQueue);
			copyPool[i] = new Thread(copyWorkers[i]);
			copyPool[i].start();
		}
	
		try 
		{
			scoutingThread.join();
			for (int i = 0; i < searchPool.length; i++) 
			{
				searchPool[i].join();
			}
			for (int i = 0; i < copyPool.length; i++) 
			{
				copyPool[i].join();
			}
		} 
		catch (InterruptedException e) 
		{
			// Continue...
		}
		
		System.out.println("Operation Completed Successuflly... Thank God !");
	}
	
	private static void validate (int search_Amount, int copyWorkers_Amount)
	{
		if (search_Amount < 1)
		{
			System.err.println(SEARCH_AMOUNT_ERR);
			System.exit(1);
		}
		
		if (copyWorkers_Amount < 1)
		{
			System.err.println(WORKERS_AMOUNT_ERR);
			System.exit(1);
		}
	}		
}
