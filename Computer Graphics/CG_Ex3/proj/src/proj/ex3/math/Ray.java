/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.math;

/**
 * This will work only if you will add the Vec Class from Ex2 - Make sure its working properly.
 * You will also need to implement your own Point3D class.
 */
public class Ray {

	// point of origin
	public Point3D p;
	// ray direction
	public Vec v;
	
	/**
	 * constructs a new ray
	 * @param p - point of origin
	 * @param v - ray direction
	 */
	public Ray(Point3D p, Vec v) {
		this.p = p;
		this.v = v;
	}
}
