package CG_ex3_partial.src.ex3.render.raytrace.light;

import java.util.Map;

import CG_ex3_partial.src.math.Point3D;
import CG_ex3_partial.src.math.Vec;

public class dirLight extends Light {

	private Vec direction;

	public dirLight(){}
	public dirLight(Map<String, String> attributes) {
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
		if (!attributes.containsKey("direction")) {
			throw new IllegalArgumentException("missing direction to the light");
		} else {
			direction = new Vec(attributes.get("direction"));
		}
	}

	public Vec getIntansityLight(Point3D p) {
		return color;
	}

	public Vec getDirection() {
		return direction;
	}

}
