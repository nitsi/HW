/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;

import java.util.Map;

import proj.ex3.math.Point3D;
import proj.ex3.math.Vec;

public class SpotLight extends Light {
	
	private Vec DEFAULT_SPOTLIGHT = new Vec(0.0d, 0.0d, 0.0d);
	
	protected Vec direction = null;

	@Override
	public Vec getLightAtPoint(Vec point) {	
		Vec L = Vec.sub(this.pos, point);
		double length = L.length();
		L.normalize();
		double LD = -L.dotProd(this.direction);
		if (LD <= 0.0d)
		      return DEFAULT_SPOTLIGHT;
		Vec result = this.color.clone();
		double lightDenominator = this.attenuation.x + 
				this.attenuation.y * length +
				 this.attenuation.z * length * length;
		result.scale(LD / lightDenominator);
		return result;
	}
	
	@Override
	public void init(Map<String, String> attributes) {
//		if(attributes.containsKey("dir")) {
//			this.direction = new Vec(attributes.get("dir"));	
//			this.direction.normalize();
//		}		
//		super.init(attributes);	
		
		super.init(attributes);
		if (attributes.containsKey("direction")) {
			new Point3D(attributes.get("direction"));
		}
	}
}
