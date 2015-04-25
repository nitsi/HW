/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */
package ex3.render.raytrace;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Polygon extends Surface {

	private static final String WRONG_POLYGON_SIZE = "Wrong polygon size!";

	private int g_polygonSize;
	private Point3D[] g_PointsArray;

	/**
	 * empty constructor
	 */
	public Polygon() {
	}

	/**
	 * constructor using XML attributes
	 * 
	 * @param attributes
	 */
	public Polygon(Map<String, String> attributes) {
		g_polygonSize = 0;
		originInit(attributes);
		init(attributes);
	}

	/**
	 * retrieves a point at given index from points array
	 * 
	 * @param index
	 * @return
	 */
	public Point3D getPointAtLocation(int index) {
		// verify we won't get array out of bounds
		return (index < 0 || index + 1 > g_polygonSize) ? null : g_PointsArray[index];
	}

	/**
	 * 
	 * @return this polygon's size
	 */
	public int getSize() {
		return g_polygonSize;
	}

	@Override
	public void init(Map<String, String> attributes) {
		// Verify polygon is in legitimate size
		if (verifyPolygonSize()) {

			Map<String, String> i_PolygonPointsMap = new HashMap<String, String>();

			// Retrieve the points from XML
			for (Map.Entry<String, String> mapEntry : attributes.entrySet()) {
				if (mapEntry.getKey().startsWith("p")) {
					i_PolygonPointsMap.put(mapEntry.getKey(), mapEntry.getValue());
				}
			}
			// set the size
			g_polygonSize = i_PolygonPointsMap.size();

			// Sort points using RB trees (tree map implementation)
			Map<String, String> i_SortedPolygonPointsMap = new TreeMap<String, String>(i_PolygonPointsMap);

			// Initialize the points array
			g_PointsArray = new Point3D[g_polygonSize];

			// Populate the points array
			String i_pointString = null;
			for (int i = 0; i < g_polygonSize; i++) {
				i_pointString = "p" + i;
				g_PointsArray[i] = new Point3D(i_SortedPolygonPointsMap.get(i_pointString));
			}
		}

	}

	/**
	 * tests whether the polygon size is smaller or equal to 2, if so we invoke
	 * exception
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
	private boolean verifyPolygonSize() throws IllegalArgumentException {
		if (g_polygonSize <= 2) {
			throw new IllegalArgumentException(WRONG_POLYGON_SIZE);
		}
		return true;
	}

	@Override
	public Vec getNormalAtLocation(Point3D p) {
		// we ignore the given point (p) in this implementation
		
		// calculate the normal
		Vec i_polygonGeneralNormal = Vec.crossProd(Point3D.vectorBetweenTwoPoints(this.g_PointsArray[0], this.g_PointsArray[1]),
				Point3D.vectorBetweenTwoPoints(this.g_PointsArray[0], this.g_PointsArray[2]));
		i_polygonGeneralNormal.normalize();
		return i_polygonGeneralNormal;
	}

}
