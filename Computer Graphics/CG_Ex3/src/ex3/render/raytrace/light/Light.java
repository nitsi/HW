package CG_ex3_partial.src.ex3.render.raytrace.light;

import CG_ex3_partial.src.ex3.render.raytrace.IInitable;
import CG_ex3_partial.src.math.Point3D;
import CG_ex3_partial.src.math.Vec;
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
