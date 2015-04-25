/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;
import java.util.Map;

import proj.ex3.math.Point3D;
import proj.ex3.math.Vec;

public class dirLight extends Light {
	
	private Point3D direction = null;
	
	public dirLight()
	{
		this.direction = new Point3D("0 0 0");
		super.color = new Vec("1 1 1");
	}
	
	public void init(Map<String, String> attributes)
	{
		super.init(attributes);
		if (attributes.containsKey("direction")) {
			new Point3D(attributes.get("direction"));
		}
	}

	@Override
	public Vec getLightAtPoint(Vec pnt)
	{
		return color;
	}
}
