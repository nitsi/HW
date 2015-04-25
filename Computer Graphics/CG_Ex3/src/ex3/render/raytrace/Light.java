/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

/*
 * Represent a point light
 */
public abstract class Light implements IInitable {

	protected Vec g_color;

	public abstract Vec getIntansityLight(Point3D p);

	public static boolean isLight(String s) {

		if (s.equals("dir-light") || s.equals("omni-light") || s.equals("spot-light")) {
			return true;
		}
		return false;
	}
}
