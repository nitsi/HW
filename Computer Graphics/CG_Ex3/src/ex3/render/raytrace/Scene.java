package ex3.render.raytrace;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A Scene class containing all the scene objects including camera, lights and
 * surfaces. Some suggestions for code are in comment If you uncomment these
 * lines you'll need to implement some new types like Surface
 * 
 * You can change all methods here this is only a suggestion! This is your
 * world, add members methods as you wish
 */
public class Scene implements IInitable {

	protected List<Surface> surfaces;
	protected List<Light> lights;
	protected Camera camera;
	private Vec backgroundCol;
	private String backgroundTex;
	private int maxRecursionLevel;
	private Vec ambientLight;

	public Scene() {

		surfaces = new LinkedList<Surface>();
		lights = new LinkedList<Light>();
		camera = new Camera();
	}

	public void init(Map<String, String> attributes) {

		if (!attributes.containsKey("background-col") && !attributes.containsKey("background-tex")) {
			backgroundCol = new Vec("0 0 0");
		} else if (attributes.containsKey("background-tex")) {
			backgroundTex = attributes.get("background-tex");
		} else {
			backgroundCol = new Vec(attributes.get("background-col"));
		}
		if (!attributes.containsKey("max-recursion-level")) {
			maxRecursionLevel = 10;
		} else {
			maxRecursionLevel = Integer.parseInt(attributes.get("max-recursion-level"));
		}
		if (!attributes.containsKey("ambient-light")) {
			ambientLight = new Vec("0 0 0");
		} else {
			ambientLight = new Vec(attributes.get("ambient-light"));
		}

	}

	/**
	 * Send ray return the nearest intersection. Return null if no intersection
	 * 
	 * @param ray
	 * @return
	 */
	public Intersection findIntersection(Ray ray) {

		double minDistance = Double.MAX_VALUE;
		Point3D minPoint = null;
		Surface surface = null;
		for (Surface shape : surfaces) {

			if (shape instanceof Polygon) {
				Point3D p = Intersection.intersection_RayAndPolygon(ray, (Polygon) shape);
				if (p == null) {
					continue;
				}
				if (Point3D.distance(ray.p, p) < minDistance && Point3D.distance(ray.p, p) > 0.001) {
					minDistance = Point3D.distance(ray.p, p);
					minPoint = p;
					surface = shape;
				}
			}
			if (shape instanceof Disc) {
				Point3D p = Intersection.intersection_RayAndDisc(ray, (Disc) shape);
				if (p == null) {
					continue;
				}
				if (Point3D.distance(ray.p, p) < minDistance && Point3D.distance(ray.p, p) > 0.001) {
					minDistance = Point3D.distance(ray.p, p);
					minPoint = p;
					surface = shape;
				}
			}
			if (shape instanceof Sphere) {
				Point3D p = Intersection.intersection_RayAndSphere(ray, (Sphere) shape);
				if (p == null) {
					continue;
				}
				if (Point3D.distance(ray.p, p) < minDistance && Point3D.distance(ray.p, p) > 0.001) {
					minDistance = Point3D.distance(ray.p, p);
					minPoint = p;
					surface = shape;
				}
			}
		}
		if (minPoint == null && surface == null) {
			return null;
		}
		return new Intersection(surface, minPoint);
	}

	public Vec calcColor(Ray ray, int level) {

		if (level == maxRecursionLevel) {
			return new Vec(0, 0, 0);
		}

		Intersection intersection = findIntersection(ray);

		if (intersection == null) {
			return backgroundCol;
		}

		Vec color = new Vec();

		color.add(calcEmissionColor(intersection));

		color.add(calcAmbientColor(intersection));

		for (Light light : lights) {
			Ray shootRayToLight = null;
			double distanceToLight = 0;
			if (light instanceof DirectionLight) {
				distanceToLight = Double.MAX_VALUE;
				Vec OpDirection = ((DirectionLight) light).getDirection();
				OpDirection.negate();
				shootRayToLight = new Ray(intersection.getPoint(), OpDirection);
			}
			if (light instanceof SpotLight) {
				distanceToLight = Point3D.distance(((SpotLight) light).getPosition(), intersection.getPoint());
				shootRayToLight = new Ray(intersection.getPoint(), Point3D.vectorBetweenTwoPoints(((SpotLight) light).getPosition(),
						intersection.getPoint()));
			}
			if (light instanceof OmniLight) {
				distanceToLight = Point3D.distance(((OmniLight) light).getPosition(), intersection.getPoint());
				shootRayToLight = new Ray(intersection.getPoint(), Point3D.vectorBetweenTwoPoints(((OmniLight) light).getPosition(),
						intersection.getPoint()));
			}
			Intersection intersectionWithobject = findIntersection(shootRayToLight);
			if (intersectionWithobject != null) {
				double distanceToNewIntersection = Point3D.distance(shootRayToLight.p, intersectionWithobject.getPoint());
				if (distanceToLight > distanceToNewIntersection + 0.0001 && distanceToNewIntersection > 0.0001) {
					continue;
				}
			}
			color.add(calcDiffuseColor(intersection, light));
			color.add(calcSpecularColor(intersection, light, ray));

		}
		Vec normal = intersection.getSurface().getNormalInPoint(intersection.getPoint());
		Ray reflectionRay = new Ray(intersection.getPoint(), ray.v.reflect(normal));
		double KS = intersection.getSurface().getReflectance();
		Vec reflectionColor = calcColor(reflectionRay, level + 1);
		color.add(Vec.scale(KS, reflectionColor));

		return color;
	}

