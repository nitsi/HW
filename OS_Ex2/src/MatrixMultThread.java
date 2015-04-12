/*  Operation Systems - Ex2
 *  Name:	Matan Gidnian
 *  ID:		200846905
 */

public class MatrixMultThread implements Runnable 
{
	private final static int M_SIZE = 1024;
	private final static int T_COUNT = 2;
	private Thread runThread;
	
	private float[][] a, b, doneMatrix;
	private int rowStart, rowEnd;
	
	public MatrixMultThread(float[][] first, float[][] second, float[][] answer, int startRow, int endRow) {
		this.a = first;
		this.b = second;
		this.doneMatrix = answer;
		this.rowStart = startRow;
		this.rowEnd = endRow;
	}
	
	public static void main(String[] args)
	{
		float[][] firstMatrix = new float[M_SIZE][M_SIZE];
		float[][] secondMatrix = new float[M_SIZE][M_SIZE];
		
		matrixRandFiller(firstMatrix);
		matrixRandFiller(secondMatrix);
		
		long startStamp = System.currentTimeMillis();
		
		mult(firstMatrix, secondMatrix, T_COUNT);
		
		long endStamp = System.currentTimeMillis();
		System.out.println("Total required time: " + (endStamp - startStamp));
	}
	
	@Override
	public void run() 
	{
		for (int i = rowStart; i <= rowEnd; i++) 
		{
			for (int j = 0; j < b[0].length; j++) 
			{
				for (int k = 0; k < a[0].length; k++) 
				{
					doneMatrix[i][j] += (a[i][k] * b[k][j]);
				}
			}
		}
	}
	
	public static float[][] mult(float[][] a, float[][] b, int threadCount)
	{
		int startRow, finishRow;
		
		// Validate matrix size are qualified for our needs
		if (a[0].length != b[0].length || a.length != a[0].length || b.length != b[0].length) 
		{
			throw new RuntimeException("Matrix size does not qualify for this exercise"
					+ System.lineSeparator() + "Please verify all matrix are square and of equal size");
		}
		// Our thread pool to be used in the multiplication
		MatrixMultThread[] threadPool = new MatrixMultThread[threadCount];

		int matrixSize = b.length;
		
		// As recommended, dividing the rows to threads
		int rowsToCover = (matrixSize / threadCount);
		
		float[][] doneMatrix = new float[matrixSize][matrixSize];

		// running filling matrix threads
		for (int i = 0; i < threadCount; i++) 
		{
			// defining the range for thread.
			startRow = i * rowsToCover;
			finishRow = startRow + rowsToCover - 1;
			
			if (matrixSize <= finishRow)
				finishRow = matrixSize - 1;
			
			// Running the thread, filling relevant rows per thread.
			threadPool[i] = new MatrixMultThread(a, b, doneMatrix, startRow, finishRow);
			threadPool[i].start();
		}

		// Wait for all threads to return to pool to finish
		for (int i = 0; i < threadCount; i++) 
		{
			try 
			{
				threadPool[i].runThread.join();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		return doneMatrix;
	}
	
	private void start() 
	{
		if (runThread == null)
		{
			runThread = new Thread(this);
		}
		runThread.start();
	}

	/*
	 * Fill the given matrix with random input.
	 */
	private static void matrixRandFiller(float[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				m[i][j] = (float)(Math.random() * 100);
			}
		}
	}
}
