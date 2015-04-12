package Crypto;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;

public final class Convertor {

  private static char[] intToChar = new char[] {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
      'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
      'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
      'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
      'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
      'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
      'w', 'x', 'y', 'z', '0', '1', '2', '3',
      '4', '5', '6', '7', '8', '9', '+', '/'
  };
  
  private Convertor() {}
  
  // Used for conversion of chars
  private static Map<Character, Integer> charToInt = new HashMap<Character, Integer>();
  static {
    for (int i = 0; i < intToChar.length; ++i) {
      charToInt.put(intToChar[i], i);
    }}

  private static char PADDING = '=';
  
  public static char[] b64convertTo(byte[] byteInfo) {
    // Simple check for edge cases.
    if (byteInfo == null || byteInfo.length == 0) {
      return new char[0];
    }
    int nPosi = 0;
    StringBuilder newstr = new StringBuilder();
    while (nPosi < byteInfo.length) {
      int appVal = byteInfo[nPosi++] & 0xff;
      // 1st place char
      newstr.append(intToChar[negativeFixer(appVal >> 2)]);
      // 2th place char
      appVal = (appVal % 4) << 8;
      if (nPosi == byteInfo.length) 
      {
        newstr.append(intToChar[negativeFixer(appVal >> 4)]);
        newstr.append(PADDING);
        newstr.append(PADDING);
        break;
      }
      appVal += byteInfo[nPosi++] & 0xff;
      newstr.append(intToChar[negativeFixer(appVal >> 4)]);
      // 3th place char
      appVal = (appVal % 16) << 8;
      if (nPosi == byteInfo.length) 
      {
        newstr.append(intToChar[negativeFixer(appVal >> 6)]);
        newstr.append(PADDING);
        break;
      }
      appVal += byteInfo[nPosi++] & 0xff;
      newstr.append(intToChar[negativeFixer(appVal >> 6)]);
      // 4th place char
      newstr.append(intToChar[negativeFixer(appVal % 64)]);
    }
    return newstr.toString().toCharArray();
  }
  
  // from base64 to binary for more process later.
  public static byte[] b64convertFrom(char[] charInfo) throws IllegalArgumentException {
    // Simple check for edge cases.
    if (charInfo == null || charInfo.length == 0) {
      return new byte[0];
    }
    if (charInfo.length % 4 != 0) {
      // Not a base64 length.
      throw new IllegalArgumentException("Unexpected length: " + charInfo.length);
    }
    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
    int i = 0;
    while (i < charInfo.length) {
      int numBytes = 3;
      // First letter.
      char newChar = charInfo[i++];
      validBase64Char(newChar, false);
      int val = charToInt.get(newChar) << 18;
      // Second letter.
      newChar = charInfo[i++];
      validBase64Char(newChar, false);
      val += charToInt.get(newChar) << 12;
      // Third letter.
      newChar = charInfo[i++];
      validBase64Char(newChar, true);
      if (newChar == PADDING) {
        // Some verifications.
        if (i != charInfo.length || charInfo[i++] != PADDING) {
          throw new IllegalArgumentException("Padding Invalid.");
        }
        numBytes = 1;
      } else {
        val += charToInt.get(newChar) << 6;
        // Forth letter.
        newChar = charInfo[i++];
        validBase64Char(newChar, true);
        if (newChar == PADDING) {
          // Some verifications.
          if (i != charInfo.length) {
            throw new IllegalArgumentException("Found invalid padding.");
          }
          numBytes = 2;
        } else {
          val += charToInt.get(newChar);
        }
      }
      // Now translate the number back to bytes.
      int modB = 1 << 16;
      while (numBytes-- > 0) {
        bStream.write(val / modB);
        val %= modB;
        modB >>= 8;
      }
    }
    return bStream.toByteArray();
  }
  
  //Check we got a valit char in the format.
  private static void validBase64Char(char c, boolean shouldPad) {
    if (PADDING == c) {
      if (shouldPad) {
        return;
      }
      throw new IllegalArgumentException("Padding is not correct");
    }
    if (!(charToInt.containsKey(c))) {
      throw new IllegalArgumentException("unexpected char: >" + c + "<");
    }
  }

  //Solution for negative values
  private static int negativeFixer(int val) {
    if (val >= 0) 
    {  return val; }
    return val + intToChar.length;
  }
  }
