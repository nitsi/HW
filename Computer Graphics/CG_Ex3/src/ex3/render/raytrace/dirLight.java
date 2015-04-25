package ex3.render.raytrace;

import java.util.Map;

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
