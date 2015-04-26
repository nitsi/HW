/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Scanner;

/**
 * 
 * Regular vector 3D class
 */
public class Vec {

	private static final String VECTOR_MAGNITUDE_IS_ZERO = "Vector magnitude is zero!";

	public double g_x, g_y, g_z;

	/**
	 * vector constructor off a given string
	 * 
	 * @param vectorString
	 */
	public Vec(String vectorString) {
		Scanner i_scanner = new Scanner(vectorString);
		g_x = i_scanner.nextDouble();
		g_y = i_scanner.nextDouble();
		g_z = i_scanner.nextDouble();
		i_scanner.close();
	}

	/**
	 * Generate zero vector (0,0,0)
	 */
	public Vec() {
		this.g_x = 0;
		this.g_y = 0;
		this.g_z = 0;
	}

	/**
	 * create vector from given X Y Z
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec(double x, double y, double z) {
		this.g_x = x;
		this.g_y = y;
		this.g_z = z;
	}

	/**
	 * clone given vector to make this a copy of that
	 * 
	 * @param vector
	 */
	public Vec(Vec vector) {
		g_x = vector.g_x;
		g_y = vector.g_y;
		g_z = vector.g_z;
	}

	/**
	 * Calculates a vector's reflection with respect to some surface's normal
	 * 
	 * @param normal
	 * @return reflected vector
	 */
	public Vec reflect(Vec normal) {
		double i_dotProduct = dotProd(normal, this);
		Vec i_scaledNormal = scale(i_dotProduct, normal);
		i_scaledNormal = scale(2, i_scaledNormal);
		return sub(this, i_scaledNormal);
	}

	/**
	 * increase this vector using given data
	 * 
	 * @param vector
	 */
	public void add(Vec vector) {

		this.g_x += vector.g_x;
		this.g_y += vector.g_y;
		this.g_z += vector.g_z;
	}

	/**
	 * subtructs this from a given vector
	 * 
	 * @param vector
	 */
	public void sub(Vec vector) {

		vector.g_x -= g_x;
		vector.g_y -= g_y;
		vector.g_z -= g_z;
	}

	/**
	 * adds the product of a given vector increase by s
	 * 
	 * @param scalar
	 * @param vector
	 */
	public void multiplyAndAdd(double scalar, Vec vector) {

		this.g_x += scalar * vector.g_x;
		this.g_y += scalar * vector.g_y;
		this.g_z += scalar * vector.g_z;
	}

	/**
	 * scales this vector by a scalar S
	 * 
	 * @param s
	 */
	public void scale(double s) {

		g_x *= s;
		g_y *= s;
		g_z *= s;
	}

	/**
	 * scales this vector using given vector's data
	 * 
	 * @param a
	 */
	public void scale(Vec a) {

		this.g_x *= a.g_x;
		this.g_y *= a.g_y;
		this.g_z *= a.g_z;
	}

	/**
	 * negates vector
	 */
	public void negate() {
		this.g_x *= -1;
		this.g_y *= -1;
		this.g_z *= -1;
	}

	/**
	 * calculates magnitude of this vector
	 * 
	 * @return
	 */
	public double magnitude() {

		return Math.sqrt(Math.pow(this.g_x, 2) + Math.pow(this.g_y, 2) + Math.pow(g_z, 2));
	}

	/**
	 * 
	 * @return squared magnitude
	 */
	public double squaredMagnitude() {

		return (Math.pow(this.g_x, 2) + Math.pow(this.g_y, 2) + Math.pow(g_z, 2));
	}

	/**
	 * dot product between this and a given vector
	 * 
	 * @param vector
	 * @return
	 */
	public double dotProduct(Vec vector) {

		return (vector.g_x * this.g_x + vector.g_y * this.g_y + vector.g_z * this.g_z);
	}

	/**
	 * Normalizes the vector to have length 1. Throws exception if magnitude is
	 * zero.
	 * 
	 * @throws ArithmeticException
	 */
	public void normalize() throws ArithmeticException {

		double i_thisVectorMagnitude = this.magnitude();
		if (i_thisVectorMagnitude == 0)
			throw new ArithmeticException(VECTOR_MAGNITUDE_IS_ZERO);
		else {
			g_x /= i_thisVectorMagnitude;
			g_y /= i_thisVectorMagnitude;
			g_z /= i_thisVectorMagnitude;
		}
	}

	/**
	 * Test if two vectors are linear dependent
	 * 
	 * @param vectorA
	 * @param vectorB
	 * @return
	 */
	public static boolean isLinearDependant(Vec vectorA, Vec vectorB) {
		return ((vectorA.g_x / vectorB.g_x) == (vectorA.g_y / vectorB.g_y) && (vectorA.g_y / vectorB.g_y) == (vectorA.g_z / vectorB.g_z));

	}

