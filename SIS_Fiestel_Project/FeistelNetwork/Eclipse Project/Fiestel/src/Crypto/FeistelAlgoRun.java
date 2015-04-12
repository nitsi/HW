package Crypto;

//This is based on DES, as defined in Wikipedia and the wide web ~
//For any information needed more, feel free to lookp the DES Wiki Databse
public class FeistelAlgoRun {

    public static final byte[] E = {
        32,  1,  2,  3,  4,  5,  4,  5,  6,  7,  8,  9,  8,  9, 10, 11,
        12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21,
                    22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,  1};
   
    public static final byte[] P = {
        16,  7, 20, 21, 29, 12, 28, 17,  1, 15, 23, 26,  5, 18, 31, 10,
         2,  8, 24, 14, 32, 27,  3,  9, 19, 13, 30,  6, 22, 11,  4, 25};
   
    public static final byte[] IP = {
        58, 50, 42, 34, 26, 18, 10,  2, 60, 52, 44, 36, 28, 20, 12,  4,
        62, 54, 46, 38, 30, 22, 14,  6, 64, 56, 48, 40, 32, 24, 16,  8,
        57, 49, 41, 33, 25, 17,  9,  1, 59, 51, 43, 35, 27, 19, 11,  3,
        61, 53, 45, 37, 29, 21, 13,  5, 63, 55, 47, 39, 31, 23, 15,  7};
   
    public static final byte[] IPr = {
        40,  8, 48, 16, 56, 24, 64, 32, 39,  7, 47, 15, 55, 23, 63, 31,
        38,  6, 46, 14, 54, 22, 62, 30, 37,  5, 45, 13, 53, 21, 61, 29,
        36,  4, 44, 12, 52, 20, 60, 28, 35,  3, 43, 11, 51, 19, 59, 27,
        34,  2, 42, 10, 50, 18, 58, 26, 33,  1, 41,  9, 49, 17, 57, 25};
 
   public static final byte[] PC1 = {
      57, 49, 41, 33, 25, 17,  9,  8,  1, 58, 50, 42, 34, 26, 18, 16,
      10,  2, 59, 51, 43, 35, 27, 24, 19, 11,  3, 60, 52, 44, 36, 32,
      63, 55, 47, 39, 31, 23, 15, 40,  7, 62, 54, 46, 38, 30, 22, 48,
      14,  6, 61, 53, 45, 37, 29, 56, 21, 13,  5, 28, 20, 12,  4, 64};
 
