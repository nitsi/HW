/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/


package proj.ex3.render.raytrace;

import java.util.Map;
import java.util.Scanner;

import proj.ex3.math.Point3D;
import proj.ex3.math.Ray;
import proj.ex3.math.Vec;

public class Triangle extends Surface {

	private Point3D P0;
	private Point3D P1;
	private Point3D P2;
	private Vec vec1;
	private Vec vec2;
	private Vec normal;
	private int numOfTriangleInTrimesh;

	/**
	 * constructor
	 * @param P0
	 * @param P1
	 * @param P2
	 */
	public Triangle(Point3D P0, Point3D P1, Point3D P2) {
		this.P0 = P0;
		this.P1 = P1;
		this.P2 = P2;
		this.vec1 = new Vec(this.P0, this.P1);
		this.vec2 = new Vec(this.P0, this.P2);
		
		this.normal = Vec.crossProd(vec1, vec2);
		this.normal.normalize();
	}
	
	/**
	 * constructor
	 */
	public Triangle(int numOfTriangleInTrimesh) {
		this.numOfTriangleInTrimesh = numOfTriangleInTrimesh;
	}
	
	@Override
	public double nearestIntersection(Ray ray, boolean backSide) {
		
		// Not intersection at all.
		if (backSide == Vec.dotProd(ray.v, normal) < 0.0d){
			return Double.POSITIVE_INFINITY;
		}			
		
		Vec P0Negate = P0.ToVec().clone();
		P0Negate.negate();
		
		double P0NegateNormal = Vec.dotProd(P0Negate, normal);
		double distance = -(Vec.dotProd(ray.p.ToVec(), normal) + P0NegateNormal)
											/(Vec.dotProd(ray.v, normal));
		
		// Not intersection at all!
		if (distance < 0)
			return Double.POSITIVE_INFINITY;
		
		Vec intersectionPoint = 
				Vec.add(ray.p.ToVec(), Vec.scale(distance, ray.v));
		Vec P0intersectPoint = Vec.sub(intersectionPoint.clone(),P0.ToVec());
		Vec P1intersectPoint = Vec.sub(intersectionPoint.clone(),P1.ToVec());
		Vec P2intersectPoint = Vec.sub(intersectionPoint.clone(),P2.ToVec());

		// Length of the vectors of the rectangle.
		Vec cpP1_P0 =
				Vec.crossProd(Vec.sub(P1.ToVec(), intersectionPoint),
						P0intersectPoint);
		Vec cpP2_P1 = 
				Vec.crossProd(Vec.sub(P2.ToVec(), P1.ToVec()),
						P1intersectPoint);
		Vec cpP0_P2 = 
				Vec.crossProd(Vec.sub(P0.ToVec(), P2.ToVec()), 
						P2intersectPoint);
		
		double dotCPP1_P0 = Vec.dotProd(cpP1_P0, normal);
		double dotCPP2_P1 = Vec.dotProd(cpP2_P1, normal);
		double dotCPP0_P2 = Vec.dotProd(cpP0_P2, normal);
		
		if ((dotCPP1_P0 >= 0) && (dotCPP2_P1 >= 0) && (dotCPP0_P2 >= 0))
			return distance;
		
		// If not
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public Vec normalAt(Point3D intersection, Ray ray) {
		return normal;
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		String curTriangleIndexString = String.valueOf(numOfTriangleInTrimesh);
		String currentTriangle = Trimesh.TRIANGLE_PREFIX + 
				curTriangleIndexString;
		String curTriangleCordsString = attributes.get(currentTriangle);
		Scanner s = new Scanner(curTriangleCordsString);
		
		double p0x = s.nextDouble();
		double p0y = s.nextDouble();
		double p0z = s.nextDouble();
		String vec_info = p0x + " " + p0y + " " + p0z;
		P0 = new Point3D(vec_info);
		
		double p1x = s.nextDouble();
		double p1y = s.nextDouble();
		double p1z = s.nextDouble();
		vec_info = p1x + " " + p1y + " " + p1z;
		P1 = new Point3D(vec_info);
		
		double p2x = s.nextDouble();
		double p2y = s.nextDouble();
		double p2z = s.nextDouble();
		vec_info = p2x + " " + p2y + " " + p2z;
		P2 = new Point3D(vec_info);		
		
		this.vec1 = new Vec(this.P1, this.P0);
		this.vec2 = new Vec(this.P2, this.P0);
		
		s.close();
		
		// checks if the points are co-linear
		if (Point3D.isColinear(P0, P1, P2)) {
			throw new IllegalArgumentException("Lines Are Co-Linear");
		}

		this.normal = Vec.crossProd(vec1, vec2);
		this.normal.normalize();
		super.init(attributes);			
	}	
}