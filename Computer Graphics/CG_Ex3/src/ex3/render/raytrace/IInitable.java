package CG_ex3_partial.src.ex3.render.raytrace;

import java.util.Map;

import CG_ex3_partial.src.math.Point3D;
import CG_ex3_partial.src.math.Vec;

/**
 * Interface for objects that can initialize from attribute pairs
 * 
 */
public interface IInitable {

	/**
	 * Initialize from attribute pairs
	 * 
	 * @param attributes
	 */
	public void init(Map<String, String> attributes) throws IllegalArgumentException;

}
