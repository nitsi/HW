/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
 */

package ex3.render.raytrace;

import java.util.Map;

/**
 * Camera class represents the camera view in the scene
 * 
 */
public class Camera implements IInitable {
	private static final String MISSING = "missing: ";
	private static final String NO_EYE_CORDS = "eye coordinates!";
	private static final String NO_LOOK_AT = "look-at attr!";
	private static final String NO_DIRECTION = "direction attr!";
	private static final String NO_UP_DIRECTION = "up-direction attr!";
	private static final String NO_SCREEN_DIST = "screen-dist attr!";
	private static final String LINEAR_DEPENDANCY = "Both directions are linear dependant!";

	private Point3D g_eye;

	private Vec g_dirTo;
	private Vec g_upDirection;
	private Vec g_rightDirection;

	private double g_screenDist;
	private double g_screenWidth;
	private Point3D g_centerCoordinate3D;

	// empty constructor
	public Camera() {
	}

	public void init(Map<String, String> attributes) throws IllegalArgumentException {

		// Some necessary input tests
		if (!attributes.containsKey("direction") && !attributes.containsKey("look-at")) {
			throw new IllegalArgumentException(MISSING + NO_LOOK_AT + NO_DIRECTION);
		}

		if (!attributes.containsKey("up-direction")) {
			throw new IllegalArgumentException(MISSING + NO_UP_DIRECTION);
		}

		if (!attributes.containsKey("eye")) {
			throw new IllegalArgumentException(MISSING + NO_EYE_CORDS);
		}

		if (!attributes.containsKey("screen-dist")) {
			throw new IllegalArgumentException(MISSING + NO_SCREEN_DIST);
		}

		// will construct Point3D with null if eye attribute doesn't exist
		g_eye = new Point3D(attributes.get("eye"));

		// already handled null exception for the Double.parse
		g_screenDist = Double.parseDouble(attributes.get("screen-dist"));

		if (attributes.containsKey("direction")) {
			g_dirTo = new Vec(attributes.get("direction"));
		} else {
			// Instantiate new Point3D with null if no "direction" attribute is
			// present
			g_dirTo = Point3D.vectorBetweenTwoPoints(new Point3D(), g_eye);
		}

		Vec i_upDirection = new Vec(attributes.get("up-direction"));

		g_rightDirection = Vec.crossProd(g_dirTo, i_upDirection);
		
		// check for orthogonal vectors
		if (Vec.dotProd(g_dirTo, i_upDirection) == 0) {
			g_upDirection = i_upDirection;
		} else {
			g_upDirection = Vec.crossProd(g_dirTo, g_rightDirection);
		}
		// Instantiate screen width to 2 if attribute doesn't exist
		g_screenWidth = attributes.containsKey("screen-width") ? Double.parseDouble(attributes.get("screen-width")) : 2;
		g_centerCoordinate3D = Point3D.add(Vec.scale(g_screenDist, g_dirTo), g_eye);

		// test vectors linear dependency
		if (Vec.isLinearDependant(g_dirTo, g_upDirection)) {
			throw new IllegalArgumentException(LINEAR_DEPENDANCY);
		}
		// normalize and negate vectors
		g_upDirection.normalize();
		g_upDirection.negate();
		g_rightDirection.normalize();
		g_dirTo.normalize();
	}

	/**
	 * Transforms image's X,Y coordinates to pane's X,Y,Z coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 * @return - piercing ray
	 */

	public Ray generatePixelPiercingRay(double x, double y, double height, double width) {
		double i_pixelSize = g_screenWidth / width;

		// initialize view pane center
		Point3D i_2DcenterCoordinate = new Point3D(Math.floor(width / 2), Math.floor(height / 2), 0);

		Vec i_rightDirectionProgress = Vec.scale(x - i_2DcenterCoordinate.x, Vec.scale(i_pixelSize, g_rightDirection));
		Vec i_upDirectionProgress = Vec.scale(y - i_2DcenterCoordinate.y, Vec.scale(i_pixelSize, g_upDirection));

		Point3D i_destinationPixel3DFormat = Point3D.add(i_upDirectionProgress, Point3D.add(i_rightDirectionProgress, g_centerCoordinate3D));

		Vec i_vectorBetweenDestPixelAndEye = Point3D.vectorBetweenTwoPoints(i_destinationPixel3DFormat, g_eye);
		return new Ray(g_eye, i_vectorBetweenDestPixelAndEye);
	}

	public Point3D getEye() {
		return g_eye;
	}
}