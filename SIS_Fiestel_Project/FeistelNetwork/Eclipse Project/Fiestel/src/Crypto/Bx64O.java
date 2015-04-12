package Crypto;

import java.io.OutputStream;
import java.io.IOException;


// wrapper base64 stream
public class Bx64O extends OutputStream {

	  private OutputStream stream;
	  byte[] dataBuffer;
	  int nxtPosi;
	  boolean f_flag;
	
  public Bx64O(OutputStream stream) {
    this.stream = stream;
    dataBuffer = new byte[3];
    nxtPosi = 0;
    f_flag = false;
  }

  @Override
  public void write(int b) throws IOException {
    if (f_flag) {
      throw new IOException("Stream finished before...");
    }
    dataBuffer[nxtPosi++] = (byte)b;
    if (nxtPosi == dataBuffer.length) {
      flushBuffer();
      nxtPosi = 0;
    }
  }

  @Override
  public void close() throws IOException {
    // singleton condition of finish
    finish();
    stream.close();
    super.close();
  }

  //Write all data, do not miss any leftovers
  public void finish() throws IOException {
    if (f_flag) {
      return;
    }
    f_flag = true;
    // Flush any remainings by swapping with a temp buffer.
    if (nxtPosi > 0) {
      byte[] remaining = new byte[nxtPosi];
      for (int i = 0; i < nxtPosi; ++i) {
        remaining[i] = dataBuffer[i];
      }
      dataBuffer = remaining;
      flushBuffer();
    }
  }
  
  // flush all relevant data, do not miss any leftovers
  private void flushBuffer() throws IOException {
    try {
      char[] chars = Convertor.b64convertTo(dataBuffer);
      for (char c : chars) {
        stream.write(c);
      }
    } catch (IllegalArgumentException e) {
      throw new IOException(e);
    }
  }
}
