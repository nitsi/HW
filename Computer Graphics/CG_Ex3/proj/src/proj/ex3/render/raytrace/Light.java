package proj.ex3.render.raytrace;

import proj.ex3.math.Point3D;
import proj.ex3.math.Vec;

/*
 * Represent a point light
 */
public abstract class Light implements IInitable {

	protected Vec color;

	public abstract Vec getIntansityLight(Point3D p);

	public static boolean isLight(String s) {

		if (s.equals("dir-light") || s.equals("omni-light") || s.equals("spot-light")) {
			return true;
		}
		return false;
	}
}
