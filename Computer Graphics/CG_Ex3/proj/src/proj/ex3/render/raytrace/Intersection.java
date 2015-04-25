package proj.ex3.render.raytrace;

import java.awt.Polygon;

import proj.ex3.math.Point3D;
import proj.ex3.math.Ray;
import proj.ex3.math.Vec;

public class Intersection {

	private Point3D point;
	private Surface surface;

	public Intersection(Surface obj, Point3D minPoint) {
		this.point = minPoint;
		this.surface = obj;
	}

	public static Point3D raySphereIntersection(Ray ray, Sphere sphere) {

		Vec fromOtoP0 = Point3D.vecBetweenTowPoints(ray.p, sphere.getCenter());
		double a = 1;
		double b = 2 * Vec.dotProd(ray.v, fromOtoP0);
		double c = fromOtoP0.lengthSquared() - sphere.getRadius() * sphere.getRadius();
		double discriminant = (b * b - 4 * a * c);
		double d = Math.sqrt(discriminant);

		// No solution exists
		if (discriminant < 0.0) {
			return null;
		}

		// a solution exists, find it
		double t1 = (-b + d) / (2.0 * a);
		double t2 = (-b - d) / (2.0 * a);

		// If t < 0 the intersection is behind me, ignore it
		if (t1 <= 0 && t2 <= 0) {
			return null;
		}

		// Find the closest t of the two
		double minT = 0;
		if (t1 > 0 && t2 <= 0) {
			minT = t1;
		} else if (t2 > 0 && t1 <= 0) {
			minT = t2;
		} else {
			minT = Math.min(t1, t2);
		}

		// Return the point of intersection
		return Point3D.add(Vec.scale(minT, ray.v), ray.p);

	}

	public static Point3D rayDiscIntersection(Ray ray, Disc disc) {

		// Check if the ray intersects with the disc's plane
		Point3D intersection = raySurfaceIntersection(ray, disc.getNormal(), disc.getCenter());

		// If it doesn't hit the plane, it can't hit the disc
		if (intersection == null) {
			return null;
		}

		// Now we need to make sure the intersection happened inside the disc.
		// Calculate the distance between the intersection point and the
		// center of the disc. If it's less than or equal to the radius of the
		// disc, we're good. If not, there is no ray-disc intersection.
		double dist = Point3D.distance(intersection, disc.getCenter());
		if (dist <= disc.getRadius()) {
			return intersection;
		} else {
			return null;
		}

	}

	public static Point3D rayPolygonIntersection(Ray ray, Polygon poly) {

		// Vec polyNormal = poly.getNormalAtPoint(null);

		// Check if the ray intersects with the disc's plane
		Point3D intersection = raySurfaceIntersection(ray, poly.getNormalInPoint(null), poly.getPoint(0));

		// If it doesn't hit the plane, it can't hit the poly
		if (intersection == null) {
			return null;
		}

		// Now we need to make sure the intersection happened inside the poly.
		for (int i = 0; i < poly.getSize(); i++) {
			Vec v = Point3D.vecBetweenTowPoints(poly.getPoint(i), ray.p);
			Vec u = Point3D.vecBetweenTowPoints(poly.getPoint((i + 1) % poly.getSize()), ray.p);
			Vec normal = Vec.crossProd(u, v);
			if (normal.equals(new Vec(0, 0, 0))) {
				return null;
			}
			normal.normalize();
			if (Vec.dotProd(normal, ray.v) < 0) {
				return null;
			}
		}
		return intersection;
	}

	private static Point3D raySurfaceIntersection(Ray ray, Vec surfaceNormal, Point3D pointOnSurface) {

		// First, check if the surface is facing the ray
		if (Vec.dotProd(ray.v, surfaceNormal) >= 0) {

			// The surface is facing away from the ray.
			// We hit the back-face, ignore it
			return null;

		}

		double rayDotNormal = Vec.dotProd(ray.v, surfaceNormal);

		// If the ray's vector and surface normal are orthogonal,
		// then the ray and the surface are parallel.
		if (rayDotNormal == 0) {

			// We can't see the surface, ignore it
			return null;

		}

		// If we got here, then the ray and the surface are not parallel.
		// Find the intersection of the ray with the surface plane
		Vec fromRaytoSurface = Point3D.vecBetweenTowPoints(pointOnSurface, ray.p);
		double fromRaytoSurfaceDotSurfaceNormal = Vec.dotProd(fromRaytoSurface, surfaceNormal);
		double d = fromRaytoSurfaceDotSurfaceNormal / rayDotNormal;
		return Point3D.add(Vec.scale(d, ray.v), ray.p);

	}

	public Surface getSurface() {
		return surface;
	}

	public Point3D getPoint() {
		return point;

	}
}