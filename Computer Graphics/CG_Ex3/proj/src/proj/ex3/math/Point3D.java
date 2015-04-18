/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.math;

import java.util.Scanner;

public class Point3D{
	public double x, y, z;


	public Point3D(String v) {
		Scanner s = new Scanner(v);
		x = s.nextDouble();
		y = s.nextDouble();
		z = s.nextDouble();
		s.close();
	}
	
	public Point3D(Point3D p, Vec v) {
		this.x = p.x + v.x;
		this.y = p.y + v.y;
		this.z = p.z + v.z;
	}
	
	public Point3D(Point3D p1, Point3D p2) {
		this.x = p1.x + p2.x;
		this.y = p1.y + p2.y;
		this.z = p1.z + p2.z;
	}
	
	public Point3D() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
	public Point3D(Point3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	
	public void add(Vec a) {
		x += a.x;
		y += a.y;
		z += a.z;
	}
	
	public void mac(double s, Vec a) {
		x += s * a.x;
		y += s * a.y;
		z += s * a.z;
	}

	public Vec ToVec(){
		Vec v = new Vec(this.x,this.y,this.z);
		return v;
	}
	
	/**
	 * gets 3 3d points and determines if they are co-linear
	 * @param comparePoint1
	 * @param comparePoint2
	 * @param comparePoint3
	 * @return
	 */
	public static boolean isColinear(Point3D comparePoint1, 
			Point3D comparePoint2, Point3D comparePoint3) {
		
		Vec v1 = Vec.sub(comparePoint2.ToVec(), comparePoint1.ToVec());
		Vec v2 = Vec.sub(comparePoint3.ToVec(), comparePoint1.ToVec());
		
		return Point3D.isColinear(v1, v2);
		
	}
	
	/**
	 * gets 2 vectors and determines if they are co-linear
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static boolean isColinear(Vec vec1, Vec vec2) {
		double epsilon = 0.0005;
		Vec v1 = vec1.clone();
		Vec v2 = vec2.clone();
		v1.normalize();
		v2.normalize();
		double res = Vec.dotProd(v1, v2) / (v1.length() * v2.length());
		
		if ((res > 1 && res < 1 + epsilon) ||
			(res < -1 && res > -1 - epsilon)) {
			return true;
		}
		return false;
	}
	
	public static Vec sub(Point3D a, Point3D b) {
	    return new Vec(a.x - b.x, a.y - b.y, a.z - b.z);
	  }
	
	/**
	 * Subtract the given point from this point.
	 * @param p
	 * @return
	 */
	 public Vec sub(Point3D p) {
		 return new Vec(this.x - p.x, this.y - p.y, this.z - p.z);
	}
	 
	 /**
	  * Add The given vector to the given point.
	  * @param a
	  * @param b
	  * @return
	  */
	  public static Point3D add(Vec a, Point3D b)
	  {
	    Point3D p = new Point3D();
	    p.x = (a.x + b.x);
	    p.y = (a.y + b.y);
	    p.z = (a.z + b.z);

	    return p;
	  }


}