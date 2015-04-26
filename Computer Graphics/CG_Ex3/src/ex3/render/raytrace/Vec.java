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
	 * Initialize vector to given coordinates
	 * 
	 * @param x
	 *            Scalar
	 * @param y
	 *            Scalar
	 * @param z
	 *            Scalar
	 */
	public Vec(double x, double y, double z) {
		this.g_x = x;
		this.g_y = y;
		this.g_z = z;
	}

	/**
	 * Initialize vector values to given vector (copy by value)
	 * 
	 * @param v
	 *            Vector
	 */
	public Vec(Vec v) {
		g_x = v.g_x;
		g_y = v.g_y;
		g_z = v.g_z;
	}

	/**
	 * Calculates the reflection of the vector in relation to a given surface
	 * normal. The vector points at the surface and the result points away.
	 * 
	 * @return The reflected vector
	 */
	public Vec reflect(Vec normal) {

		return sub(this, scale(2, (scale((dotProd(normal, this)), normal))));
	}

	/**
	 * Adds a to vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void add(Vec a) {

		this.g_x = a.g_x + g_x;
		this.g_y = a.g_y + g_y;
		this.g_z = a.g_z + g_z;
	}

	/**
	 * Subtracts from vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void sub(Vec a) {

		a.g_x = a.g_x - g_x;
		a.g_y = a.g_y - g_y;
		a.g_z = a.g_z - g_z;
	}

	/**
	 * Multiplies & Accumulates vector with given vector and a. v := v + s*a
	 * 
	 * @param s
	 *            Scalar
	 * @param a
	 *            Vector
	 */
	public void mac(double s, Vec a) {

		g_x = g_x + s * a.g_x;
		g_y = g_y + s * a.g_y;
		g_z = g_z + s * a.g_z;
	}

	/**
	 * Multiplies vector with scalar. v := s*v
	 * 
	 * @param s
	 *            Scalar
	 */
	public void scale(double s) {

		g_x = s * g_x;
		g_y = s * g_y;
		g_z = s * g_z;
	}

	/**
	 * Pairwise multiplies with another vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void scale(Vec a) {

		g_x = g_x * a.g_x;
		g_y = g_y * a.g_y;
		g_z = g_z * a.g_z;
	}

	/**
	 * Inverses vector
	 * 
	 * @return Vector
	 */
	public void negate() {
		g_x = -g_x;
		g_y = -g_y;
		g_z = -g_z;
	}

	/**
	 * Computes the vector's magnitude
	 * 
	 * @return Scalar
	 */
	public double length() {

		return Math.sqrt(g_x * g_x + g_y * g_y + g_z * g_z);
	}

	/**
	 * Computes the vector's magnitude squared. Used for performance gain.
	 * 
	 * @return Scalar
	 */
	public double lengthSquared() {

		return (g_x * g_x + g_y * g_y + g_z * g_z);
	}

	/**
	 * Computes the dot product between two vectors
	 * 
	 * @param a
	 *            Vector
	 * @return Scalar
	 */
	public double dotProd(Vec a) {

		return (a.g_x * g_x + a.g_y * g_y + a.g_z * g_z);
	}

	/**
	 * Normalizes the vector to have length 1. Throws exception if magnitude is
	 * zero.
	 * 
	 * @throws ArithmeticException
	 */
	public void normalize() throws ArithmeticException {

		double vecLenght = this.length();
		if (vecLenght == 0)
			throw new ArithmeticException();
		else {
			g_x = g_x / vecLenght;
			g_y = g_y / vecLenght;
			g_z = g_z / vecLenght;
		}
	}

	public static boolean isLinearDependant(Vec v, Vec u) {
		if ((v.g_x / u.g_x) == (v.g_y / u.g_y) && (v.g_y / u.g_y) == (v.g_z / u.g_z)) {
			return true;
		}
		return false;
	}

	/**
	 * Compares to a given vector
	 * 
	 * @param a
	 *            Vector
	 * @return True if have same values, false otherwise
	 */
	public boolean equals(Vec a) {
		return ((a.g_x == g_x) && (a.g_y == g_y) && (a.g_z == g_z));
	}

	/**
	 * Returns the angle in radians between this vector and the vector
	 * parameter; the return value is constrained to the range [0,PI].
	 * 
	 * @param v1
	 *            the other vector
	 * @return the angle in radians in the range [0,PI]
	 */
	public final double angle(Vec v1) {

		double lenghtOfCurrentVec = this.length();
		double lenghtOfGivenVec = v1.length();
		double angleInRadians = Math.abs(Math.acos((this.dotProd(v1) / Math.sqrt(lenghtOfCurrentVec * lenghtOfGivenVec))));
		return angleInRadians;
	}

	/**
	 * Computes the cross product between two vectors using the right hand rule
	 * 
	 * @param a
	 *            Vector1
	 * @param b
	 *            Vector2
	 * @return Vector1 x Vector2
	 */
	public static Vec crossProd(Vec a, Vec b) {

		return new Vec(a.g_y * b.g_z - a.g_z * b.g_y, a.g_z * b.g_x - a.g_x * b.g_z, a.g_x * b.g_y - a.g_y * b.g_x);
	}

	/**
	 * Adds vectors a and b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a+b
	 */
	public static Vec add(Vec a, Vec b) {
		// TODO:
		return new Vec(a.g_x + b.g_x, a.g_y + b.g_y, a.g_z + b.g_z);
	}

	/**
	 * Subtracts vector b from a
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a-b
	 */
	public static Vec sub(Vec a, Vec b) {
		// TODO:
		return new Vec(a.g_x - b.g_x, a.g_y - b.g_y, a.g_z - b.g_z);
	}

	/**
	 * Inverses vector's direction
	 * 
	 * @param a
	 *            Vector
	 * @return -1*a
	 */
	public static Vec negate(Vec a) {

		return new Vec(-1 * a.g_x, -1 * a.g_y, -1 * a.g_z);
	}

	/**
	 * Scales vector a by scalar s
	 * 
	 * @param s
	 *            Scalar
	 * @param a
	 *            Vector
	 * @return s*a
	 */
	public static Vec scale(double s, Vec a) {

		return new Vec(s * a.g_x, s * a.g_y, s * a.g_z);
	}

	/**
	 * Pair-wise scales vector a by vector b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a.*b
	 */
	public static Vec scale(Vec a, Vec b) {

		return new Vec(b.g_x * a.g_x, b.g_y * a.g_y, b.g_z * a.g_z);
	}

	/**
	 * Compares vector a to vector b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a==b
	 */
	public static boolean equals(Vec a, Vec b) {
		// TODO:
		return (a.g_x == b.g_x) && (a.g_y == b.g_y) && (a.g_z == b.g_z);
	}

	/**
	 * Dot product of a and b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a.b
	 */
	public static double dotProd(Vec a, Vec b) {

		return (b.g_x * a.g_x + b.g_y * a.g_y + b.g_z * a.g_z);
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
