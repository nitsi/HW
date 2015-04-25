package CG_ex3_partial.src.ex3.render.raytrace.light;

import java.util.Map;
import java.util.Scanner;

import CG_ex3_partial.src.math.Point3D;
import CG_ex3_partial.src.math.Vec;

public class OmniLight extends Light{

	private Point3D position;
	private double kConst;
	private double kLinear;
	private double kQuadratic;
	
	public OmniLight(){}
	public OmniLight(Map<String, String> attributes) {
		init(attributes);
	}

	@Override
	public void init(Map<String, String> attributes)
			throws IllegalArgumentException {

		if (!attributes.containsKey("color")) {
			color = new Vec(1, 1, 1);
		} else {
			color = new Vec(attributes.get("color"));
		}
		if (!attributes.containsKey("pos")){
			throw new IllegalArgumentException("missing position to the light");
		}else{ 
			position = new Point3D(attributes.get("pos"));
		}
		if (!attributes.containsKey("kc")) {
			kConst = 1;
		} else {
			kConst = Double.parseDouble(attributes.get("kc"));
		}
		if (!attributes.containsKey("kl")) {
			kLinear = 0;
		} else {
			kLinear = Double.parseDouble(attributes.get("kl"));
		}
		if (!attributes.containsKey("kq")) {
			kQuadratic = 0;
		} else {
			kQuadratic = Double.parseDouble(attributes.get("kq"));
		}
		
		
	}

	public Vec getIntansityLight(Point3D p) {
		
		double lengthFromHit = p.distance(position);
		Vec IL = Vec.scale(1 / (kConst + kLinear * lengthFromHit + kQuadratic * lengthFromHit * lengthFromHit), color);
		return IL;
	}

	public Point3D getPosition() {
		return position;
	}
}
