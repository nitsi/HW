/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Map;

/**
 * Implements Surface, representing a sphere
 * 
 * 
 *
 */
public class Sphere extends Surface {
	private static final String MISSING = "missing: ";
	private static final String NO_RADIUS = "radius attr!";
	private static final String NO_CENTER_POINT = "center point attr!";
	private double g_sphereRadius;
	private Point3D g_sphereCenter;

	public Sphere() {
	}

	/**
	 * Instantiate sphere using inputed attributes
	 * 
	 * @param attributes
	 */
	public Sphere(Map<String, String> attributes) {
		originInit(attributes);
		init(attributes);
	}

	@Override
	public Vec getNormalAtPoint(Point3D point) {
		Vec i_normal;
		i_normal = Point3D.vectorBetweenTwoPoints(g_sphereCenter, point);
		i_normal.normalize();
		return i_normal;
	}

	@Override
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		// test and set input
		if (attributes.containsKey("center")) {
			g_sphereCenter = new Point3D(attributes.get("center"));
		} else {
			throw new IllegalArgumentException(MISSING + NO_CENTER_POINT);
		}

		if (attributes.containsKey("radius")) {
			g_sphereRadius = Double.parseDouble(attributes.get("radius"));
		} else {
			throw new IllegalArgumentException(MISSING + NO_RADIUS);
		}

	}

	public Point3D getCenter() {
		return g_sphereCenter;
	}

	public double getRadius() {
		return g_sphereRadius;
	}
}
