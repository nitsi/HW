package ex3.render.raytrace;

/*
 * Represent a point light
 */
public abstract class Light implements IInitable {

	protected Vec color;
	
	public abstract Vec getIntansityLight(Point3D p);
	
	public static boolean isLight(String s){
		
		if (s.equals("dir-light") || s.equals("omni-light") || s.equals("spot-light")){
			return true;
		}
		return false;
	}
}
