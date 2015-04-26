/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Scanner;

/**
 * Represents a point in 3D space
 * 
 *
 */
public class Point3D {

	public double x, y, z;

	/**
	 * constructor for string input
	 * 
	 * @param v
	 */
	public Point3D(String v) {
		Scanner s = new Scanner(v);
		this.x = s.nextDouble();
		this.y = s.nextDouble();
		this.z = s.nextDouble();
		// close scanner
		s.close();
	}

	/**
	 * constructor for explicit input
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * constructor for point input
	 * 
	 * @param point
	 */
	public Point3D(Point3D point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}

	/**
	 * constructor for empty input
	 */
	public Point3D() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * checks if a given vector equals the Point vector
	 * 
	 * @param a
	 * @return
	 */
	public boolean equals(Vec a) {
		return ((this.x == a.g_x) && (this.y == a.g_y) && (this.z == a.g_z));
	}

	/**
	 * 
	 * @param point
	 * @return distance between given point to the current one
	 */
	public double distance(Point3D point) {
		return Math.sqrt(Math.pow((point.x - x), 2) + Math.pow((point.y - y), 2) + Math.pow((point.z - z), 2));
	}

	/**
	 * calculates distance between two given Point3D
	 * 
	 * @param p1
	 * @param p2
	 * @return distance between two given points p1 & p2
	 */
	public static double distance(Point3D p1, Point3D p2) {
		return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2) + Math.pow((p1.z - p2.z), 2));
	}

	/**
	 * calculates the vector between this a a given point
	 * 
	 * @param p
	 * @return vector between a given point and this
	 */
	public Vec vectorBetweenTwoPoints(Point3D p) {
		return new Vec(p.x - this.x, p.y - this.y, p.z - this.z);
	}

	/**
	 * Calculates the vector between two given points
	 * 
	 * @param p1
	 * @param p2
	 * @return vector between two given points
	 */
	public static Vec vectorBetweenTwoPoints(Point3D p1, Point3D p2) {
		return new Vec(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
	}

	/**
	 * Receives a vector and a point
	 * 
	 * @param v
	 * @param p
	 * @return new Point3D with the new values
	 */
	public static Point3D add(Vec v, Point3D p) {
		return new Point3D(v.g_x + p.x, v.g_y + p.y, v.g_z + p.z);
	}

	/**
	 * standard toString
	 */
	public String toString() {
		return ("( " + this.x + " , " + this.y + " , " + this.z + " )");
	}

	/**
	 * clones this Point3D
	 */
	public Point3D clone() {
		return new Point3D(this);
	}
}