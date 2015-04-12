package Crypto;

import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.InputStream;

public class CryptLogic {

  //Block size
  public static final int BLOCK = 8;
  //Relevant bytes values of padding
  public static final byte PADDER_FIRST = (byte)0x80;
  public static final byte PADDER_TRAIL = 0;
  
  // Object for fiesteling
  private FeistelAlgoRun fiestelProc = new FeistelAlgoRun();

  public CryptLogic() {}
  
  public void encrypt(InputStream plain, OutputStream cipheredS, InputStream key) throws IOException {
	//Get the key from file.
    key = new KeyHandler(key);

    // Cipher is always in format base64.
    cipheredS = new Bx64O(cipheredS);

    // block process and init
    long pText = 0L;
    long cText = 0L;
    long keyPart = 0L; 

    byte[] block = new byte[BLOCK];
    boolean moreDataAvilable = true;
    
    //Read plain and get key part
    //and process
    while (moreDataAvilable) {
    	
      moreDataAvilable = plainTextReader(plain, block);
      pText = toLong(block);
      keyFileReader(key, block);
      keyPart = toLong(block);
      pText ^= cText; //XOR
      cText = fiestelProc.encrypt(pText, keyPart);
      cipheredS.write(fromLong(cText));
    }
    
    // do not forget last bits
    ((Bx64O)cipheredS).finish();
  }

  public void decrypt(InputStream cipher, OutputStream plain, InputStream key) throws IOException {
    
	key = new KeyHandler(key);
    
    cipher = new Bx64I(cipher);

    byte[] block = new byte[BLOCK];
    long lastCipherVal = 0L;
    long keyPart = 0L;
    long pText = 0L;
    
    boolean moreDataAvailable = cipherTextReader(cipher, block);
    long cipherVal = toLong(block);
    while (moreDataAvailable) {
     
    	// Read the next key block.
      keyFileReader(key, block);
      keyPart = toLong(block);
      
      // Decrypt the block.
      pText = fiestelProc.decrypt(cipherVal, keyPart);
      pText ^= lastCipherVal;
        
      // Store the cipher buffer for future use.
      lastCipherVal = cipherVal;
      // Read the next cipher block.
      moreDataAvailable = cipherTextReader(cipher, block);
      cipherVal = toLong(block);
      
      if (moreDataAvailable) {
        plain.write(fromLong(pText));
      }
    }
    // We've reached the end of the cipher. Process the padding and write the result.
    plain.write(brushPadAll(fromLong(pText)));
  }

  public boolean verify(InputStream plain, InputStream cipher, InputStream key) {
    try {
      // Assumes Hex key file.
      key = new KeyHandler(key);
      // Assume the cipher is Radix64.
      cipher = new Bx64I(cipher);

      // Prepare the process block and assisting values.
      byte[] block = new byte[BLOCK];
      long pText = 0L;
      long cText = 0L;
      long keyPrat = 0L;
      
      //As long as there is still data and also cioher have data
      boolean moreDataAvailable = true;
      
      // Process all data till onto finsih.
      while (moreDataAvailable) {
        // Read the next plain block.
        moreDataAvailable = plainTextReader(plain, block);
        pText = toLong(block);
        // Read the next key block.
        keyFileReader(key, block);
        keyPrat = toLong(block);
        // CBC requires XORing the former cipher/iv with the current plain.
          pText ^= cText;
        // Calculate the expected result block.
        cText = fiestelProc.encrypt(pText, keyPrat);
        // Now read the cipher block.
        if (!cipherTextReader(cipher, block)) {
          return false;
        }
        // Compare the results.
        if (cText != toLong(block)) {
          return false;
        }
      }
      // The plain stream should be empty now. Make sure that the cipher stream
      // is empty as well.
      if (cipher.read() != -1) {
        return false;
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  // Key getter
  private void keyFileReader(InputStream keyStream, byte[] key) throws IOException {
    int readByte;
    for (int i = 0; i < key.length; ++i) {
      readByte = keyStream.read();
      if (readByte == -1) {
        throw new IOException("Unexpected end of key");
      }
      key[i] = (byte)readByte;
    }
  }
  
  //Read all available plainText
  private boolean plainTextReader(InputStream plainStream, byte[] plainT) throws IOException {
    int readByte;
    int i = 0; 
    for (; (i < plainT.length) && ((readByte = plainStream.read()) >= 0); ++i) {
      plainT[i] = (byte)readByte;
    }
    if (i == plainT.length) {
      // We've reached the end of the buffer. No need for padding.
      return true;
    }
    // We need to pad the rest of the buffer.
    plainT[i++] = PADDER_FIRST;
    while (i < plainT.length) {
      plainT[i++] = PADDER_TRAIL;
    }
    return false;
  }
  
  // Read the cipher and store it.
  // return true if success, false otherwise for end of stream.
  private boolean cipherTextReader(InputStream cipherStream, byte[] ciphered) throws IOException {
    // First, check that the cipher has more data.
    int inByte = cipherStream.read();
    if (inByte == -1) {
      return false;
    }
    // read all buffer
    ciphered[0] = (byte)inByte;
    for (int i = 1; i < ciphered.length; ++i) {
      inByte = cipherStream.read();
      if (inByte == -1) {
        throw new IOException("Unexpected EOS");
      }
      ciphered[i] = (byte)inByte;
    }
    return true;
  }
  
  //Return the true buffered without ending to insert as needed
  private byte[] brushPadAll(byte[] buffer) throws IllegalArgumentException {
    // Padding length could be (0,buffer-len], so final length is [0,buffer-len).
    int length = buffer.length - 1;
    // Make sure the buffer has a possible padding suffix.
    if ((buffer[length] != PADDER_FIRST) && (buffer[length] != PADDER_TRAIL)) {
      throw new IllegalArgumentException("Corrupted pad buffer received?");
    }
    // count the padding length.
    while (length > 0 && buffer[length] == PADDER_TRAIL) {
      // Found a trailing padding, decrease the length.
      length--;
    }
    // No more trailing padding. The first padding byte should be found now.
    if (buffer[length] != PADDER_FIRST) {
      throw new IllegalArgumentException("Problem with first padding byte?");
    }
    // Build the result buffer and return.
    byte[] result = new byte[length];
    for (int i = 0; i < length; ++i) {
      result[i] = buffer[i];
    }
    return result;
  }
  
  // Converting byte to long
  public static long toLong(byte[] bytes) {
    long number = 0;
    // Constructs a new long number from given input
    for (int i = 0; i < bytes.length; i++) {
      // Move the MSBs upwords.
      number <<= Byte.SIZE;
      // Expand single byte into unsigned long.
      number |= ((long) bytes[i]) & 0xFF;
    }
    return number;
  }
  
  // Converting as needed
  public static byte[] fromLong(long value) {
    return ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(value).array();
  }
}
