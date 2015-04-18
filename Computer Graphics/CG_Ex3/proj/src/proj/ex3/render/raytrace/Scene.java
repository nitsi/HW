/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import proj.ex3.math.*;



/**
 * A Scene class containing all the scene objects including camera, lights and
 * surfaces.
 * 
 */
public class Scene implements IInitable {
	
	private static final double ACCELERATION = 0;
	private static final double SUPPER_SAMP_WIDTH = 1;

	protected Vec backgroundCol = null;
	protected String backgroundTex = null;
	protected double maxRecursionLevel;
	protected Vec ambientLight = null;
	
	protected double superSampWidth;
	protected double useAcceleration;

	protected List<Surface> surfaces;
	protected List<Light> lights;
	protected Camera camera;
	protected BufferedImage backgroundImage;


	/**
	 * constructor
	 */
	public Scene() {
		this.backgroundCol = new Vec(0, 0, 0);
		this.maxRecursionLevel = 10;
		this.ambientLight = new Vec(0, 0, 0);
		
		this.surfaces = new LinkedList<Surface>();
		this.lights = new LinkedList<Light>();
		this.camera = new Camera();
		
		this.backgroundTex = null;
		this.backgroundImage = null;
		
		this.superSampWidth = SUPPER_SAMP_WIDTH;
		this.useAcceleration = ACCELERATION;
	}

