/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;

import java.util.Map;

import proj.ex3.math.Vec;

public abstract class Light implements IInitable {
	
	protected final Vec DEFAULT_ATTEN = new Vec(1,0,0);
	
	protected Vec pos;
	protected Vec color;
	protected Vec attenuation;
	
	/**
	 * constructor
	 */
	public Light() {
		this.pos = new Vec(0, 0, 0);
		this.color = new Vec(1,1,1);
		this.attenuation = DEFAULT_ATTEN;		
	}
	
	/**
	 * constructor
	 * @param v
	 * @param color
	 * @param atten
	 */
	public Light(Vec v, Vec color, Vec atten) {
		this.pos = v;
		this.color = color;
		this.attenuation = atten;		
	}
	
	/**
	 * initializes the light
	 * @param attributes
	 */
	@Override
	public void init(Map<String, String> attributes) {
		if(attributes.containsKey("pos")) {
			this.pos = new Vec(attributes.get("pos"));
		}
		
		if(attributes.containsKey("color")){
			this.color = new Vec(attributes.get("color"));
		}
		
		if(attributes.containsKey("kc")){			
			this.attenuation.x = Double.valueOf(attributes.get("kc"));
		}
		
		if(attributes.containsKey("kl")){			
			this.attenuation.y = Double.valueOf(attributes.get("kl"));			
		}
		
		if(attributes.containsKey("kq")){			
			this.attenuation.z = Double.valueOf(attributes.get("kq"));
		}	
	}
	
	/**
	 * Given a point, returns the amount of light at this point
	 * @param point - a point on a surface
	 * @return the amount of light at the given point
	 */
	public abstract Vec getLightAtPoint(Vec point);
}
