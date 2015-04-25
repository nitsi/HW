package CG_ex3_partial.src.ex3.render.raytrace.shape;

import java.util.Map;

import CG_ex3_partial.src.math.Point3D;
import CG_ex3_partial.src.math.Vec;

public class Disc extends Sphere{

	private Vec normal;
	
	public Disc(){}
	public Vec getNormal(){
		return normal;
	}
	
	public Disc(Map<String, String> attributes) {
	
		super(attributes);
		constractTheNormal(attributes);
	}


	public void constractTheNormal(Map<String, String> attributes) {
		if (!attributes.containsKey("normal")){
			throw new IllegalArgumentException("missing normal");
		}else{ 
			normal = new Vec(attributes.get("normal"));
		}
	}
	@Override
	public Vec getNormalInPoint(Point3D p) {
		return normal;
	}

}