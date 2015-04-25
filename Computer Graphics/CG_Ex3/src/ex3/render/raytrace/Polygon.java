package ex3.render.raytrace;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Polygon extends Surface{

	private int size;
	private Point3D[] p;
	public Polygon(){}
	public Polygon(Map<String, String> attributes) {
		size = 0;	
		originInit(attributes);
		init(attributes);
	}
	public int  getSize(){
		return size;
	}
	public Point3D getPoint(int i){
		if (i < 0 || i > size - 1){
			return null;
		}
		return p[i];
	}
	@Override
	public void init(Map<String, String> attributes) throws IllegalArgumentException {
		
		Map<String, String> justThePoints = new HashMap<String, String>(); 
		
		// Filter out just the points from the attributes, and count them
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			if (entry.getKey().startsWith("p")) {
				justThePoints.put(entry.getKey(), entry.getValue());
				size++;
			}
		}
		
		// Sanity check
		if (size < 3) {
			throw new IllegalArgumentException("Invalid Poly");
		}
		
		// Make sure the points are in sorted order
		Map<String, String> justThePointsSorted = new TreeMap<String, String>(justThePoints);
		
		// Initialize the points array
		p = new Point3D[size];
		
		// Populate the points array
		for (int i=0; i<size; i++) {
			p[i] = new Point3D(justThePointsSorted.get("p" + i));
		}
		
	}

	@Override
	public Vec getNormalInPoint(Point3D p) {
		
		Vec oneVectorInThePoli = Point3D.vecBetweenTwoPoints(this.p[0], this.p[1]);
		Vec towVectorInThePoli = Point3D.vecBetweenTwoPoints(this.p[0], this.p[2]);
		Vec normal = Vec.crossProd(oneVectorInThePoli, towVectorInThePoli);
		normal.normalize();
		return normal;
	}

}
