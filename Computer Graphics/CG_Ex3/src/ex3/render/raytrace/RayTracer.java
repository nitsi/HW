/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */

package ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import ex3.parser.Element;
import ex3.parser.SceneDescriptor;
import ex3.render.IRenderer;

public class RayTracer implements IRenderer {

	private static final boolean DEBUG = false;

	private Scene g_currentScene;
	private int g_width;
	private int g_height;

	/**
	 * Instantiates the current renderer using scene description Will set the
	 * canvas to (Width x Height) dimensions.
	 * 
	 * @param sceneDescription
	 *            contains scene data
	 * @param width
	 *            Canvas width
	 * @param height
	 *            Canvas height
	 * @param filePath
	 * 
	 *            Path to scene's location. Should reference external files like
	 *            background
	 */

	@Override
	public void init(SceneDescriptor sceneDescription, int width, int height, File filePath) {
		// Initialize scene objects
		this.g_height = height;
		this.g_width = width;
		this.g_currentScene = new Scene();
		this.g_currentScene.init(sceneDescription.getSceneAttributes());

		for (Element sceneElement : sceneDescription.getObjects()) {
			g_currentScene.addObjectByName(sceneElement.getName(), sceneElement.getAttributes());
		}
		// Initialize camera data
		g_currentScene.setCameraAttributes(sceneDescription.getCameraAttributes());

	}

	/**
	 * Renders the given line to the given canvas. Invoked ONLY after this.init
	 * 
	 * @param canvas
	 *            BufferedImage containing the partial image
	 * @param line
	 *            The line of the image that should be rendered.
	 */
	@Override
	public void renderLine(BufferedImage canvas, int line) {
		int i_R, i_G, i_B;
		for (int i = 0; i < g_width; i++) {

			Ray i_RenderingRay = g_currentScene.castRay(i, line, g_height, g_width);
			Vec i_SceneCol = g_currentScene.calcColor(i_RenderingRay, 0);

			// verify X Y Z values for further usage

			// Logic : if col.t bigger than one, set as one, otherwise if
			// smaller than zero set as zero other wise keep current value

			i_SceneCol.x = i_SceneCol.x > 1 ? 1 : (i_SceneCol.x < 0 ? 0 : i_SceneCol.x);
			i_SceneCol.y = i_SceneCol.y > 1 ? 1 : (i_SceneCol.y < 0 ? 0 : i_SceneCol.y);
			i_SceneCol.z = i_SceneCol.z > 1 ? 1 : (i_SceneCol.z < 0 ? 0 : i_SceneCol.z);
			if (DEBUG) {
				System.out.println("i: " + i + "line: " + line);
			}
			// Calculate RGB values
			i_R = (int) (i_SceneCol.x * 255);
			i_G = (int) (i_SceneCol.y * 255);
			i_B = (int) (i_SceneCol.z * 255);

			Color i_CalculatedColor = new Color(i_R, i_G, i_B);

			// inject colors to canvas
			canvas.setRGB(i, line, i_CalculatedColor.getRGB());
		}
	}

}
