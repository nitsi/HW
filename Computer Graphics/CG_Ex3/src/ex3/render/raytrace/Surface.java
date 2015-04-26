/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.Map;

public abstract class Surface implements IInitable {

	protected Vec g_materialDifuse;
	protected Vec g_materialSpecular;
	protected Vec g_materialAmbient;
	protected Vec g_materialEmission;
	protected double g_materialShininess;
	protected double g_materialReflectance;

	public Double getReflectance() {
		return g_materialReflectance;
	}

	public Double getShininess() {
		return g_materialShininess;
	}

	public Vec getEmission() {
		return g_materialEmission;
	}

	public Vec getDifuse() {
		return g_materialDifuse;
	}

	protected void originInit(Map<String, String> attributes) {
		
		// populate variables according to attributes checks, if attributes doesn't exist -> we use fallback value
		g_materialDifuse = attributes.containsKey("mtl-diffuse") ? new Vec(attributes.get("mtl-diffuse")) : new Vec(0.7, 0.7, 0.7);
		g_materialSpecular = attributes.containsKey("mtl-specular") ? new Vec(attributes.get("mtl-specular")) : new Vec(1, 1, 1);
		g_materialAmbient = attributes.containsKey("ambient-light") ? new Vec(attributes.get("mtl-ambient")) : new Vec(0.1, 0.1, 0.1);
		g_materialEmission = attributes.containsKey("mtl-emission") ? new Vec(attributes.get("mtl-emission")) : new Vec("0 0 0");
		g_materialShininess = attributes.containsKey("mtl-shininess") ? Double.parseDouble(attributes.get("mtl-shininess")) : 100;
		g_materialReflectance = attributes.containsKey("reflectance") ? Double.parseDouble(attributes.get("reflectance")) : 0;
	}

	/**
	 * 
	 * @param point
	 * @return g_ambient and g_specular
	 */
	public abstract Vec getNormalAtPoint(Point3D point);

	public Vec getAmbiant() {
		return g_materialAmbient;
	}

	public Vec getSpecular() {
		return g_materialSpecular;
	}

}
