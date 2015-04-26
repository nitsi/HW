/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Map;

/**
 * Extends Sphere. used to instansiate discs
 * 
 *
 */
public class Disc extends Sphere {

	private static final String MISSING = "missing: ";
	private static final String NO_NORMAL = "normal attr!";

	private Vec g_normal;

	// empty constructor
	public Disc() {
	}

	public Vec getNormal() {
		return g_normal;
	}

	/**
	 * 
	 * @param attributes
	 *            - from XML
	 */
	public Disc(Map<String, String> attributes) {
		super(attributes);
		generateNormal(attributes);
	}

	/**
	 * generate vector from the normal attribute
	 * 
	 * @param attributes
	 */
	public void generateNormal(Map<String, String> attributes) {
		if (verifyInput(attributes)) {
			g_normal = new Vec(attributes.get("normal"));
		}

	}

	/**
	 * verifies XML input
	 * 
	 * @param attributes
	 * @return true if all input tests passed
	 */
	private boolean verifyInput(Map<String, String> attributes) {
		if (!attributes.containsKey("normal")) {
			throw new IllegalArgumentException(MISSING + NO_NORMAL);
		}
		return true;
	}

	@Override
	public Vec getNormalAtPoint(Point3D p) {
		return g_normal;
	}

}
