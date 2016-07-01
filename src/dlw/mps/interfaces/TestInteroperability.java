package dlw.mps.interfaces;

import java.util.ArrayList;

import dlw.concepts.representation.RTTComponent;
import dlw.concepts.representation.RTTComponentInstance;
import dlw.concepts.representation.RTTPackage;

public class TestInteroperability implements MpsInteroperability {

	@Override
	public ArrayList<RTTPackage> updateAvailableComponents() {
		// initialize stuff for the tree view
		ArrayList<RTTPackage> componentPackages = new ArrayList<RTTPackage>();
		// package 1:
		RTTPackage tmpPkg = new RTTPackage("rtt_gazebo_embedded", "/homes/dwigand/code/rtt-gazebo-embedded/orocos");
		tmpPkg.addComponent(new RTTComponent("RttGazeboEmbedded"));
		componentPackages.add(tmpPkg);
		// package 2:
		tmpPkg = new RTTPackage("rtt_robot_sim", "/homes/dwigand/code/rtt-robot-sim/orocos");
		tmpPkg.addComponent(new RTTComponent("RttRobotSim"));
		componentPackages.add(tmpPkg);
		// package 3:
		tmpPkg = new RTTPackage("rtt_core_extensions", "/homes/dwigand/code/rtt-core-extensions/orocos");
		tmpPkg.addComponent(new RTTComponent("RTTKinematicChainViewJa"));
		tmpPkg.addComponent(new RTTComponent("RTTKinematicChainViewJt"));
		componentPackages.add(tmpPkg);
		return componentPackages;
	}

	@Override
	public boolean insertComponentInstance(RTTComponentInstance compInst) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeComponentInstance(RTTComponentInstance compInst) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeComponentInstance(String compInstMpsId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RTTComponentInstance updateComponentInstance(RTTComponentInstance compInst) {
		// TODO Auto-generated method stub
		return null;
	}

}
