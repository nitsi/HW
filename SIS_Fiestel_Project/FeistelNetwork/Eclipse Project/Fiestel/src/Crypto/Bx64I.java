package Crypto;

import java.io.InputStream;
import java.io.IOException;

public class Bx64I extends InputStream {

	  private InputStream stream;
	  byte[] dataStorage;
	  int nxtPosi;
	  
  public Bx64I(InputStream stream) {
    this.stream = stream;
    this.dataStorage = null;
    this.nxtPosi = 0;
  }
  
  @Override
  public int read() throws IOException {
    if (nxtPosi % 3 == 0) {
      nxtPosi = 0;
      char[] charSet = new char[4];
      int inVal = stream.read();
      if (inVal == -1) {
        return -1;
      }
      charSet[0] = (char)inVal;
      charSet[1] = nextChar();
      charSet[2] = nextChar();
      charSet[3] = nextChar();
      try {
        dataStorage = Convertor.b64convertFrom(charSet);
      } catch (IllegalArgumentException e) {
        throw new IOException(e);
      }
    }
    if (dataStorage.length <= nxtPosi)
    { return -1; }
    // Range is 0 -> 255
    // Check for relevant range
    return dataStorage[nxtPosi++] & 0xff;
  }

  @Override
  public void close() throws IOException {
    stream.close();
    super.close();
  }

  //Check next char with reference of when is end of stream
  private char nextChar() throws IOException {
    int inVal = stream.read();
    if (inVal == -1) {
      throw new IOException("Unexpected end of stream reached!");
    }
    return (char)inVal;
  }
}
