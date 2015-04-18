/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/


package proj.ex3.render.raytrace;

import proj.ex3.math.*;

/**
 * Contains information regarding a ray-surface intersection.
 * 
 */
public class Hit {

	public Point3D intersection;
	public Surface surface;
	public double distance;

	/**
	 * constructor
	 * @param intersection
	 * @param surface
	 * @param distance
	 */
	public Hit(Point3D intersection, Surface surface, double distance) {
		this.intersection = intersection;
		this.surface = surface;
		this.distance = distance;
	}
}