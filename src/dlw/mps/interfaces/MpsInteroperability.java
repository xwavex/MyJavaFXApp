package dlw.mps.interfaces;

import java.util.ArrayList;

import dlw.concepts.representation.RTTComponentInstance;
import dlw.concepts.representation.RTTPackage;

public interface MpsInteroperability {

	public ArrayList<RTTPackage> updateAvailableComponents();

	public boolean insertComponentInstance(RTTComponentInstance compInst);

	public boolean removeComponentInstance(RTTComponentInstance compInst);

	public boolean removeComponentInstance(String compInstMpsId);

	public RTTComponentInstance updateComponentInstance(RTTComponentInstance compInst);

}