	@Override
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("background-col")) {
			this.backgroundCol = new Vec(attributes.get("background-col"));
		}
		if (attributes.containsKey("background-tex")) {
			this.backgroundTex = attributes.get("background-tex");
		}
		if (attributes.containsKey("ambient-light")) {
			this.ambientLight = new Vec(attributes.get("ambient-light"));
		}
		if (attributes.containsKey("super-samp-width")){
			this.superSampWidth = 
			Double.valueOf(attributes.get("super-samp-width"));
		}
		if (attributes.containsKey("max-recursion-level")) {
			this.maxRecursionLevel =
			Double.valueOf(attributes.get("max-recursion-level"));
		}
		if (attributes.containsKey("use-acceleration")){
			this.useAcceleration = 
			Double.valueOf(attributes.get("use-acceleration"));
		}
	}
	
	/**
	 * Send ray return the nearest intersection. Return null if no intersection
	 * 
	 * @param ray
	 * @return
	 */
	public Hit findIntersection(Ray ray, boolean backSide) {

		double minDistance = Double.POSITIVE_INFINITY;
		Surface minSurface = null;
		for (Surface surface : surfaces) {
			double d = surface.nearestIntersection(ray, backSide);
			if (minDistance > d && d > 0.0001) {
				minDistance = d;
				minSurface = surface;
			}
		}

		if (Double.isInfinite(minDistance))
			return null;

		Point3D intersection = new Point3D(ray.p);
		intersection.mac(minDistance, ray.v);

		return new Hit(intersection, minSurface, minDistance);
	}
	
	/**
	 * loads background image
	 * @param backgroundImagePath
	 * @param width
	 * @param height
	 */
	public void getBackgroundTexture(File backgroundImagePath, int width, int height) {
		try {
			this.backgroundImage = ImageIO.read
					(new File(backgroundImagePath.getParent() + 
							File.separatorChar + backgroundTex));
			AffineTransform transformer = new AffineTransform();

			double transformedWidth = 
					(double) width / backgroundImage.getWidth();
			double transformedHeight = 
					(double) height / backgroundImage.getHeight();
			transformer.scale(transformedWidth, transformedHeight);

			AffineTransformOp transformOperation = 
					new AffineTransformOp(transformer, 
							AffineTransformOp.TYPE_BILINEAR);
			backgroundImage = transformOperation.filter(backgroundImage, null);

		} catch (IOException e) {
			System.out.println("Problem with background image.");
		}
	}

	/**
	 * Calculates the color at a given intersection point
	 * 
	 * @param hit
	 *            The intersection point and surface
	 * @param ray
	 *            Hitting ray
	 * @return
	 */
	public Vec calcColor(Hit hit, Ray ray, int level, double backgroundX, 
			double backgroundY) {

		Ray outRay;
		Hit newHit;
		Vec recursionResult;

		// no hit
		if (hit == null) {
			Color backColor = getBackgroundColorAt((int)backgroundX, 
					(int)backgroundY);
			double red = (double)backColor.getRed() / 255;
			double green = (double)backColor.getGreen() / 255;
			double blue = (double)backColor.getBlue() / 255;
			return new Vec(red, green, blue);
		}

		// recursion reached its end
		if (level == this.maxRecursionLevel) {
			return new Vec();
		}

		Vec I = new Vec(); // I from the equation - final result
		Material material = hit.surface.material; // material
		Vec Ie = material.emission; // material emission
		Vec Ia = this.ambientLight; // global ambient
		Vec Ka = material.ambient; // material ambient
		Vec Ks = material.specular; // material specular
		double Kr = material.reflectance; // material reflection
		double n = material.shininess; // material shininess
		Point3D intersection = hit.intersection;
		Vec N; // intersection normal
		Vec V; // intersection to eye
		Vec lightSigmaResult;

		// I = Ie + Ka * Ia
		I.add(Vec.add(Ie, Vec.scale(Ka, Ia)));

		// lights sigma: I += sigma
		N = hit.surface.normalAt(intersection, ray);
		V = ray.v.clone();		
		lightSigmaResult = calcSumOfLights(hit, Ks, N, V, n);
		I.add(lightSigmaResult);

		// recursion
		Vec outVec = ray.v.reflect(N);
		outVec.normalize();
		outRay = new Ray(intersection, outVec);
		newHit = findIntersection(outRay, false);
		recursionResult = 
				calcColor(newHit, outRay, level + 1, backgroundX, backgroundY);
		I.mac(Kr, recursionResult);
		
		return I;
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
		if (name == "sphere") {
			Surface surface = new Sphere();
			surface.init(attributes);
			this.surfaces.add(surface);
		}
		
		if(name == "trimesh"){
			Trimesh trimesh = new Trimesh();
			trimesh.init(attributes);
			LinkedList<Triangle> trianglesList = trimesh.getTriangles();
			for(Triangle triangle: trianglesList){
				this.surfaces.add(triangle);
			}
		}
		
		if (name == "disc") {
			Disc disc = new Disc();
			disc.init(attributes);
			this.surfaces.add(disc);
		}
		
		if(name == "omni-light"){
			omniLight light = new omniLight();			
			light.init(attributes);
			this.lights.add(light);
		}
		
		if(name == "spot-light"){
			SpotLight light = new SpotLight();			
			light.init(attributes);
			this.lights.add(light);
		}
		
		if(name == "dir-light"){
			dirLight light = new dirLight();			
			light.init(attributes);
			this.lights.add(light);
		}
		
	}
	
	// sigma: (Kd(N*L) + Ks(V*R)^n )Sl*Il
	private Vec calcSumOfLights(Hit hit,Vec Ks, Vec N, Vec V, double n) {
		
		Vec sumOfLights = new Vec();
		
		for (int i = 0 ; i < lights.size() ; i++) {			
			
			Light light = lights.get(i);			
			
			//Vector to Light 
			Vec L = Vec.sub(light.pos, hit.intersection);
			double Lightdistance = L.length();
			L.normalize(); 
			
			//check if shadowed
			Ray shadowedRay = new Ray(hit.intersection,L);
			Hit shadowedHit = findIntersection(shadowedRay, true);
			//if the point is not shadowed
			if (shadowedHit == null || 
					shadowedHit.distance >= Lightdistance){			
				//diffuse
				Vec Kd = new Vec();
				//calc. teta in order to find the right diffuse
				double teta = Vec.dotProd(N, L);
				if(teta > 0){
					Surface surface = hit.surface;
					//if the surface is rectangle we need to check
					//diffuse at a specific point for checkers type surface   
					Kd = Vec.scale(teta, surface.material.diffuse);
					
				}
				//Specular
				Vec R = L.reflect(N);
				V.normalize();
				double VR = Vec.dotProd(V, R);
				if (VR >= 0) {
					Vec specular = Vec.scale(Math.pow(VR , n), Ks);
					Kd.add(specular);
				}
				Vec vecFromPoint =
						new Vec(hit.intersection.x
								, hit.intersection.y, hit.intersection.z);
				Kd.scale(light.getLightAtPoint(vecFromPoint));
				sumOfLights.add(Kd);
			}			
		}
		
		return sumOfLights;			
	}

	/**
	 * returns the color of the background at a wanted point
	 * @param x
	 * @param y
	 * @return
	 */
	protected Color getBackgroundColorAt(int x, int y) {

		if (this.backgroundImage != null) {
			return new Color(this.backgroundImage.getRGB(x, y));
		}

		return new Color((int)(this.backgroundCol.x * 255),
				(int)(this.backgroundCol.y * 255),
				(int)(this.backgroundCol.z * 255));
	}
	// Well, same comment from upper part.
	// We probably don't need it the way we implemented, but we will check if we can delete this
//	public void setCameraAttributes(Map<String, String> attributes) {
//		//TODO uncomment after implementing camera interface if you like
//		this.camera.init(attributes);
//	}
}
