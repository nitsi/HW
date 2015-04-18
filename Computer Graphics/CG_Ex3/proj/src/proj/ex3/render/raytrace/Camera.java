/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;

import java.util.Map;

import proj.ex3.math.Point3D;
import proj.ex3.math.Ray;
import proj.ex3.math.Vec;

/**
 * Represents the scene's camera.
 * 
 */
public class Camera implements IInitable {
	
	protected Point3D eye = null;
	protected Vec direction = null;
	protected Point3D lookAt = null;
	protected Vec upDirection = null;
	protected double screenDist;
	protected double screenWidth;
	
	//protected Vec realUpDirection;
	protected Vec rightDirection;
	protected double screenHeight;
	
	public Camera() {
		screenWidth = 2;
		screenDist = 0;
	}
	
	/**
	 * sends a ray through a wanted pixel
	 * @param newX
	 * @param newY
	 * @param canvasWidth
	 * @param canvasHeight
	 * @return
	 */


	@Override
	public void init(Map<String, String> attributes) {
		
		// Attributes is the XML phrased.
		if (attributes.containsKey("eye") &&
				!attributes.get("eye").isEmpty()) {
			this.eye = new Point3D(attributes.get("eye"));
		}
		if (attributes.containsKey("direction") &&
				!attributes.get("direction").isEmpty()) {
			this.direction = new Vec(attributes.get("direction"));
			this.direction.normalize();
		}
		if (attributes.containsKey("look-at") &&
				!attributes.get("look-at").isEmpty()) {
			this.lookAt = new Point3D(attributes.get("look-at"));
			this.direction = new Vec(eye, lookAt);
			this.direction.normalize();
		}
		if (attributes.containsKey("up-direction") &&
				!attributes.get("up-direction").isEmpty()) {
			this.upDirection = new Vec(attributes.get("up-direction"));
			this.upDirection.normalize();
		}
		if (attributes.containsKey("screen-dist") &&
				!attributes.get("screen-dist").isEmpty()) {
			this.screenDist = Double.valueOf(attributes.get("screen-dist"));
		}
		if (attributes.containsKey("screen-width") &&
				!attributes.get("screen-width").isEmpty()) {
			this.screenWidth = Double.valueOf(attributes.get("screen-width"));
		}
		this.rightDirection = Vec.crossProd(direction, upDirection);
		this.rightDirection.normalize();
		
		// up direction and direction are co-linear
		Vec directionToTest = direction.clone();
		directionToTest.normalize();
		Vec upDirectionToTest = upDirection.clone();
		upDirectionToTest.normalize();
		if (Point3D.isColinear(directionToTest, upDirectionToTest)) {
			throw new IllegalArgumentException("Lines Are Co-Linear");
		}
		
		// If the direction and up direction is not orthogonal,
		// make them orthogonal.
		if (Vec.dotProd(direction, upDirection) != 0) {			
			this.upDirection = Vec.crossProd(rightDirection, direction);
			this.upDirection.normalize();			
		}
	}
	
	/**
	 * Transforms image xy coordinates to view pane xyz coordinates. Returns the
	 * ray that goes through it.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Ray constructRayThroughPixel(double newX,
			double newY, int canvasWidth, int canvasHeight) {
		
		if (direction == null && lookAt == null) {
			System.out.println("insufficiant data");
		}
		if (direction == null) {
			direction = new Vec(eye, lookAt);
		}
		
		double width = screenWidth / canvasWidth;	
		double nWidth = (newX - (canvasWidth / 2)) * width;
		double nHeight = (newY - (canvasHeight / 2)) * width;

		Vec P = new Vec(direction.x, direction.y, direction.z);
		P = Vec.add(eye.ToVec(), Vec.scale(screenDist, direction));

		P.mac(nHeight, upDirection);
		P.mac(nWidth, rightDirection);
		P.sub(eye.ToVec());		
		P.normalize();
		
		return new Ray(eye, P);
	}
}
