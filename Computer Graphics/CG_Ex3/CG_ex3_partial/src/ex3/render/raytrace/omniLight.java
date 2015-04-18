package ex3.render.raytrace;
import java.util.Map;

import math.Point3D;
import math.Vec;

public class omniLight extends Light {
	
	protected Point3D pos = null;
	protected Point3D attenuation = null;
	
	
	public omniLight() {
		super.color = new Vec(1,1,1);
		new Point3D("1 0 0");
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("pos")) {
			new Point3D(attributes.get("pos"));
		}
		if (attributes.containsKey("attenuation")) {
			new Point3D(attributes.get("attenuation"));
		}
	}


}