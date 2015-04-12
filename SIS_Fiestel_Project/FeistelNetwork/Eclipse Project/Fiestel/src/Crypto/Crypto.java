package Crypto;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;

public class Crypto {

  // Some default values for the input files.
  public static final String CONFIG_FILE_NAME = "cfg.txt";
  public static final String PLAINTEXT_FILE_NAME = "p.txt";
  public static final String CIPHERTEXT_FILE_NAME = "c.txt";
  public static final String KEY_FILE_NAME = "k.txt";

  /*
   * This is the main of the whole project.
   * Should run automatically.
   */
  public static void main(String[] args) {

    InputStream key = null;
    InputStream insert1 = null;
    InputStream isert2 = null;
    OutputStream outcome = null;
    
    try {
    	
      //Read config file
      String configPath = System.getProperty("user.dir") + "/cfg.txt";
      RandomAccessFile configFile  = new RandomAccessFile(configPath, "r");
      String config = configFile.readLine().toLowerCase();
      configFile.close();
      
      // This objects provide working logic for our crypto
      CryptLogic crypto = new CryptLogic();
      
      // Open key readings.
      key = new FileInputStream(KEY_FILE_NAME);
      
      switch (config) {
        case "encrypt":
          System.out.println("Encrypting MODE!");
          insert1 = new FileInputStream(PLAINTEXT_FILE_NAME);
          outcome = new FileOutputStream(CIPHERTEXT_FILE_NAME);
          crypto.encrypt(insert1, outcome, key);
          System.out.println("ENC: Success !");
          break;
        case "decrypt":
          System.out.println("Decrypting MODE!");
          insert1 = new FileInputStream(CIPHERTEXT_FILE_NAME);
          outcome = new FileOutputStream(PLAINTEXT_FILE_NAME);
          crypto.decrypt(insert1, outcome, key);
          System.out.println("DEC: Success!");
          break;
        case "verify":
          System.out.println("Verifying MODE!");
          insert1 = new FileInputStream(PLAINTEXT_FILE_NAME);
          isert2 = new FileInputStream(CIPHERTEXT_FILE_NAME);
          if (crypto.verify(insert1, isert2, key)) {
            System.out.println("VER: Success!");
          } else {
            System.out.println("VER: Failed!");
          }
          break;
        default:
        //if you get this message please check config file is as instructed.
          throw new Exception("Please enter config file onl one of the following:" + System.lineSeparator()
        		  + "encrypt / decrypt / verify, use lowercase!"  + System.lineSeparator()
        		  + "Your input: " + config);
      }
    } 
    catch (Exception e) {
    	System.out.print(e.getMessage() + System.lineSeparator());
    	e.printStackTrace();
    } 
    finally 
    {
    	try {
    		if (key != null)
    			key.close();
    		if (insert1 != null)
    			insert1.close();
    		if (isert2 != null)
    			isert2.close();
    		if (outcome != null)
    			outcome.close();
    	}
    	catch (IOException e){
    		//Getting out here actually, nothing more.
    	}
    }
  }
}