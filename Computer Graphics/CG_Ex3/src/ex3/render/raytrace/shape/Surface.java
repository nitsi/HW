package CG_ex3_partial.src.ex3.render.raytrace.shape;

import java.util.Map;

import CG_ex3_partial.src.ex3.render.raytrace.IInitable;
import CG_ex3_partial.src.math.Point3D;
//import CG_ex3_partial.src.math.Point3D;
//import CG_ex3_partial.src.math.Vec;
import CG_ex3_partial.src.math.Vec;

public abstract class Surface implements IInitable{

	protected Vec mltDifuse;
	protected Vec mltSpecular;
	protected Vec mltAmbient;
	protected Vec mltEmission;
	protected double mltShininess;
	protected double reflectance;
	
	public  Double getReflectance(){
		return reflectance;
	}
	public Double getShininess(){
		return mltShininess;
	}
	public Vec getEmission(){
		return mltEmission;
	}
	public Vec getDifuse(){
		return mltDifuse;
	}
	
	protected void originInit(Map<String, String> attributes){
		
		if (!attributes.containsKey("mtl-diffuse")){
			mltDifuse = new Vec(0.7, 0.7, 0.7);
		}else{ 
			mltDifuse = new Vec(attributes.get("mtl-diffuse"));
		}
		if (!attributes.containsKey("mtl-specular")){
			mltSpecular = new Vec(1, 1, 1);
		}else{ 
			mltSpecular = new Vec(attributes.get("mtl-specular"));
		}
		if (!attributes.containsKey("ambient-light")){
			mltAmbient = new Vec(0.1, 0.1, 0.1);
		}else{ 
			mltAmbient = new Vec(attributes.get("mtl-ambient"));
		}
		if (!attributes.containsKey("mtl-emission")){
			mltEmission = new Vec(0, 0, 0);
		}else{ 
			mltEmission = new Vec(attributes.get("mtl-emission"));
		}
		if (!attributes.containsKey("mtl-shininess")){
			mltShininess = 100;
		}else{ 
			mltShininess = Double.parseDouble(attributes.get("mtl-shininess"));
		}
		if (!attributes.containsKey("reflectance")){
			reflectance = 0;
		}else{ 
			reflectance = Double.parseDouble(attributes.get("reflectance"));
		}
	}
	public abstract Vec getNormalInPoint(Point3D p);

	public Vec getAmbiant() {
		return mltAmbient;
	}
	public Vec getSpecular() {
		return mltSpecular;
	}
	
}