   public static final byte[] PC2 = {
      14, 17, 11, 24,  1,  5,  3, 28, 15,  6, 21, 10, 23, 19, 12,  4,
      26,  8, 16,  7, 27, 20, 13,  2, 41, 52, 31, 37, 47, 55, 30, 40,
      51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
 
   public static final byte[][][] S_BOX = {
    {  // S1
      { 14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7 },
      {  0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8 },
      {  4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0 },
      { 15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13 }},
    {  // S2
      { 15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10 },
      {  3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5 },
      {  0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15 },
      { 13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9 }},
    {  // S3
      { 10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8 },
      { 13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1 },
      { 13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7 },
      {  1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12 }},
    {  // S4
      {  7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15 },
      { 13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9 },
      { 10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4 },
      {  3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14 }},
    {  // S5
      {  2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9 },
      { 14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6 },
      {  4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14 },
      { 11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3 }},
    {  // S6
      { 12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11 },
      { 10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8 },
      {  9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6 },
      {  4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13 }},
    {  // S7
      {  4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1 },
      { 13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6 },
      {  1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2 },
      {  6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12 }},
    {  // S8
      { 13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7 },
      {  1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2 },
      {  7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8 },
      {  2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11 }}};


    public static final int[] SHIFTS_ARRAY = {
       1,  1 , 2,  2,  2,  2,  2,  2,  1,  2,  2,  2,  2,  2,  2,  1 };
 
    private static final int NUM_KEYS = SHIFTS_ARRAY.length;
 
	public long decrypt(long cipher, long key) {
        return activate("decrypt", cipher, key);
    }

    public long encrypt(long plain, long key) {
        return activate( "encrypt", plain, key);
    }
 
       
        //Does both encryption and decryption of the Feistel-like algorithm.
    private long activate(String action, long input, long key) {
       
        long roundKey;
        // Produce the keys.
                long[] keys = keysGenerator(key);
 
                //permutating.
            long permutated = permutation(input, IP);
   
        //make 2 part relevant.
        long leftSide = permutated >>> (Byte.SIZE * 4);
        long rightSide = permutated & 0xFFFFFFFFL;
       
        // Basic algorithm as explained in wiki & class.
        for (int i = 0; i < NUM_KEYS; i++) {
                long tempRight = rightSide;
                switch (action){
                case "decrypt":
                        roundKey = keys[NUM_KEYS - i - 1];
                        break;
                case "encrypt":
                        roundKey = keys[i];
                        break;
                default:
                        //Won't happen
                        roundKey = 0;
                        System.out.println("Wrong input");
                        }
 
                        rightSide = leftSide ^ f_Function(rightSide, roundKey);
                        leftSide = tempRight;
                }
               
                // "concatenation"
                long roundOut = leftSide | (rightSide << Long.SIZE / 2);
 
                // use the reversed permutation.
            return permutation(roundOut, IPr);
    }
 
       
        //Creates round keys out of a 64bit key.
    private long[] keysGenerator(long key) {
           
            key = permutation(key, PC1);
            long part1 = key >>> Byte.SIZE * 4;
    long part2 = key & 0xFFFFFFFFL;
    long[] roundKeys = new long[NUM_KEYS];
   
    for (int i = 0; i < roundKeys.length; ++i) {
            // Shift left.
            int shifts = SHIFTS_ARRAY[i];
            part1 = (part1 >>> (Integer.SIZE - shifts)) | (part1 << shifts);
            part2 = (part2 >>> (Integer.SIZE - shifts)) | (part2 << shifts) ;
            //Assembles the 2 parts together.
            long almostRoundKey = (part1 << Long.SIZE / 2) | part2;
            //Contains the round key
                roundKeys[i] = permutation(almostRoundKey, PC2);
        }
    return roundKeys;
        }
 
 
        //Expands a 32-bit value into 48-bits.
    long expand(long value) {
            long expVal = 0;
            //Takes each bit from value onto expVal.
                for (int i = 0; i < E.length; i++) {
                        int setPos = Long.SIZE - E.length + i;  
                        int getPos = Long.SIZE - Byte.SIZE * 4 + E[i] - 1;
                        expVal = setBit(expVal, setPos, getBit(value, getPos));
                }
                return expVal;
        }
 
       
        //Returns a byte (six bits) out of a long number starting at pos.
    byte getSixBits(long number, int pos) {
            return (byte) ((number >>> (Long.SIZE - pos - 6)) & 0b111111);
    }
   
   
    //S-box transformation on given value.
        long sBoxTrans(long value) {
                long outVal = 0;
                for (int i = 0; i < 6; i++) {
                        byte b = getSixBits(value, i * 6);
 
                        //Column is the middle 4 bits,
                    //Row is the leftmost bit with the rightmost one
                        int column = (b >>> 1) & 0xf;
                        int row = 2 * (b >>> 5) + (b & 1);
 
                        // Substitution transformation
                    byte moded = getByte(outVal, i);
                    moded |= S_BOX[i][row][column];
                   
                    // S_BOX is actually 4bit
                    // Change while pointer is even.
                    if (0 == i % 2) {
                            moded <<= 4;
                    }
                   
                    // Modify the relevant output byte
                        outVal = setByte(outVal, moded, i);
                }
                return outVal;
        }
 
       
        //Bitwise permutation on given value.
        long permutation(long value, byte[] rPermut) {
                long result = 0;
               
                for (int i = 0; i < rPermut.length; i++) {
                        int setPos = Long.SIZE - rPermut.length + i;
                        int getPos = Long.SIZE - rPermut.length + rPermut[i] - 1;
                        result = setBit(result, setPos, getBit(value, getPos));
                }
                return result;
        }
       
 
       
          //Gets byte from value from given position,
      public static byte getByte(long value, int pos) {
        return (byte) (value >>> (Long.SIZE - ((pos + 1) * Byte.SIZE)));
      }
     
     
      //Sets byte data in given number to requested input
          public static long setByte(long value, byte input, int pos) {
            value &= ~(0xFL << Long.SIZE - ((pos + 1) * Byte.SIZE));
            long inputLong = (long) input;
            return (value | (inputLong << Long.SIZE - ((pos + 1) * Byte.SIZE)));
          }
 
         
          //Gets bit in given position from given long value.
          public static boolean getBit(long value, int pos) {
            return ((value >>> (Long.SIZE - pos - 1)) & 1L) != 0;
          }
 
 
          //Sets the requested bit in the given long value.
      public static long setBit(long value, int pos, boolean affected) {
            long mask;
            // Create a mask covering only the single affected bit.
            if (affected){
                    mask = 1L << (Long.SIZE - 1 - pos);
            }else {
                    mask = 0L << (Long.SIZE - 1 - pos);
            }
        // Keep all unrelated bits, and mask the last one.
        return  (mask | (value & ~mask));
      }
     
     
            //Maps both 32-bit and 48-bit values to a 32-bit processed value.
            private long f_Function (long val, long key) {
                   
                    // 32bit to 48bit
                        long expVal = expand(val);
                        long valXored = expVal ^ key;
 
                        //S-box's transformation on xored value
                        long narrowed = sBoxTrans(valXored);
 
                        // Last permutation and return ciphered data
                    return permutation(narrowed, P);
            }
               
}