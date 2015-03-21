/*
 * Operation Systems - Exercise 1
 * ID: 200846905
 * Name: Matan Gidnian
 * Note: PDF file included
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class TimeTest {

	// Messages Constants
	private static final String USAGE = "Usage: java TimeTest [/force] source_file target_file buffer_size";
	
	public static void main(String[] args) throws Exception 
	{ 
		// Basic initialization
		String sourceDes, targetDes;
		int buffer;
		boolean forceFlag = false;
		
		// Argument validation
		if (args.length != 3 && args.length != 4)
			throw new Exception(USAGE);
		
		if (args.length == 4)
		{
			if (args[0].equals("/force"))
				forceFlag = true;
			else
			{
				throw new Exception(USAGE);
			}
			
			sourceDes = args[1];
			targetDes = args[2];
			buffer = Integer.parseInt(args[3]);
		}
		else
		{
			sourceDes = args[0];
			targetDes = args[1];
			buffer = Integer.parseInt(args[2]);
		}
		
		if (buffer < 1)
			throw new Exception("The buffer entered is smaller then 1, copying cannot be doe!");
		
		// Copy file
		long startTime = System.currentTimeMillis();
		
		boolean copyStatus = copyFile(sourceDes, targetDes, buffer, forceFlag);
		
		long endTime = System.currentTimeMillis(); 
		
		if(copyStatus)
		{
			System.out.println("File " + sourceDes + " was copied to " + targetDes);
			System.out.println("Total Time: " + (endTime - startTime) + "ms");
		}
		else
		{
			System.out.println("COPY FAILED! : An error occured while trying to copy the file, please view relevant printed Exceptions");
		}
	}
	
	/**
	 * Copies a file to a specific path, using the specified buffer size. *
	 * @param srcFileName File to copy
	 * @param toFileName Destination file name
	 * @param bufferSize Buffer size in bytes
	 * @param bOverwrite If file already exists, overwrite it
	 * @return true when copy succeeds, false otherwise
	 * @throws Exception 
	 */
	public static boolean copyFile(String srcFileName, String toFileName,
			int bufferSize, boolean bOverwrite) throws Exception {
		
		char[] charChunk = new char[bufferSize];
		BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        int readed = 0;
        
        File checkFile = new File(toFileName);
        if (checkFile.exists() && !checkFile.isDirectory() && !bOverwrite)
        {
        	throw new Exception(System.lineSeparator() + toFileName + " exists, invalid overwrite argument passed"
        			+ System.lineSeparator() + USAGE);
        }
        
        try 
        {
            inputStream = new BufferedReader(new FileReader(srcFileName));
            outputStream = new PrintWriter(new FileWriter(toFileName));	
            
            while ((readed =inputStream.read(charChunk, 0, bufferSize)) != -1)
            {
            	if (charChunk.length - readed == 0)
            		outputStream.write(charChunk);
            	else
            		outputStream.write(charChunk, 0, readed);
            }
        }
        catch (FileNotFoundException e) 
        {
        	System.out.println("Requested source file not found");
			e.printStackTrace();
			return false;
		} 
        catch (IOException e) 
        {
        	System.out.println("A problem has occured while trying to access the output file");
			e.printStackTrace();
			return false;
		} 
        finally 
		{
            try
            {
            	if (inputStream != null) 
            		inputStream.close();
            
            	if (outputStream != null) 
            		outputStream.close();
            }
            catch (Exception e)
            {
            	System.out.println("A problem has occured while trying to close a stream");
            	e.printStackTrace();
    			return false;
            }
        }
        return true;
	}
}