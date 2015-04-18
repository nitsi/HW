/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.render.raytrace;

import java.util.LinkedList;
import java.util.Map;

public class Trimesh implements IInitable {
	
	protected final static String TRIANGLE_PREFIX = "tri";
	private LinkedList<Triangle> triangleList = new LinkedList<Triangle>();
	

	@Override
	public void init(Map<String, String> attributes) {
		
		int curTriangleIndex = 0;
		String curTriangleIndexString = String.valueOf(curTriangleIndex);
		String currentTriangle = TRIANGLE_PREFIX + curTriangleIndexString;			
		String curTriangleCordsString = attributes.get(currentTriangle);
		
		if(curTriangleCordsString == null){
			curTriangleIndex++;
			curTriangleIndexString = String.valueOf(curTriangleIndex);
			currentTriangle = TRIANGLE_PREFIX + curTriangleIndexString;				
			curTriangleCordsString = attributes.get(currentTriangle);
		}
		
		while(curTriangleCordsString != null){				
			Triangle triangle = new Triangle(curTriangleIndex);
			triangle.init(attributes);
			triangleList.add(triangle);			

			curTriangleIndex++;
			curTriangleIndexString = String.valueOf(curTriangleIndex);
			currentTriangle = TRIANGLE_PREFIX + curTriangleIndexString;
			curTriangleCordsString = attributes.get(currentTriangle);
		}		
	}
	
	/**
	 * Returns the triangles list held in this trimesh
	 * @return Returns the triangles list held in this trimesh.
	 */
	public LinkedList<Triangle> getTriangles(){
		return this.triangleList;
	}

}
