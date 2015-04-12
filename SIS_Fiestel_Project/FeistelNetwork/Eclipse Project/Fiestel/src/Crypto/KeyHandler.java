package Crypto;

import java.util.HashMap;
import java.io.InputStream;
import java.util.Map;
import java.io.IOException;

public class KeyHandler extends InputStream {
	  
  private InputStream stream;
	
  // This will contain the conversions for our key.
  private static final Map<Character, Integer> hexMap = new HashMap<Character, Integer>(16);
  
  static {
    char[] hexPoss = new char[] {'0', '1', '2', '3', '4', '5', '6', '7',
                              '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    for (int i = 0; i < hexPoss.length; i++) {
      hexMap.put(hexPoss[i], i);
    }
  }

  @Override
  public int read() throws IOException {
    // Read the most significant bits first.
    int mSigBit = read1Char();
    if (mSigBit == -1) { 
      return -1;
    }
    //After done with MSB -> LSB
    // LSB should not be the last part of the stream.
    int lSigBit = read1Char();
    if (lSigBit == -1) {
      throw new IOException("The end of the stream was reached beforehand.");
    }
    //finish
    return (mSigBit << 4) + lSigBit;
  }


  //converting hex to num, one by one for key handling
  private int read1Char() throws IOException {
    int newC = stream.read();
    
    //Whitespace manipulation.
    while (newC == '\n' || newC == ' ' ) {
      newC = stream.read();
    }
    // stream end ?
    if (-1 == newC) {
      return newC;
    }
    // Check it's a valid character.
    char carCase = (char)newC;
    if (hexMap.containsKey(carCase)) {
      return hexMap.get(carCase);
    }
    // Or its upper case version of it...
    char upper = Character.toUpperCase(carCase);
    if (hexMap.containsKey(upper)) {
      return hexMap.get(upper);
    }
    
    throw new IOException("Unknown character found: '" + carCase + "'");
  }
  
  public KeyHandler(InputStream stream) {
	    this.stream = stream;
	  }
  
  @Override
  public void close() throws IOException {
    stream.close();
  }

}
