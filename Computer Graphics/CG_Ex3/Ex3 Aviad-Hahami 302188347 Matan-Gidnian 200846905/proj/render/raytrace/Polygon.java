/*
 Computer Graphics - Exercise 3
 Matan Gidnian	200846905
 Aviad Hahami	302188347
 */

package ex3.render.raytrace;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Polygon extends Surface, represents general polygon in space
 * 
 *
 */
public class Polygon extends Surface {

	private static final String INVALID_POLYGON_SIZE = "Invalid polygon size!";
	private int g_PolygonSize;
	private Point3D[] g_PolygonPointsArray;

	public Polygon() {
	}

	/**
	 * constructs polygon
	 * 
	 * @param attributes
	 */
	public Polygon(Map<String, String> attributes) {
		g_PolygonSize = 0;
		originInit(attributes);
		init(attributes);
	}

	public int getSize() {
		return g_PolygonSize;
	}

	/**
	 * 
	 * @param index
	 * @return polygon point (Point3D) @ a given index
	 */
	public Point3D getPolygonPointAtIndex(int index) {
		// verify index is in bounds
		return (index >= 0 || index <= g_PolygonSize - 1) ? g_PolygonPointsArray[index] : null;
	}

	@Override
	public void init(Map<String, String> attributes) throws IllegalArgumentException {

		Map<String, String> i_PolygonPointsMap = new HashMap<String, String>();

		// Purify points data from XML crap
		for (Map.Entry<String, String> mapEntry : attributes.entrySet()) {
			if (mapEntry.getKey().startsWith("p")) {
				i_PolygonPointsMap.put(mapEntry.getKey(), mapEntry.getValue());
			}
		}
		// set the size according to the table
		g_PolygonSize = i_PolygonPointsMap.size();

		// Verify we're not going anywhere bad
		if (g_PolygonSize <= 2) {
			throw new IllegalArgumentException(INVALID_POLYGON_SIZE);
		}

		g_PolygonPointsArray = new Point3D[g_PolygonSize];

		// Sort the points in the map using RB trees(TreeMap implementation)
		Map<String, String> i_PolygonPointsSortedMap = new TreeMap<String, String>(i_PolygonPointsMap);

		// Generate the points array with the polygon size data
		g_PolygonPointsArray = new Point3D[g_PolygonSize];

		// Insert data from the map to array
		String i_pointIndexString;
		for (int i = 0; i < g_PolygonSize; i++) {
			i_pointIndexString = "p" + i;
			g_PolygonPointsArray[i] = new Point3D(i_PolygonPointsSortedMap.get(i_pointIndexString));
		}

	}

	@Override
	public Vec getNormalAtPoint(Point3D point) {

		Vec i_v1 = Point3D.vectorBetweenTwoPoints(this.g_PolygonPointsArray[0], this.g_PolygonPointsArray[1]);
		Vec i_v2 = Point3D.vectorBetweenTwoPoints(this.g_PolygonPointsArray[0], this.g_PolygonPointsArray[2]);
		Vec i_calculatedNormal = Vec.crossProd(i_v1, i_v2);
		i_calculatedNormal.normalize();
		return i_calculatedNormal;
	}

}
