package CG_ex3_partial.src.ex3.render.raytrace.light;

import java.util.Map;
import java.util.Scanner;

import javax.swing.text.Position;

import CG_ex3_partial.src.math.Point3D;
import CG_ex3_partial.src.math.Vec;

public class SpotLight extends Light {

	private Vec direction;
	private Point3D position;
	private double kConst;
	private double kLinear;
	private double kQuadratic;

	public SpotLight() {
	}

	public Point3D getPosition() {
		return position;
	}

	public SpotLight(Map<String, String> attributes) {
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
		if (!attributes.containsKey("pos")) {
			throw new IllegalArgumentException("missing position to the light");
		} else {
			position = new Point3D(attributes.get("pos"));
		}
		if (attributes.containsKey("kc")) {
			kConst = Double.parseDouble(attributes.get("kc"));
		} else {
			kConst = 1;
		}

		if (attributes.containsKey("kl")) {
			kLinear = Double.parseDouble(attributes.get("kl"));
		} else {
			kLinear = 0;
		}

		if (attributes.containsKey("kq")) {
			kQuadratic = Double.parseDouble(attributes.get("kq"));
		} else {
			kQuadratic = 0;
		}
		if (!attributes.containsKey("dir")) {
			throw new IllegalArgumentException("missing direction to the light");
		} else {
			direction = new Vec(attributes.get("dir"));
			direction.normalize();
		}

	}

	public Vec getIntansityLight(Point3D p) {

		Vec locationRelativeToLightSource = Point3D.vecBetweenTowPoints(p,
				position);
		locationRelativeToLightSource.normalize();
		double angleBetweenObjectAndLight = Math.max(
				Vec.dotProd(locationRelativeToLightSource, direction), 0);
		double lengthFromHit = p.distance(position);
		Vec IL = Vec.scale(1 / (kConst + kLinear * lengthFromHit + kQuadratic
				* lengthFromHit * lengthFromHit),
				Vec.scale(angleBetweenObjectAndLight, color));
		return IL;
	}
}
