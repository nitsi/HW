/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
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

	/**
	 * 
	 * @param ray
	 * @param recursionLevel
	 * @return calculated color
	 */
	public Vec calcColor(Ray ray, int recursionLevel) {

		if (recursionLevel == g_MaximumRecursionDepth) {
			return new Vec(0, 0, 0);
		}

		Intersection i_rayIntersection = findIntersection(ray);

		// if no intersection we return background
		if (i_rayIntersection == null) {
			return g_BackgroundCol;
		}

		Vec i_colorVector = new Vec();

		// calculate Emission and Ambient color values
		i_colorVector.add(calculateEmissionColor(i_rayIntersection));

		i_colorVector.add(calculateAmbientColor(i_rayIntersection));

		Ray i_castRayTowardsLightSource = null;
		Point3D i_tempRayPositionHolder = null;
		Point3D i_tempRayIntersectionHolder = null;
		double i_distanceTowardsLightSource = 0;
		for (Light i_lightIterator : g_LightsList) {
			// act according the objects' instance
			if (i_lightIterator instanceof DirectionLight) {
				i_distanceTowardsLightSource = Double.MAX_VALUE;

				Vec i_rayDirection = ((DirectionLight) i_lightIterator).getDirection();
				// negate it
				i_rayDirection.negate();

				i_castRayTowardsLightSource = new Ray(i_rayIntersection.getPoint(), i_rayDirection);
			}
			if (i_lightIterator instanceof OmniLight) {
				i_tempRayPositionHolder = ((OmniLight) i_lightIterator).getPosition();
				i_tempRayIntersectionHolder = i_rayIntersection.getPoint();
				i_distanceTowardsLightSource = Point3D.distance(i_tempRayPositionHolder, i_tempRayIntersectionHolder);
				i_castRayTowardsLightSource = new Ray(i_tempRayIntersectionHolder, Point3D.vectorBetweenTwoPoints(i_tempRayPositionHolder,
						i_tempRayIntersectionHolder));
			}

			if (i_lightIterator instanceof SpotLight) {
				i_tempRayPositionHolder = ((SpotLight) i_lightIterator).getPosition();
				i_tempRayIntersectionHolder = i_rayIntersection.getPoint();

				i_distanceTowardsLightSource = Point3D.distance(i_tempRayPositionHolder, i_tempRayIntersectionHolder);
				i_castRayTowardsLightSource = new Ray(i_tempRayIntersectionHolder, Point3D.vectorBetweenTwoPoints(i_tempRayPositionHolder,
						i_tempRayIntersectionHolder));
			}

			Intersection i_RayObjectIntersection = findIntersection(i_castRayTowardsLightSource);

			if (i_RayObjectIntersection != null) {
				double i_distanceToIntersection = Point3D.distance(i_castRayTowardsLightSource.g_rayPoint, i_RayObjectIntersection.getPoint());
				if (i_distanceTowardsLightSource - 0.0001 > i_distanceToIntersection && i_distanceToIntersection > 0.001) {
					continue;
				}
			}

			// append calculated colors to the vector
			i_colorVector.add(calculateDiffuseColor(i_rayIntersection, i_lightIterator));
			i_colorVector.add(calculateSpecularColor(i_rayIntersection, i_lightIterator, ray));

		}

		Vec i_rayIntersectionNormal = i_rayIntersection.getSurface().getNormalAtPoint(i_rayIntersection.getPoint());
		Ray i_intersectionReflectionRay = new Ray(i_rayIntersection.getPoint(), ray.g_rayDirection.reflect(i_rayIntersectionNormal));

		double i_rayIntersectionReflectance = i_rayIntersection.getSurface().getReflectance();
		Vec i_intersectionReflectionColor = calcColor(i_intersectionReflectionRay, recursionLevel + 1);
		// append to our colors vector
		i_colorVector.add(Vec.scale(i_rayIntersectionReflectance, i_intersectionReflectionColor));

		return i_colorVector;
	}

	/**
	 * Calculate the amount of ambient color at the intersection point.
	 * 
	 * @param intersection
	 *            point
	 * @return ambient factor
	 */
	private Vec calculateAmbientColor(Intersection intersection) {
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
	private Vec calculateDiffuseColor(Intersection intersection, Light light) {

		Surface i_intersectingObject = intersection.getSurface();
		Point3D i_intersectionPoint = intersection.getPoint();

		// get intersection point's normal
		Vec i_normalAtIntersectionPoint = i_intersectingObject.getNormalAtPoint(i_intersectionPoint);

		// Calculate the vector between the intersection point and the light
		// source
		Vec i_light, i_lightIntensity = null;

		if (light instanceof DirectionLight) {
			DirectionLight i_directionalLight = (DirectionLight) light;

			i_light = i_directionalLight.getDirection();
			i_light.negate();
			if (i_intersectingObject instanceof Sphere)
			{
				i_light.negate();
			}
			i_lightIntensity = i_directionalLight.getLightIntensity(i_intersectionPoint);

		} else if (light instanceof OmniLight) {
			OmniLight i_omniLight = (OmniLight) light;
			i_light = Point3D.vectorBetweenTwoPoints(i_intersectionPoint, i_omniLight.getPosition());
			// we negate the light in case of Polygon
			if (i_intersectingObject instanceof Polygon) {
				i_light.negate();
			}
			i_lightIntensity = i_omniLight.getLightIntensity(i_intersectionPoint);
		} else {
			SpotLight i_spotLight = (SpotLight) light;
			i_light = Point3D.vectorBetweenTwoPoints(i_intersectionPoint, i_spotLight.getPosition());
			i_lightIntensity = i_spotLight.getLightIntensity(i_intersectionPoint);
		}
		// normalize result
		i_light.normalize();

		// Get dot product of light and intersection point normal
		double dotProduct = Vec.dotProd(i_normalAtIntersectionPoint, i_light);

		// get diffuse coefficient
		Vec i_surfaceDiffuseCoeff = i_intersectingObject.getDifuse();
		double i_dotProductAngel = Math.max(0, dotProduct);

		// return calculated ID
		return Vec.scale(i_surfaceDiffuseCoeff, Vec.scale(i_dotProductAngel, i_lightIntensity));

	}

	/**
	 * Calculate the amount of specular color at the intersection point, by the
	 * specified light source.
	 * 
	 * @param intersection
	 *            point
	 * @return specular color factor
	 */
	private Vec calculateSpecularColor(Intersection intersection, Light light, Ray ray) {

		Surface i_surfaceAtIntersection = intersection.getSurface();
		Point3D i_intersectionPoint = intersection.getPoint();

		// calculate intersection point normal
		Vec normalAtIntersectionPoint = i_surfaceAtIntersection.getNormalAtPoint(i_intersectionPoint);

		Vec i_light = null;
		Vec i_lightIntensity = null;

		if (light instanceof DirectionLight) {
			DirectionLight i_directionalLight = (DirectionLight) light;

			i_light = i_directionalLight.getDirection();
			i_light.negate();
			if (i_surfaceAtIntersection instanceof Sphere)
			{
				i_light.negate();
			}
			i_lightIntensity = i_directionalLight.getLightIntensity(i_intersectionPoint);


		} else if (light instanceof OmniLight) {

			OmniLight i_omniLight = (OmniLight) light;
			i_light = Point3D.vectorBetweenTwoPoints(i_intersectionPoint, i_omniLight.getPosition());
			i_lightIntensity = i_omniLight.getLightIntensity(i_intersectionPoint);

		} else {
			SpotLight i_spotLight = (SpotLight) light;
			i_light = Point3D.vectorBetweenTwoPoints(i_intersectionPoint, i_spotLight.getPosition());
			i_lightIntensity = i_spotLight.getLightIntensity(i_intersectionPoint);
		}

		i_light.normalize();

		// Reflect L with relation to N
		Vec i_R = i_light.reflect(normalAtIntersectionPoint);
		i_R.normalize();
		Vec i_userPOV = Point3D.vectorBetweenTwoPoints(ray.g_rayPoint, i_intersectionPoint);
		i_userPOV.normalize();
		// Calculate the dot product between R and users' point of view
		double i_userPOVandRdotProd = Math.max(0, Vec.dotProd(i_userPOV, i_R));

		// according to class we raise it to N
		double dotProductN = Math.pow(i_userPOVandRdotProd, i_surfaceAtIntersection.getShininess());

		Vec i_surfaceSpecularCoeff = i_surfaceAtIntersection.getSpecular();

		double i_scaleAngel = Math.max(dotProductN, 0);

		// Calculate and return IS
		return Vec.scale(i_surfaceSpecularCoeff, Vec.scale(i_scaleAngel, i_lightIntensity));

	}

	/**
	 * 
	 * @param intersection
	 * @return emission at intersection point
	 */
	private Vec calculateEmissionColor(Intersection intersection) {

		Surface i_intersectionSurface = intersection.getSurface();
		return i_intersectionSurface.getEmission();
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
		String i_name = name;
		i_name.toLowerCase();

		Surface i_surface = null;
		Light i_light = null;

		if (Light.isLight(i_name)) {
			// ALL OF THE LIGHTS!
			if ("dir-light".equals(i_name)) {
				i_light = new DirectionLight();
				i_light = new DirectionLight(attributes);
			} else if ("omni-light".equals(i_name)) {
				i_light = new OmniLight();
				i_light = new OmniLight(attributes);
			} else {
				i_light = new SpotLight();
				i_light = new SpotLight(attributes);
			}
			// append to lights list
			g_LightsList.add(i_light);
		} else {
			if ("sphere".equals(i_name)) {
				i_surface = new Sphere();
				i_surface = new Sphere(attributes);
			} else if ("disc".equals(i_name)) {
				i_surface = new Disc();
				i_surface = new Disc(attributes);
			} else {
				i_surface = new Polygon();
				i_surface = new Polygon(attributes);
			}
			// append to surface list
			g_SurfacesList.add(i_surface);
		}
	}

	/**
	 * sets a new camera with the inputed attributes
	 * 
	 * @param attributes
	 */
	public void setCameraAttributes(Map<String, String> attributes) {
		this.g_MainCamera = new Camera();
		this.g_MainCamera.init(attributes);
	}

	/**
	 * casts a ray through a pixel
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 * @return Ray object casted using the given data
	 */
	public Ray castRay(double x, double y, double height, double width) {
		return g_MainCamera.generatePixelPiercingRay(x, y, height, width);
	}
}
