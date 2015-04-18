/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render;

import proj.ex3.render.raytrace.RayTracer;

/**
 * Returns the default renderer
 */
public class RendererFactory {

	/**
	 * Instantiates a new renderer and returns it
	 * 
	 * @return
	 */
	public static IRenderer newInstance() {

		return new RayTracer();
	}
}