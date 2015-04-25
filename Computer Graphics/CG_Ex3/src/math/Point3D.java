package CG_ex3_partial.src.math;

import java.util.Scanner;

public class Point3D {

	public double x, y, z;

	public Point3D(String v) {
		Scanner s = new Scanner(v);
		x = s.nextDouble();
		y = s.nextDouble();
		z = s.nextDouble();
		s.close();
	}

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(Point3D point) {
		x = point.x;
		y = point.y;
		z = point.z;
	}

	public Point3D() {
		x = 0;
		y = 0;
		z = 0;
	}

	public boolean equals(Vec a) {
		return ((a.x == x) && (a.y == y) && (a.z == z));
	}
	// return the distance between point and a given point
	public double distance (Point3D point){
		return Math.sqrt((point.x - x) * (point.x - x) + (point.y - y) * (point.y - y) + (point.z - z) * (point.z - z)) ;
	}
	// return the distance between 2 points
	public static double distance (Point3D pointOne, Point3D pointTow){
		return Math.sqrt((pointOne.x - pointTow.x) * (pointOne.x - pointTow.x) + (pointOne.y - pointTow.y) * (pointOne.y - pointTow.y) + (pointOne.z - pointTow.z) * (pointOne.z - pointTow.z)) ;
	}
	// return the vector between point and a given point
	public Vec vecBetweenTowPoints(Point3D point){
		return new Vec( point.x - x, point.y - y, point.z - z);
	}
	// return the vector between 2 points
	public static Vec vecBetweenTowPoints (Point3D pointOne, Point3D pointTow){
		return new Vec(pointOne.x - pointTow.x, pointOne.y - pointTow.y, pointOne.z - pointTow.z);
	}
	public static Point3D add (Vec v, Point3D p){
		return new Point3D(v.x + p.x, v.y + p.y, v.z + p.z);
	}
	
	public String toString(){
		return ("x: " + x + "y: " + y + "z: " + z ) ;
	}
	public Point3D clone(){
		return new Point3D(this);
	}
}
