package ex3.render.raytrace;

import java.util.Map;

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
			g_color = new Vec(1, 1, 1);
		} else {
			g_color = new Vec(attributes.get("color"));
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
		Vec IL = Vec.scale(1 / (kConst + kLinear * lengthFromHit + kQuadratic * lengthFromHit * lengthFromHit), g_color);
		return IL;
	}

	public Point3D getPosition() {
		return position;
	}
}
