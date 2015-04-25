package ex3.render.raytrace;

public class Intersection {

	private Point3D g_point;
	private Surface g_surface;

	/**
	 * 
	 * @param surface
	 * @param point
	 */
	public Intersection(Surface surface, Point3D point) {
		this.g_point = point;
		this.g_surface = surface;
	}

	public static Point3D intersection_RayAndSphere(Ray ray, Sphere sphere) {

		Vec i_zeroToP0 = Point3D.vectorBetweenTwoPoints(ray.p, sphere.getCenter());
		double i_ONE = 1;
		double i_doubleDotProd = 2 * Vec.dotProd(ray.v, i_zeroToP0);
		double i_distanceDiff = i_zeroToP0.lengthSquared() - (Math.pow(sphere.getRadius(), 2));

		double i_discriminant = (Math.pow(i_doubleDotProd, 2) - (4 * i_ONE * i_distanceDiff));
		double i_discriminantSqrt = Math.sqrt(i_discriminant);

		// if the discriminant is small than zero, means there's no solution and
		// we return null, otherwise we calculate the solution

		if (i_discriminant > -1) {
			double i_intersectionCalc1, i_intersectionCalc2;
			i_intersectionCalc1 = (negateSign(i_doubleDotProd) + i_discriminantSqrt) / (2.0 * i_ONE);
			i_intersectionCalc2 = (negateSign(i_doubleDotProd) - i_discriminantSqrt) / (2.0 * i_ONE);

			// if the calculation larger than zero, we find the closest
			// intersectionCalc. otherwise the intersection is irrelevant

			if (i_intersectionCalc1 > 0 && i_intersectionCalc2 > 0) {
				// get the minimal value between the two. if both are larger
				double i_minimalValue = Math.min(i_intersectionCalc1, i_intersectionCalc2);

				// Return the intersection location in Point3D
				return Point3D.add(Vec.scale(i_minimalValue, ray.v), ray.p);

			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	/**
	 * 
	 * @param i_number
	 * @return the number with opposite sign
	 */
	private static double negateSign(double i_number) {
		return -1 * i_number;
	}

	public static Point3D intersection_RayAndDisc(Ray ray, Disc disc) {

		// Check if the ray intersects with the disc
		Point3D i_RayAndDiscSurfaceIntersection = intersection_RayAndSurface(ray, disc.getNormal(), disc.getCenter());

		// if intersection is null, means we ray doesn't reach the disc
		if (i_RayAndDiscSurfaceIntersection == null) {
			return null;
		}

		// we calculate whether the intersection happened within the disc radius
		// or not. if yes, we return the intersection point in Point3D,
		// otherwise null

		double i_RayAndDiscCenterDistance = Point3D.distance(i_RayAndDiscSurfaceIntersection, disc.getCenter());

		return i_RayAndDiscCenterDistance > disc.getRadius() ? null : i_RayAndDiscSurfaceIntersection;

	}

	public static Point3D intersection_RayAndPolygon(Ray ray, Polygon polygon) {

		// Test whether the ray intersects with the polygon's plane
		Point3D i_RayAndPolygonSurfaceIntersection = intersection_RayAndSurface(ray, polygon.getNormalInPoint(null), polygon.getPoint(0));

		// if no intersection with the plane, there's no intersection with the
		// polygon either
		if (i_RayAndPolygonSurfaceIntersection == null) {
			return null;
		}

		// we verify the intersection happened within the polygon's area
		Vec i_zeroVector = new Vec(0, 0, 0);
		for (int i = 0; i < polygon.getSize(); i++) {
			Vec i_firstVector = Point3D.vectorBetweenTwoPoints(polygon.getPoint(i), ray.p);
			Vec i_secondVector = Point3D.vectorBetweenTwoPoints(polygon.getPoint((i + 1) % polygon.getSize()), ray.p);

			// get the cross product
			Vec i_vectorsNormal = Vec.crossProd(i_secondVector, i_firstVector);

			// test if the normal is the zero vector
			if (i_zeroVector.equals(i_vectorsNormal)) {
				return null;
			} else {
				i_vectorsNormal.normalize();
				// if the dot product of the normal and the ray is smaller than
				// zero, we're out
				if (Vec.dotProd(i_vectorsNormal, ray.v) < 0) {
					return null;
				}
			}
		}
		return i_RayAndPolygonSurfaceIntersection;
	}

	private static Point3D intersection_RayAndSurface(Ray ray, Vec surfaceNormal, Point3D pointOnSurface) {

		double i_RaySurfaceIntersection = Vec.dotProd(ray.v, surfaceNormal);

		// Test if the surface is facing the ray
		if (i_RaySurfaceIntersection >= 0) {
			// The surface is facing away from the ray or they are orthogonal,
			// we ignore them
			return null;
		}

		// If we got here, then the ray and the surface are not parallel.
		// Find the intersection of the ray with the surface plane

		Vec i_RayToSurfaceDistance = Point3D.vectorBetweenTwoPoints(pointOnSurface, ray.p);
		
		double i_RayToSurfaceNormal = Vec.dotProd(i_RayToSurfaceDistance, surfaceNormal);
		
		double i_RayToSurfaceNormalDivision = i_RayToSurfaceNormal / i_RaySurfaceIntersection;
		return Point3D.add(Vec.scale(i_RayToSurfaceNormalDivision, ray.v), ray.p);

	}

	public Surface getSurface() {
		return g_surface;
	}

	public Point3D getPoint() {
		return g_point;

	}
}