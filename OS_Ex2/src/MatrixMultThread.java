/*  Operation Systems - Ex2
 *  Name:	Matan Gidnian
 *  ID:		200846905
 */

public class MatrixMultThread implements Runnable 
{
	private final static int M_SIZE = 1024;
	private final static int T_COUNT = 2;
	private float[][] a, b, c;
	private int rowStart, rowEnd;
	
	public MatrixMultThread(float[][] a, float[][] b, float[][] c, int rowStart, int rawEnd) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
	}
	
	public static void main(String[] args)
	{
		float[][] a = new float[M_SIZE][M_SIZE];
		float[][] b = new float[M_SIZE][M_SIZE];
		
		matrixRandFiller(a);
		matrixRandFiller(b);
		
		long timeStamp = System.currentTimeMillis();
		
		mult(a,b,T_COUNT);
		
		timeStamp = System.currentTimeMillis() - timeStamp;
		System.out.println("Total required time: " + timeStamp);
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
	}
	
	public static float[][] mult(float[][] a, float[][] b, int threadCount)
	{
		return b;
		
	}
	
	private static void matrixRandFiller(float[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				m[i][j] = (float)(Math.random() * 100);
			}
		}
	}

	
}
