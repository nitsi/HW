/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Map;
/**
 * OmniLight extends Light, represents Omnilight in scene
 *
 */
public class OmniLight extends Light {

	private static final String MISSING = "missing: ";
	private static final String NO_LIGHT_POSITION = "pos attr!";

	private Point3D g_pointPosition;
	private double g_kConstant;
	private double g_kLinear;
	private double g_kQuadratic;

	public OmniLight() {
	}

	/**
	 * OmniLight(Map<String, String> attributes)
	 * 
	 * @param attributes
	 */
	public OmniLight(Map<String, String> attributes) {
		init(attributes);
	}

	@Override
	public void init(Map<String, String> attributes) {

		if (verifyInput(attributes)) {
			// Instantiate the XML input into variables
			g_color = attributes.containsKey("color") ? new Vec(attributes.get("color")) : new Vec(1, 1, 1);
			g_pointPosition = new Point3D(attributes.get("pos"));
			g_kConstant = attributes.containsKey("kc") ? Double.parseDouble(attributes.get("kc")) : 1;
			g_kLinear = attributes.containsKey("kl") ? Double.parseDouble(attributes.get("kl")) : 0;
			g_kQuadratic = attributes.containsKey("kq") ? Double.parseDouble(attributes.get("kq")) : 0;
		}
	}

	private boolean verifyInput(Map<String, String> attributes) {
		if (!attributes.containsKey("pos")) {
			throw new IllegalArgumentException(MISSING + NO_LIGHT_POSITION);
		}
		return true;
	}

	public Vec getLightIntensity(Point3D point) {

		double i_distanceFromPointPosition = point.distance(g_pointPosition);
		// return the vector post scale
		return Vec.scale(1 / (g_kConstant + g_kLinear * i_distanceFromPointPosition + g_kQuadratic * i_distanceFromPointPosition
				* i_distanceFromPointPosition), g_color);

	}

	public Point3D getPosition() {
		return g_pointPosition;
	}
}
