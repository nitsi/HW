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

	private Point3D g_eye;

	private Vec g_dirTo;
	private Vec g_upDirection;
	private Vec g_rightDirection;

	private double g_screenDist;
	private double g_screenWidth;
	private Point3D g_centerCoordinate3D;

	public Camera() {
	}

	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		//if all input tests passed, we continue 
		if (verifyIputs(attributes)) {
			this.g_eye = new Point3D(attributes.get("eye"));

			if (attributes.containsKey("direction")) {
				g_dirTo = new Vec(attributes.get("direction"));
			} else {
				g_dirTo = Point3D.vecBetweenTowPoints(new Point3D(attributes.get("loot-at")), g_eye);
			}

			Vec i_tempUpDirection = new Vec(attributes.get("up-direction"));
			g_rightDirection = Vec.crossProd(g_dirTo, i_tempUpDirection);
			if (!(Vec.dotProd(g_dirTo, i_tempUpDirection) == 0)) {
				g_upDirection = Vec.crossProd(g_dirTo, g_rightDirection);
			} else {
				g_upDirection = i_tempUpDirection;
			}
			if (!attributes.containsKey("screen-width")) {
				g_screenWidth = 2;
			} else {
				g_screenWidth = Double.parseDouble(attributes.get("screen-width"));
			}
		}
		g_centerCoordinate3D = Point3D.add(Vec.scale(g_screenDist, g_dirTo), g_eye);
		if (Vec.isLinearDependant(g_dirTo, g_upDirection)) {
			throw new IllegalArgumentException("direction and up-direction are linearDependant");
		}
		g_screenDist = Double.parseDouble(attributes.get("screen-dist"));
		g_upDirection.normalize();
		g_upDirection.negate();
		g_rightDirection.normalize();
		g_dirTo.normalize();

	}

	private boolean verifyIputs(Map<String, String> attributes) {

		if (!attributes.containsKey("eye")) {
			throw new IllegalArgumentException(MISSING + NO_EYE_CORDS);
		}

		if (!attributes.containsKey("look-at")) {
			throw new IllegalArgumentException(MISSING + NO_LOOK_AT);
		}

		if (!attributes.containsKey("direction")) {
			throw new IllegalArgumentException(MISSING + NO_DIRECTION);
		}

		if (!attributes.containsKey("up-direction")) {
			throw new IllegalArgumentException(MISSING + NO_UP_DIRECTION);
		}

		if (!attributes.containsKey("screen-dist")) {
			throw new IllegalArgumentException(MISSING + NO_SCREEN_DIST);
		}
		return true;
	}

	/**
	 * Transforms image xy coordinates to view pane xyz coordinates. Returns the
	 * ray that goes through it.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */

	public Ray constructRayThroughPixel(double x, double y, double height, double width) {
		double pixSize = g_screenWidth / width;
		Point3D centerCoordinate2D = new Point3D(Math.floor(width / 2), Math.floor(height / 2), 0);
		// the center of the view plane
		Vec rightProgress = Vec.scale(x - centerCoordinate2D.x, Vec.scale(pixSize, g_rightDirection));
		Vec upProgress = Vec.scale(y - centerCoordinate2D.y, Vec.scale(pixSize, g_upDirection));
		Point3D destinationPixelIn3D = Point3D.add(upProgress, Point3D.add(rightProgress, g_centerCoordinate3D));
		Vec vectorBetweenDestinationPixelAndAye = Point3D.vecBetweenTowPoints(destinationPixelIn3D, g_eye);
		return new Ray(g_eye, vectorBetweenDestinationPixelAndAye);
	}

	public Point3D getEye() {
		return g_eye;
	}
}