	/**
	 * Compares to a given vector
	 * 
	 * @param vector
	 *            Vector
	 * @return True if have same values, false otherwise
	 */
	public boolean equals(Vec vector) {
		return ((vector.g_x == g_x) && (vector.g_y == g_y) && (vector.g_z == g_z));
	}

	/**
	 * Returns the angle in radians between this vector and the vector
	 * parameter; the return value is constrained to the range [0,PI].
	 * 
	 * @param vector
	 *            the other vector
	 * @return the angle in radians in the range [0,PI]
	 */
	public final double angle(Vec vector) {

		double lenghtOfCurrentVec = this.magnitude();
		double lenghtOfGivenVec = vector.magnitude();
		return Math.abs(Math.acos((this.dotProduct(vector) / Math.sqrt(lenghtOfCurrentVec * lenghtOfGivenVec))));
	}

	/**
	 * Right hand rule method :)
	 * 
	 * @param vectorA
	 * @param vectorB
	 * @return Vector1 cross product Vector2
	 */
	public static Vec crossProd(Vec vectorA, Vec vectorB) {
		double i_x, i_y, i_z;
		i_x = vectorA.g_y * vectorB.g_z - vectorA.g_z * vectorB.g_y;
		i_y = vectorA.g_z * vectorB.g_x - vectorA.g_x * vectorB.g_z;
		i_z = vectorA.g_x * vectorB.g_y - vectorA.g_y * vectorB.g_x;
		return new Vec(i_x, i_y, i_z);
	}

	/**
	 * Sums 2 given vectors
	 * 
	 * @param vectorA
	 *            Vector
	 * @param vectorB
	 *            Vector
	 * @return a plus b
	 */
	public static Vec add(Vec vectorA, Vec vectorB) {
		return new Vec(vectorA.g_x + vectorB.g_x, vectorA.g_y + vectorB.g_y, vectorA.g_z + vectorB.g_z);
	}

	/**
	 * Subtracts two given vectors (first minus second)
	 * 
	 * @param vectorA
	 *            Vector
	 * @param vectorB
	 *            Vector
	 * @return a minus b
	 */
	public static Vec sub(Vec vectorA, Vec vectorB) {
		return new Vec(vectorA.g_x - vectorB.g_x, vectorA.g_y - vectorB.g_y, vectorA.g_z - vectorB.g_z);
	}

	/**
	 * Negates vector's direction
	 * 
	 * @param vector
	 *            Vector
	 * @return -1*a
	 */
	public static Vec negate(Vec vector) {
		return new Vec(vector.g_x * -1, vector.g_y * -1, vector.g_z * -1);
	}

	/**
	 * Scales given vector by given scalar
	 * 
	 * @param scalar
	 *            Scalar
	 * @param vector
	 *            Vector
	 * @return s*a
	 */
	public static Vec scale(double scalar, Vec vector) {

		return new Vec(scalar * vector.g_x, scalar * vector.g_y, scalar * vector.g_z);
	}

	/**
	 * scales a vector by another one
	 * 
	 * @param a
	 * @param b
	 * @return the new vecotr post scaling
	 */
	public static Vec scale(Vec a, Vec b) {

		return new Vec(a.g_x * b.g_x, a.g_y * b.g_y, a.g_z * b.g_z);
	}

	/**
	 * Compares two given vectors
	 * 
	 * @param vectorA
	 *            Vector
	 * @param vectorB
	 *            Vector
	 * @return a==b
	 */
	public static boolean equals(Vec vectorA, Vec vectorB) {
		return (vectorA.g_x == vectorB.g_x) && (vectorA.g_y == vectorB.g_y) && (vectorA.g_z == vectorB.g_z);
	}

	/**
	 * Dot product of two given vectors
	 * 
	 * @param vectorA
	 *            Vector
	 * @param vectorB
	 *            Vector
	 * @return vectorA dot product vectorB
	 */
	public static double dotProd(Vec vectorA, Vec vectorB) {

		return (vectorB.g_x * vectorA.g_x + vectorB.g_y * vectorA.g_y + vectorB.g_z * vectorA.g_z);
	}

	/**
	 * Returns a string that contains the values of this vector. The form is
	 * (x,y,z).
	 * 
	 * @return the vector as String (X , Y , Z)
	 */
	public String toString() {
		return "(" + this.g_x + " , " + this.g_y + " , " + this.g_z + ")";
	}

	@Override
	public Vec clone() {
		return new Vec(this);
	}
}
