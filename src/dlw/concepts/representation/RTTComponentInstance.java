package dlw.concepts.representation;

public class RTTComponentInstance {

	String name;

	String mpsId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMpsId() {
		return mpsId;
	}

	public void setMpsId(String mpsId) {
		this.mpsId = mpsId;
	}

	public RTTComponent getBaseclass() {
		return baseclass;
	}

	public void setBaseclass(RTTComponent baseclass) {
		this.baseclass = baseclass;
	}

	RTTComponent baseclass;

	// TODO package backlink

	// TODO link to MPS concept class per mpsId

	public RTTComponentInstance(String name, RTTComponent baseclass) {
		this.name = name;
		this.baseclass = baseclass;
	}
}
