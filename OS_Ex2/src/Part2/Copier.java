package Part2;

/*  Operation Systems - Ex2
 *  Name:	Matan Gidnian
 *  ID:		200846905
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *  A copier thread. Reads files to copy from a queue and copies them to the given destination.
 */
public class Copier implements Runnable {
	private File fileDest;
	String fPath;
	private SynchronizedQueue<File> resQueue;
	
	/**
	 * Constructor. Initializes the worker with a destination directory and a queue of files to copy. 
	 * 
	 * @param destination Destination directory
	 * @param resultsQueue Queue of files found, to be copied
	 */
	public Copier(File destination, SynchronizedQueue<File> resultsQueue) {
		fileDest = destination;
		fPath = fileDest.getAbsolutePath();
		resQueue = resultsQueue;
	}
	
	/**
	 * Runs the copier thread. Thread will fetch files from queue and copy them, one after each other, to the destination directory.
	 * When the queue has no more files, the thread finishes.
	 */
	@Override
	public void run() 
	{
		File file;
		File destFile;
		while ((file = resQueue.dequeue()) != null) 
		{
				try 
				{
					destFile = new File(fPath, file.getName());
					fileMirror(file, destFile);
				} 
				catch (IOException e) 
				{
					// Continue
				}
		}
	}
	
	private static void fileMirror(File source, File dest) throws IOException
	{
		int length;
	    InputStream fileIStream = null;
	    OutputStream fileOStream = null;
	    byte[] buffer = new byte[2048];
	    
	    try 
	    {
	        fileIStream = new FileInputStream(source);
	        fileOStream = new FileOutputStream(dest);
	        
	        while ((length = fileIStream.read(buffer)) > 0) 
	        {
	            fileOStream.write(buffer, 0, length);
	        }
	    } 
	    finally 
	    {
	        fileIStream.close();
	        fileOStream.close();
	    }
	}
}
