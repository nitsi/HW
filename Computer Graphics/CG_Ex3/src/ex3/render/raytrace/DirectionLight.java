/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Map;

/**
 * Direction Light extends Light, used to calculate directional light in scene
 * 
 * 
 *
 */
public class DirectionLight extends Light {

	private static final String MISSING = "missing: ";
	private static final String NO_DIRECTION = "direction attr!";

	private Vec g_directionVector;

	public DirectionLight() {
	}

	public DirectionLight(Map<String, String> attributes) {
		init(attributes);
	}

	@Override
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		if (verifyInput(attributes)) {
			g_color = attributes.containsKey("color") ? new Vec(attributes.get("color")) : new Vec(1, 1, 1);
			g_directionVector = new Vec(attributes.get("direction"));

		}
	}

	/**
	 * verifies XML input
	 * 
	 * @param attributes
	 * @return - true if all input tests passed
	 */
	private boolean verifyInput(Map<String, String> attributes) {
		if (!attributes.containsKey("direction")) {
			throw new IllegalArgumentException(MISSING + NO_DIRECTION);
		}
		return true;
	}

	public Vec getLightIntensity(Point3D p) {
		return g_color;
	}

	public Vec getDirection() {
		return g_directionVector;
	}

}
