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

	private Point3D eyeView;
	
	private Vec dirTo;
	private Vec up;
	private Vec _rightDirection;
	
	private double _screenDist;
	private double _screenWidth;
	private Point3D centerCoordinate3D;

	public Camera() {
	}

	public void init(Map<String, String> attributes) throws IllegalArgumentException {

		if (!attributes.containsKey("eye")) {
			throw new IllegalArgumentException("pleas enter aye coordinate");
		}
		this.eyeView = new Point3D(attributes.get("eye"));

		if (!attributes.containsKey("look-at") && !attributes.containsKey("direction")) {
			throw new IllegalArgumentException("missing direction or look-at attributes");
		}
		if (attributes.containsKey("direction")) {
			dirTo = new Vec(attributes.get("direction"));
		} else {
			dirTo = Point3D.vecBetweenTowPoints(new Point3D(attributes.get("loot-at")), eyeView);
		}
		if (!attributes.containsKey("up-direction")) {
			throw new IllegalArgumentException("missing up-direction");
		}
		Vec _upDirection = new Vec(attributes.get("up-direction"));
		_rightDirection = Vec.crossProd(dirTo, _upDirection);
		if (!(Vec.dotProd(dirTo, _upDirection) == 0)) {
			up = Vec.crossProd(dirTo, _rightDirection);
		} else {
			up = _upDirection;
		}
		if (!attributes.containsKey("screen-dist")) {
			throw new IllegalArgumentException("missing screen-dist");
		} else {
			_screenDist = Double.parseDouble(attributes.get("screen-dist"));
		}

		if (!attributes.containsKey("screen-width")) {
			_screenWidth = 2;
		} else {
			_screenWidth = Double.parseDouble(attributes.get("screen-width"));
		}
		centerCoordinate3D = Point3D.add(Vec.scale(_screenDist, dirTo), eyeView);
		if (Vec.isLinearDependant(dirTo, up)) {
			throw new IllegalArgumentException("direction and up-direction are linearDependant");
		}
		up.normalize();
		up.negate();
		_rightDirection.normalize();
		dirTo.normalize();
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
		double pixSize = _screenWidth / width;
		Point3D centerCoordinate2D = new Point3D(Math.floor(width / 2), Math.floor(height / 2), 0);
		// the center of the view plane
		Vec rightProgress = Vec.scale(x - centerCoordinate2D.x, Vec.scale(pixSize, _rightDirection));
		Vec upProgress = Vec.scale(y - centerCoordinate2D.y, Vec.scale(pixSize, up));
		Point3D destinationPixelIn3D = Point3D.add(upProgress, Point3D.add(rightProgress, centerCoordinate3D));
		Vec vectorBetweenDestinationPixelAndAye = Point3D.vecBetweenTowPoints(destinationPixelIn3D, eyeView);
		return new Ray(eyeView, vectorBetweenDestinationPixelAndAye);
	}

	public Point3D getEye() {
		return eyeView;
	}
}
