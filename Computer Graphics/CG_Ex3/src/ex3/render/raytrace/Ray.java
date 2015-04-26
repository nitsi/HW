/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;
/**
 * Represents a Ray with point (Point3D) and direction (Vec)
 *
 */
public class Ray {

	public Point3D g_rayPoint;
	public Vec g_rayDirection;

	/**
	 * 
	 * @param rayPoint
	 * @param rayDirection
	 */
	public Ray(Point3D rayPoint, Vec rayDirection) {
		this.g_rayPoint = rayPoint;
		this.g_rayDirection = rayDirection;
		rayDirection.normalize();
	}

}
