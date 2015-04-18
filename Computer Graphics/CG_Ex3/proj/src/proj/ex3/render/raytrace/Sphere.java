/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/


package proj.ex3.render.raytrace;

import java.util.Map;

import proj.ex3.render.raytrace.Surface;
import proj.ex3.math.Point3D;
import proj.ex3.math.Ray;
import proj.ex3.math.Vec;

public class Sphere extends Surface {

	protected Point3D center;
	protected double radius;

	@Override
	public double nearestIntersection(Ray ray, boolean backSide) {

		boolean isGeometric = false;

		if (isGeometric) {
			Vec V = new Vec(ray.p, center);

			double qB = V.dotProd(ray.v);
			double delta = (qB * qB) - ray.v.lengthSquared()
					* (V.lengthSquared() - radius * radius);

			if (delta <= 0)
				return Double.POSITIVE_INFINITY; // no intersection

			double sqDelta = Math.sqrt(delta);
			double tp = (qB + sqDelta);
			if (tp < 0)
				return Double.POSITIVE_INFINITY;
			double tn = (qB - sqDelta);

			return (tn > 0.0) ? tn : tp; // inside or outside
		} else {

			double a = 1;
			double b = 2 * (ray.v.x * (ray.p.x - center.x) + ray.v.y
					* (ray.p.y - center.y) + ray.v.z * (ray.p.z - center.z));
			double c = Math.pow(ray.p.x - center.x, 2)
					+ Math.pow(ray.p.y - center.y, 2)
					+ Math.pow(ray.p.z - center.z, 2) - radius * radius;

			double R = b * b - 4 * a * c;
			if (R < 0)
				return Double.POSITIVE_INFINITY;

			double T = Math.sqrt(R);

			double t1 = (-b + T) / (2 * a);
			double t2 = (-b - T) / (2 * a);
			
			if(!backSide){
				return Math.min(t1, t2);
			}
			
			return Math.max(t1, t2);			
		}
	}

	@Override
	public Vec normalAt(Point3D intersection, Ray ray) {

		Vec res = new Vec(center, intersection);
		res.normalize();
		return res;
	}

	@Override
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("center"))
			center = new Point3D(attributes.get("center"));
		if (attributes.containsKey("radius"))
			radius = Double.valueOf(attributes.get("radius"));
		super.init(attributes);
	}
}