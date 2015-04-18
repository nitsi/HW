/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;

import java.util.Map;

import proj.ex3.math.Point3D;
import proj.ex3.math.Vec;

/**
 * Represent an omni-directional light point
 * 
 */
public class omniLight extends Light {		

	// This is original code, will test to see if we still need it as we implemented 
	//using other ways
	// + Part 1 of This code create a bug, schene -> 4 -> 2 or 3 not loading completely

	//Part 1
//	protected Point3D pos = null;
//	protected Point3D attenuation = null;
//	
//	
//	public omniLight() {
//		super.color = new Vec(1,1,1);
//		new Point3D("1 0 0");
//	}
	
	// Part 2 - still her code
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("pos")) {
			new Point3D(attributes.get("pos"));
		}
		if (attributes.containsKey("attenuation")) {
			new Point3D(attributes.get("attenuation"));
		}
	}
	
	@Override
	public Vec getLightAtPoint(Vec point){
		double length = Vec.sub(point, pos).length();
		double lightDenominator = this.attenuation.x + 
				this.attenuation.y * length +
				 this.attenuation.z * length * length;
		double resultToBeScaled = 1 / lightDenominator;
		return Vec.scale(resultToBeScaled, this.color);
	}	
}