	/**
	 * Calculate the amount of ambient color at the intersection point.
	 * 
	 * @param intersection
	 *            point
	 * @return ambient factor
	 */
	private Vec calcAmbientColor(Intersection intersection) {
		return Vec.scale(intersection.getSurface().getAmbiant(), ambientLight);
	}

	/**
	 * Calculate the amount of diffuse color at the intersection point, by the
	 * specified light source.
	 * 
	 * @param intersection
	 *            point
	 * @return diffuse factor
	 */
	private Vec calcDiffuseColor(Intersection intersection, Light light) {

		Surface object = intersection.getSurface();
		Point3D point = intersection.getPoint();

		// Find the normal at the intersection point
		Vec normalAtIntersectionPoint = object.getNormalInPoint(point);

		// Find the vector between the intersection point
		// and the light source, and IL at that point
		Vec L = null;
		Vec IL = null;
		if (light instanceof DirectionLight) {
			DirectionLight dLight = (DirectionLight) light;
			L = dLight.getDirection();
			L.negate();
			IL = dLight.getLightIntensity(point);
		} else if (light instanceof OmniLight) {
			OmniLight oLight = (OmniLight) light;
			L = Point3D.vectorBetweenTwoPoints(point, oLight.getPosition());
			if (object instanceof Polygon) {
				L.negate();
			}
			IL = oLight.getLightIntensity(point);
		} else {
			SpotLight sLight = (SpotLight) light;
			L = Point3D.vectorBetweenTwoPoints(point, sLight.getPosition());
			IL = sLight.getLightIntensity(point);
		}
		L.normalize();

		// Calculate the dot product between them
		double dotProduct = Vec.dotProd(normalAtIntersectionPoint, L);

		// Get the surface's diffuse coefficient
		Vec KD = object.getDifuse();
		double angel = Math.max(0, dotProduct);
		// Calculate ID
		Vec ID = Vec.scale(KD, Vec.scale(angel, IL));

		return ID;

	}

	/**
	 * Calculate the amount of specular color at the intersection point, by the
	 * specified light source.
	 * 
	 * @param intersection
	 *            point
	 * @return specular factor
	 */
	private Vec calcSpecularColor(Intersection intersection, Light light, Ray ray) {

		Surface object = intersection.getSurface();
		Point3D intersectionPoint = intersection.getPoint();

		// Find the normal at the intersection point
		Vec normalAtIntersectionPoint = object.getNormalInPoint(intersectionPoint);

		// Find the vector between the intersection point
		// and the light source, and IL at that point
		Vec L = null;
		Vec IL = null;
		if (light instanceof DirectionLight) {
			DirectionLight dLight = (DirectionLight) light;
			L = dLight.getDirection();
			L.negate();
			IL = dLight.getLightIntensity(intersectionPoint);
		} else if (light instanceof OmniLight) {
			OmniLight oLight = (OmniLight) light;
			L = Point3D.vectorBetweenTwoPoints(intersectionPoint, oLight.getPosition());
			IL = oLight.getLightIntensity(intersectionPoint);
		} else {
			SpotLight sLight = (SpotLight) light;
			L = Point3D.vectorBetweenTwoPoints(intersectionPoint, sLight.getPosition());
			IL = sLight.getLightIntensity(intersectionPoint);
		}
		L.normalize();

		// Reflect L in relation to N
		Vec R = L.reflect(normalAtIntersectionPoint);
		R.normalize();
		Vec eyeLookAtPoint = Point3D.vectorBetweenTwoPoints(ray.p, intersectionPoint);
		eyeLookAtPoint.normalize();
		// Calculate the dot product between them
		double dotProduct = Math.max(0, Vec.dotProd(eyeLookAtPoint, R));

		// Raise it to the power of n
		double dotProductN = Math.pow(dotProduct, object.getShininess());

		// Get the surface's specular coefficient
		Vec KS = object.getSpecular();

		double angel = Math.max(dotProductN, 0);

		// Calculate IS
		Vec IS = Vec.scale(KS, Vec.scale(angel, IL));
		return IS;

	}

	private Vec calcEmissionColor(Intersection intersection) {

		Surface s = intersection.getSurface();
		Vec v = s.getEmission();
		return v;
	}

	/**
	 * Add objects to the scene by name
	 * 
	 * @param name
	 *            Object's name
	 * @param attributes
	 *            Object's attributes
	 */
	public void addObjectByName(String name, Map<String, String> attributes) {
		// here is some code example for adding a surface or a light.
		// you can change everything and if you don't want this method, delete
		// it

		name.toLowerCase();

		Surface surface = null;
		Light light = null;

		if (Light.isLight(name)) {

			if ("omni-light".equals(name)) {
				light = new OmniLight();
				light = new OmniLight(attributes);
			}
			if ("dir-light".equals(name)) {
				light = new DirectionLight();
				light = new DirectionLight(attributes);
			}
			if ("spot-light".equals(name)) {
				light = new SpotLight();
				light = new SpotLight(attributes);
			}
			lights.add(light);
		} else {
			if ("sphere".equals(name)) {
				surface = new Sphere();
				surface = new Sphere(attributes);
			} else if ("disc".equals(name)) {
				surface = new Disc();
				surface = new Disc(attributes);
			} else {
				surface = new Polygon();
				surface = new Polygon(attributes);
			}
			surfaces.add(surface);
		}
	}

	public void setCameraAttributes(Map<String, String> attributes) {
		this.camera = new Camera();
		this.camera.init(attributes);
	}

	public Ray castRay(double x, double y, double height, double width) {
		return camera.generatePixelPiercingRay(x, y, height, width);
	}
}
