package ex3.render.raytrace;

import java.util.Map;

/**
 * Camera class represents the camera view in the scene
 * 
 */
public class Camera implements IInitable {

	private Point3D eye;
	private Vec towards;
	private Vec up;
	private Vec right;
	private double screenDist;
	private double screenWidth;
	private Point3D centerCoordinate3D;

	public Camera() {
	}

	public void init(Map<String, String> attributes) throws IllegalArgumentException {

		if (!attributes.containsKey("eye")) {
			throw new IllegalArgumentException("pleas enter aye coordinate");
		}
		eye = new Point3D(attributes.get("eye"));

		if (!attributes.containsKey("look-at") && !attributes.containsKey("direction")) {
			throw new IllegalArgumentException("missing direction or look-at attributes");
		}
		if (attributes.containsKey("direction")) {
			towards = new Vec(attributes.get("direction"));
		} else {
			towards = Point3D.vecBetweenTowPoints(new Point3D(attributes.get("loot-at")), eye);
		}
		if (!attributes.containsKey("up-direction")) {
			throw new IllegalArgumentException("missing up-direction");
		}
		Vec tempUp = new Vec(attributes.get("up-direction"));
		right = Vec.crossProd(towards, tempUp);
		if (!(Vec.dotProd(towards, tempUp) == 0)) {
			up = Vec.crossProd(towards, right);
		} else {
			up = tempUp;
		}
		if (!attributes.containsKey("screen-dist")) {
			throw new IllegalArgumentException("missing screen-dist");
		} else {
			screenDist = Double.parseDouble(attributes.get("screen-dist"));
		}

		if (!attributes.containsKey("screen-width")) {
			screenWidth = 2;
		} else {
			screenWidth = Double.parseDouble(attributes.get("screen-width"));
		}
		centerCoordinate3D = Point3D.add(Vec.scale(screenDist, towards), eye);
		if (Vec.linearDependant(towards, up)) {
			throw new IllegalArgumentException("direction and up-direction are linearDependant");
		}
		up.normalize();
		up.negate();
		right.normalize();
		towards.normalize();
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
		double pixSize = screenWidth / width;
		Point3D centerCoordinate2D = new Point3D(Math.floor(width / 2), Math.floor(height / 2), 0);
		// the center of the view plane
		Vec rightProgress = Vec.scale(x - centerCoordinate2D.x, Vec.scale(pixSize, right));
		Vec upProgress = Vec.scale(y - centerCoordinate2D.y, Vec.scale(pixSize, up));
		Point3D destinationPixelIn3D = Point3D.add(upProgress, Point3D.add(rightProgress, centerCoordinate3D));
		Vec vectorBetweenDestinationPixelAndAye = Point3D.vecBetweenTowPoints(destinationPixelIn3D, eye);
		return new Ray(eye, vectorBetweenDestinationPixelAndAye);
	}

	public Point3D getEye() {
		return eye;
	}
}
