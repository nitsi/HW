package ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import math.Ray;
import math.Vec;
import ex3.parser.Element;
import ex3.parser.SceneDescriptor;
import ex3.render.IRenderer;

public class RayTracer implements IRenderer {

	private Scene scene;
	private int width;
	private int height;

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
		// you can initialize your scene object here
		this.height = height;
		this.width = width;
		scene = new Scene();
		scene.init(sceneDesc.getSceneAttributes());
		for (Element e : sceneDesc.getObjects()) {
			scene.addObjectByName(e.getName(), e.getAttributes());
		}

		scene.setCameraAttributes(sceneDesc.getCameraAttributes());

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

		for (int i = 0; i < width; i++) {
			if (i ==  width / 4  && line == height / 4 - 20 ) {
				System.out.println("haaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			}
			Ray ray = scene.castRay(i, line, height, width);
			Vec col = scene.calcColor(ray, 0);
			if (col.x > 1) {
				col.x = 1;
			}
			if (col.x < 0) {
				col.x = 0;
			}
			if (col.y > 1) {
				col.y = 1;
			}
			if (col.y < 0) {
				col.y = 0;
			}
			if (col.z > 1) {
				col.z = 1;
			}
			if (col.z < 0) {
				col.z = 0;
			}
			System.out.println("i: " + i + "line: " + line);
			Color realCol = new Color((int) (col.x * 255), (int) (col.y * 255),
					(int) (col.z * 255));
//			if (i ==  width / 4  && line == height / 4 - 20 ) {
//				realCol = new Color(255, 255, 255);
//			}
			canvas.setRGB(i, line, realCol.getRGB());
		}
	}

}
