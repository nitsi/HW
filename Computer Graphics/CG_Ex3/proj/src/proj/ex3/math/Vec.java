/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.math;

import java.awt.Color;
import java.util.Scanner;

/**
 * 3D vector class that contains three doubles. Could be used to represent
 * Vectors but also Points and Colors.
 * 
 */
public class Vec {

	/**
	 * Vector data. Allowed to be accessed publicly for performance reasons
	 */
	public double x, y, z;

	
	public Vec(String string) {
		if (string.equals("")) {
			x = 0;
			y = 0;
			z = 0;
		} else {
			Scanner s = new Scanner(string);
			x = s.nextDouble();
			y = s.nextDouble();
			z = s.nextDouble();
			s.close();
		}
	}

	public Color toColor() {
		float r = (float) (x>1?1:x);
		float g = (float) (y>1?1:y);
		float b = (float) (z>1?1:z);
		return new Color(r,g,b);
	}
	
	/**
	 * Initialize vector to (0,0,0)
	 */
	public Vec() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
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
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Initialize vector values to given vector (copy by value)
	 * 
	 * @param v
	 *            Vector
	 */
	public Vec(Vec v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vec(Point3D p1,Point3D p2) {
		x = p2.x-p1.x;
		y = p2.y-p1.y;
		z = p2.z-p1.z;
	}

	/**
	 * Calculates the reflection of the vector in relation a given surface
	 * normal. The vector points at the surface and the result points away.
	 * 
	 * @return The reflected vector
	 */
	public Vec reflect(Vec normal) {
//		double g = normal.dotProd(this);
//		Vec r = clone();
//		r.add(Vec.scale(-2 * g, normal));
//		return r;
		double scalar = this.dotProd(normal);
		return new Vec(sub(this, new Vec(2 * scalar * normal.x, 2 * scalar
				* normal.y, 2 * scalar * normal.z)));
	}

	/**
	 * Adds a to vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void add(Vec a) {
		x += a.x;
		y += a.y;
		z += a.z;
	}

	/**
	 * Subtracts from vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void sub(Vec a) {
		x -= a.x;
		y -= a.y;
		z -= a.z;
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
		x += s * a.x;
		y += s * a.y;
		z += s * a.z;
	}

	/**
	 * Muliplies vector with scalar. v := s*v
	 * 
	 * @param s
	 *            Scalar
	 */
	public void scale(double s) {
		this.x *= s;
		this.y *= s;
		this.z *= s;
	}

	/**
	 * Pairwise multiplies with anther vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void scale(Vec a) {
		this.x *= a.x;
		this.y *= a.y;
		this.z *= a.z;
	}

	/**
	 * Inverses vector
	 * 
	 * @return Vector
	 */
	public void negate() {
//		x = -x;
//		y = -y;
//		z = -z;
		scale(-1);
	}

	/**
	 * Computes the vector's magnitude
	 * 
	 * @return Scalar
	 */
	public double length() {
		return (double) Math.sqrt(lengthSquared());
	}

	/**
	 * Computes the vector's magnitude squared. Used for performance gain.
	 * 
	 * @return Scalar
	 */
	public double lengthSquared() {
		return (double) (x * x) + (y * y) + (z * z);
	}

	/**
	 * Computes the dot product between two vectors
	 * 
	 * @param a
	 *            Vector
	 * @return Scalar
	 */
	public double dotProd(Vec a) {
		return (double) ((this.x * a.x) + (this.y * a.y) + (this.z * a.z));
	}

	/**
	 * Normalizes the vector to have length 1. 
	 * Throws exception if magnitude is zero.
	 * 
	 * @throws ArithmeticException
	 */
	public void normalize() throws ArithmeticException {
		double len = Math.sqrt(x * x + y * y + z * z);
		if (len == 0.0)
			throw new ArithmeticException("Norm is zero");
		x /= len;
		y /= len;
		z /= len;
	}

	/**
	 * Compares to a given vector
	 * 
	 * @param a
	 *            Vector
	 * @return True if have same values, false otherwise
	 */
	public boolean equals(Vec a) {
		return ((a.x == x) && (a.y == y) && (a.z == z));
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
//		double vDot = this.dotProd(v1) / (this.length() * v1.length());
//		if (vDot < -1.0)
//			vDot = -1.0;
//		if (vDot > 1.0)
//			vDot = 1.0;
//		return ((double) (Math.acos(vDot)));
		
		return Math.acos((dotProd(this, v1) / (length() * v1.length())));
	}

	/**
	 * Computes the Euclidean distance between two points
	 * 
	 * @param a
	 *            Point1
	 * @param b
	 *            Point2
	 * @return Scalar
	 */
	static public double distance(Vec a, Vec b) {
//		return Math
//				.sqrt(a.x * a.x - 2 * a.x * b.x + b.x * b.x + a.y * a.y - 2
//						* a.y * b.y + b.y * b.y + a.z * a.z - 2 * a.z * b.z
//						+ b.z * b.z);
		return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2)
				+ Math.pow((a.z - b.z), 2));
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
		return new Vec(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y
				- a.y * b.x);
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
		return new Vec(a.x + b.x, a.y + b.y, a.z + b.z);
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
		return new Vec(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	public static Vec sub(Vec a, Point3D b) {
		return new Vec(a.x - b.x, a.y - b.y, a.z - b.z);
	}	
	
	public static Vec sub(Point3D a, Point3D b) {
		return new Vec(a.x - b.x, a.y - b.y, a.z - b.z);
	}	

	/**
	 * Inverses vector's direction
	 * 
	 * @param a
	 *            Vector
	 * @return -1*a
	 */
	public static Vec negate(Vec a) {
		//return new Vec(-a.x, -a.y, -a.z);
		return scale(-1, a);
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
		return new Vec(a.x * s, a.y * s, a.z * s);
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
		return new Vec(a.x * b.x, a.y * b.y, a.z * b.z);
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
		return ((a.x == b.x) && (a.y == b.y) && (a.z == b.z));
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
		//return a.x * b.x + a.y * b.y + a.z * b.z;
		Vec ans = scale(a, b);
		return (double) (ans.x + ans.y + ans.z);
	}

	/**
	 * Returns a string that contains the values of this vector. The form is
	 * (x,y,z).
	 * 
	 * @return the String representation
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	@Override
	public Vec clone() {
		return new Vec(this);
	}
}
