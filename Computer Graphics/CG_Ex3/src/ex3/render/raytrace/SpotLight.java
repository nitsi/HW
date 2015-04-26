/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Map;

public class SpotLight extends Light {

	private static final String MISSING = "missing: ";
	private static final String NO_POS = "pos attr!";
	private static final String NO_DIRECTION_TO_LIGHT = "dir attr!";

	private Vec g_directionVector;
	private Point3D g_positionPoint;
	private double g_kConst;
	private double g_kLinear;
	private double g_kQuadratic;

	public SpotLight() {
	}

	public Point3D getPosition() {
		return this.g_positionPoint;
	}

	/**
	 * Instantiates SpotLight using Light attributes constructor
	 * 
	 * @param attributes
	 */
	public SpotLight(Map<String, String> attributes) {
		init(attributes);
	}

	// Guess what, I lied. We override the constructor
	@Override
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		// killer attributes
		if (!attributes.containsKey("pos")) {
			throw new IllegalArgumentException(MISSING + NO_POS);
		}
		if (!attributes.containsKey("dir")) {
			throw new IllegalArgumentException(MISSING + NO_DIRECTION_TO_LIGHT);
		}

		// if we got here, we populate variables with defaults / data from XML
		g_positionPoint = new Point3D(attributes.get("pos"));

		g_color = attributes.containsKey("color") ? new Vec(attributes.get("color")) : new Vec("1 1 1");

		g_kConst = attributes.containsKey("kc") ? Double.parseDouble(attributes.get("kc")) : 1;

		g_kLinear = attributes.containsKey("kl") ? Double.parseDouble(attributes.get("kl")) : 0;
		g_kQuadratic = attributes.containsKey("kq") ? Double.parseDouble(attributes.get("kq")) : 0;

		g_directionVector = new Vec(attributes.get("dir"));
		g_directionVector.normalize();

	}

	/**
	 * returns the light intensity in a given point
	 */
	public Vec getLightIntensity(Point3D point) {

		Vec i_locationWithRespectToLight = Point3D.vectorBetweenTwoPoints(point, g_positionPoint);
		i_locationWithRespectToLight.normalize();
		// calculate the angle
		double i_tempDotProd = Vec.dotProd(i_locationWithRespectToLight, g_directionVector);
		double i_angelBetweenLightAndDirectionVector = i_tempDotProd < 0 ? 0 : i_tempDotProd;

		double i_distanceFromPoint = point.distance(g_positionPoint);
		// calculate and return IL according to formula
		return Vec.scale(1 / (g_kConst + g_kLinear * i_distanceFromPoint + g_kQuadratic * i_distanceFromPoint * i_distanceFromPoint),
				Vec.scale(i_angelBetweenLightAndDirectionVector, g_color));

	}
}
