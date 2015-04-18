/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import proj.ex3.render.raytrace.Hit;

import proj.ex3.math.Ray;
import proj.ex3.parser.Element;
import proj.ex3.render.raytrace.Scene;

import proj.ex3.parser.SceneDescriptor;
import proj.ex3.render.IRenderer;

public class RayTracer implements IRenderer {


	protected int width;
	protected int height;
	protected Scene scene;

	/**
	 * Inits the renderer with scene description and sets the target canvas to
	 * size (width X height). After init renderLine may be called
	 * 
	 * @param sceneDesc
	 *            Description data structure of the scene
	 * @param width
	 *            Width of the canvas
	 * @param height
	 *            Height of the canvas
	 * @param path
	 *            File path to the location of the scene. Should be used as a
	 *            basis to load external resources (e.g. background image)
	 */
	@Override
	public void init(SceneDescriptor sceneDesc, int width, int height, File path) {
		
		this.width = width;
		this.height = height;
		
		this.scene = new Scene();
		this.scene.init(sceneDesc.getSceneAttributes());

		this.scene.camera.init(sceneDesc.getCameraAttributes());

		for (Element e : sceneDesc.getObjects()) {
			this.scene.addObjectByName(e.getName(), e.getAttributes());
		}
		
		// Original code, we sill have to see how we implement missing parts as we did the code our
		// way... so we should check if we can just drop it.
//		BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//		for (int i = 0; i < height; i++) {
//			renderLine(canvas, i);
//		}
//		scene.setCameraAttributes(sceneDesc.getCameraAttributes());

		// loads the background image if needed
		if (scene.backgroundTex != null) {
			scene.getBackgroundTexture(path, width, height);
		}
	}	

	/**
	 * Renders the given line to the given canvas. Canvas is of the exact size
	 * given to init. This method must be called only after init.
	 * 
	 * @param canvas
	 *            BufferedImage containing the partial image
	 * @param line
	 *            The line of the image that should be rendered.
	 */
	@Override
	public void renderLine(BufferedImage canvas, int line) {
		int y = line;
		for (int x = 0; x < width; ++x) {
			if (scene.superSampWidth == 1)
				canvas.setRGB(x, y, 
						castRay(x, height - y - 1, canvas).getRGB());
			else
				canvas.setRGB(x, y, 
						castRaySupSamp(x, height - y - 1, canvas).getRGB());
		}
	}

	/**
	 * Super sampling ray tracing. - Bonus
	 * @param x current x coordinate
	 * @param y current y coordinate
	 * @param canvas current scene canvas
	 * @return the Color in position (x,y).
	 */
	private Color castRaySupSamp(int x, int y, BufferedImage canvas) {
		double sampleWidth = scene.superSampWidth;
		double sampleHeight = scene.superSampWidth;
		double pixIndexI = -(sampleWidth/2);
		double pixIndexJ = -(sampleHeight/2);
		
		Color colorAtPix;
		int green = 0;
		int red = 0;
		int blue = 0;
		
		// Calculate where to construct the ray.
		for (;pixIndexI <= sampleWidth/2; pixIndexI++) {
			for (;pixIndexJ <= sampleHeight/2; pixIndexJ++) {
				if (pixIndexI != 0 && pixIndexJ !=0) {
					double newX = x + (1/sampleWidth)*pixIndexI;
					double newY = y + (1/sampleWidth)*pixIndexJ;
					Ray ray = scene.camera.constructRayThroughPixel
							(newX, newY, canvas.getWidth(),
									canvas.getHeight());
					
					Hit hit = scene.findIntersection(ray, false);

					if (hit == null) {
						colorAtPix = scene.getBackgroundColorAt
								((int)newX, (int)(height - newY - 1));
					} else {
						colorAtPix = scene.calcColor
								(hit, ray, 0, newX, 
										height - newY - 1).toColor();
					}

					blue += colorAtPix.getBlue();
					red += colorAtPix.getRed();
					green += colorAtPix.getGreen();
				}
			}
		}
		return new Color((int)(red / scene.superSampWidth), 
				(int)(green / scene.superSampWidth), 
				(int)(blue / scene.superSampWidth));
	}

	/**
	 * Regular ray tracing
	 * @param x current x coordinate
	 * @param y current y coordinate
	 * @param canvas current scene canvas
	 * @return the Color in position (x,y).
	 */
	private Color castRay(int x, int y, BufferedImage canvas) {
		Ray ray = scene.camera.constructRayThroughPixel
				(x, y, canvas.getWidth(), canvas.getHeight());
		Hit hit = scene.findIntersection(ray, false);
		if (hit == null) {
			return scene.getBackgroundColorAt(x, height - y - 1);
		}
		return scene.calcColor(hit, ray, 0, x, height - y - 1).toColor();
	}
}