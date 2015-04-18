/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;
import java.util.Map;
import proj.ex3.math.Point3D;
import proj.ex3.math.Ray;
import proj.ex3. math.Vec;


	public class Disc extends Surface
	{
	  protected Point3D ctr;
	  protected Vec normal;
	  protected double r;

	  public double nearestIntersection(Ray ray, boolean backFace)
	  {
	    double t = Point3D.sub(this.ctr, ray.p).dotProd(this.normal) / ray.v.dotProd(this.normal);
	    if (t < 0.0001D)
	      return (1.0D / 0.0D);
	    Point3D intersectionPoint = Point3D.add(Vec.scale(t, ray.v), ray.p);

	    if (intersectionPoint.sub(this.ctr).length() > this.r) {
	      return (1.0D / 0.0D);
	    }

	    double totalLength = Point3D.sub(intersectionPoint, ray.p).length();
	    if (backFace == Vec.dotProd(ray.v, this.normal) < 0.0D)
	      return (1.0D / 0.0D);
	    return totalLength;
	  }

	  public Vec normalAt(Point3D intersection, Ray ray)
	  {
	    return this.normal;
	  }

	  public void init(Map<String, String> attributes)
	  {
	    if (attributes.containsKey("center"))
	      this.ctr = new Point3D((String)attributes.get("center"));
	    if (attributes.containsKey("radius"))
	      this.r = Double.valueOf((String)attributes.get("radius")).doubleValue();
	    if (attributes.containsKey("normal")) {
	      this.normal = new Vec((String)attributes.get("normal"));
	      this.normal.normalize();
	    }
	    super.init(attributes);
	  }
	}


