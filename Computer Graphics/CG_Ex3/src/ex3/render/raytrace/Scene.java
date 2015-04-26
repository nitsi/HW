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

	protected List<Surface> g_SurfacesList;
	protected List<Light> g_LightsList;
	protected Camera g_MainCamera;
	private Vec g_BackgroundCol;
	private String g_BackgroundTexture;
	private int g_MaximumRecursionDepth;
	private Vec g_AmbientLight;

	/**
	 * construct an empty scene object
	 */
	public Scene() {
		g_SurfacesList = new LinkedList<Surface>();
		g_LightsList = new LinkedList<Light>();
		g_MainCamera = new Camera();
	}

	/**
	 * initialization method, receives attributes map
	 */
	public void init(Map<String, String> attributes) {
		// if we got texture, we take it. otherwise we check if we didn't
		// receive "background-col". if so, we create new vector with 0,0,0
		// otherwise we save the data in g_backgroundCol

		if (attributes.containsKey("background-tex")) {
			g_BackgroundTexture = attributes.get("background-tex");
		} else if (!attributes.containsKey("background-col")) {
			g_BackgroundCol = new Vec("0 0 0");
		} else {
			g_BackgroundCol = new Vec(attributes.get("background-col"));
		}
		// set recursion depth and ambient light according to input test
		g_MaximumRecursionDepth = attributes.containsKey("max-recursion-level") ? Integer.parseInt(attributes.get("max-recursion-level")) : 10;
		g_AmbientLight = attributes.containsKey("ambient-light") ? new Vec(attributes.get("ambient-light")) : new Vec("0 0 0");

	}

	/**
	 * 
	 * @param ray
	 * @return the nearest intersection to the given ray, null if no such
	 */
	public Intersection findIntersection(Ray ray) {

		double i_minimalDistance = Double.MAX_VALUE;
		Point3D i_minimalPoint = null;
		Surface i_currentSurface = null;
		double i_tempDistaceHolder = 0.0;

		for (Surface i_surfaceIterator : g_SurfacesList) {
			if (i_surfaceIterator instanceof Polygon) {

				Point3D i_polygonCastedRayIntersection = Intersection.intersection_RayAndPolygon(ray, (Polygon) i_surfaceIterator);
				// means no intersection
				if (i_polygonCastedRayIntersection == null) {
					continue;
				}
				// hold the distance in variable for further injection

				i_tempDistaceHolder = Point3D.distance(ray.g_rayPoint, i_polygonCastedRayIntersection);

				if (i_tempDistaceHolder < i_minimalDistance && i_tempDistaceHolder > 0.001) {

					i_minimalDistance = i_tempDistaceHolder;
					i_minimalPoint = i_polygonCastedRayIntersection;
					i_currentSurface = i_surfaceIterator;
				}
			}
			if (i_surfaceIterator instanceof Disc) {
				Point3D i_discCastedRayIntersection = Intersection.intersection_RayAndDisc(ray, (Disc) i_surfaceIterator);
				if (i_discCastedRayIntersection == null) {
					continue;
				}
				i_tempDistaceHolder = Point3D.distance(ray.g_rayPoint, i_discCastedRayIntersection);

				if (i_tempDistaceHolder < i_minimalDistance && i_tempDistaceHolder > 0.001) {

					i_minimalDistance = i_tempDistaceHolder;
					i_minimalPoint = i_discCastedRayIntersection;
					i_currentSurface = i_surfaceIterator;
				}
			}
			if (i_surfaceIterator instanceof Sphere) {
				Point3D i_sphereCastedRayIntersection = Intersection.intersection_RayAndSphere(ray, (Sphere) i_surfaceIterator);
				if (i_sphereCastedRayIntersection == null) {
					continue;
				}
				i_tempDistaceHolder = Point3D.distance(ray.g_rayPoint, i_sphereCastedRayIntersection);
				if (i_tempDistaceHolder < i_minimalDistance && i_tempDistaceHolder > 0.001) {
					i_minimalDistance = i_tempDistaceHolder;
					i_minimalPoint = i_sphereCastedRayIntersection;
					i_currentSurface = i_surfaceIterator;
				}
			}
		}
		return (i_minimalPoint != null || i_currentSurface != null) ? new Intersection(i_currentSurface, i_minimalPoint) : null;
	}

	public Vec calcColor(Ray ray, int level) {

		if (level == g_MaximumRecursionDepth) {
			return new Vec(0, 0, 0);
		}

		Intersection intersection = findIntersection(ray);

		if (intersection == null) {
			return g_BackgroundCol;
		}

		Vec color = new Vec();

		color.add(calcEmissionColor(intersection));

		color.add(calcAmbientColor(intersection));

		for (Light light : g_LightsList) {
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
				double distanceToNewIntersection = Point3D.distance(shootRayToLight.g_rayPoint, intersectionWithobject.getPoint());
				if (distanceToLight > distanceToNewIntersection + 0.0001 && distanceToNewIntersection > 0.0001) {
					continue;
				}
			}
			color.add(calcDiffuseColor(intersection, light));
			color.add(calcSpecularColor(intersection, light, ray));

		}

		Vec normal = intersection.getSurface().getNormalAtPoint(intersection.getPoint());
		Ray reflectionRay = new Ray(intersection.getPoint(), ray.g_rayDirection.reflect(normal));

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
		return Vec.scale(intersection.getSurface().getAmbiant(), g_AmbientLight);
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
		Vec normalAtIntersectionPoint = object.getNormalAtPoint(point);

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
		Vec normalAtIntersectionPoint = object.getNormalAtPoint(intersectionPoint);

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
		Vec eyeLookAtPoint = Point3D.vectorBetweenTwoPoints(ray.g_rayPoint, intersectionPoint);
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
			g_LightsList.add(light);
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
			g_SurfacesList.add(surface);
		}
	}

	public void setCameraAttributes(Map<String, String> attributes) {
		this.g_MainCamera = new Camera();
		this.g_MainCamera.init(attributes);
	}

	public Ray castRay(double x, double y, double height, double width) {
		return g_MainCamera.generatePixelPiercingRay(x, y, height, width);
	}
}
