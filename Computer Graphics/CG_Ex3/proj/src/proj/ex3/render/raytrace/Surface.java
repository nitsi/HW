/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/


package proj.ex3.render.raytrace;

import java.util.Map;

import proj.ex3.render.raytrace.Material;

import proj.ex3.math.*;

/**
 * A simple surface primitive
 * 
 */
public abstract class Surface implements IInitable {

	private static final String DEFAULT_TYPE = "flat";
	private static final Vec DEFAULT_DIFFUSE = new Vec(0.7,0.7,0.7);
	private static final Vec DEFAULT_SPECULAR = new Vec(1,1,1);
	private static final Vec DEFAULT_AMBIENT = new Vec(0.1,0.1,0.1);
	private static final Vec DEFAULT_EMISSION = new Vec(0,0,0);
	private static final double DEFAULT_SHININESS = 100;
	private static final Vec DEFAULT_DIFFUSE1 = 
			new Vec(1,1,1); // White
	private static final Vec DEFAULT_DIFFUSE2 = 
			new Vec(0.2,0.2,0.2); // Dark Grey.
	private static final double DEFUALT_REF = 0;
	
	Material material;
	String mtlType = DEFAULT_TYPE;
	Vec mtlDiffuse = DEFAULT_DIFFUSE;
	Vec mtlSpecular = DEFAULT_SPECULAR;
	Vec mtlAmbient = DEFAULT_AMBIENT;
	Vec mtlEmission = DEFAULT_EMISSION;
	double mtlShininess = DEFAULT_SHININESS;
	Vec checkersDiffuse1 = DEFAULT_DIFFUSE1;
	Vec checkersDiffuse2 = DEFAULT_DIFFUSE2;
	double reflectance = DEFUALT_REF;
	
	@Override
	public void init(Map<String, String> attributes) {
		material = new Material();
		material.init(attributes);		

		if (attributes.containsKey("mtl-diffuse"))
			this.mtlDiffuse = 
			new Vec(attributes.get("mtl-diffuse"));
		
		if (attributes.containsKey("mtl-specular"))
			this.mtlSpecular = 
			new Vec(attributes.get("mtl-specular"));
		
		if (attributes.containsKey("mtl-ambient"))
			this.mtlAmbient = 
			new Vec(attributes.get("mtl-ambient"));
		
		if (attributes.containsKey("mtl-emission"))
			this.mtlEmission = 
			new Vec(attributes.get("mtl-emission"));
		
		if (attributes.containsKey("mtl-shininess"))
			this.mtlShininess = 
			Double.valueOf(attributes.get("mtl-shininess"));		
		
		if (attributes.containsKey("reflectance"))
			this.reflectance = 
			Double.valueOf(attributes.get("reflectance"));
		}

	/**
	 * Computes ray's distance to surface. Returns PosInf if no intersection.
	 * @param ray the cast ray.
	 * @param backSide boolean which signifies when the back side
	 *        should be checked for "water-tightness".
	 * @return the intersection between the surface and cast ray.
	 */
	public abstract double nearestIntersection(Ray ray, boolean backSide);

	/**
	 * Computes the surface's normal at a given intersection point
	 * 
	 * @param intersection
	 *            Point where ray intersected surface
	 * @param ray
	 *            Ray originating the intersection
	 * @return Normalized vector
	 */
	public abstract Vec normalAt(Point3D intersection, Ray ray);
}
