/*
Computer Graphics - Exercise 3
Matan Gidnian	200846905
Aviad Hahami	302188347
*/

package proj.ex3.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a simple Element with attributes from the XML
 */
public class Element {

	public Element(String name) {
		this.name = name;
		attributes = new HashMap<String, String>();
	}

	protected String name;

	protected Map<String, String> attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
