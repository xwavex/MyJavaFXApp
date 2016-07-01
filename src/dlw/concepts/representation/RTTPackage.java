package dlw.concepts.representation;

import java.util.ArrayList;

public class RTTPackage {
	private String name;
	private String path;

	private ArrayList<RTTComponent> components;

	public RTTPackage() {
		components = new ArrayList<RTTComponent>();
	}

	public RTTPackage(String name, String path) {
		components = new ArrayList<RTTComponent>();
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void addComponent(RTTComponent comp) {
		components.add(comp);
	}

	public ArrayList<RTTComponent> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<RTTComponent> components) {
		this.components = components;
	}
}
