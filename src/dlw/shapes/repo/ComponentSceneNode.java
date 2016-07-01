package dlw.shapes.repo;

import dlw.concepts.representation.RTTComponentInstance;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ComponentSceneNode extends Group {

	private RTTComponentInstance instRef;

	public ComponentSceneNode(RTTComponentInstance instRef) {
		this.setInstRef(instRef);

		Text a = new Text(getInstRef().getName());
		a.setStroke(Color.WHITE);
		getChildren().add(a);
	}

	public RTTComponentInstance getInstRef() {
		return instRef;
	}

	public void setInstRef(RTTComponentInstance instRef) {
		this.instRef = instRef;
	}


}
