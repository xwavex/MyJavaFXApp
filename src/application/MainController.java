package application;

import java.util.ArrayList;
import java.util.ResourceBundle;

import dlw.concepts.representation.RTTComponent;
import dlw.concepts.representation.RTTComponentInstance;
import dlw.concepts.representation.RTTPackage;
import dlw.mps.interfaces.MpsInteroperability;
import dlw.shapes.repo.ComponentSceneNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainController implements Initializable {

	public ScrollPane rootpane;
	public StackPane rootstackpane;
	public TreeView<String> componenttree;
	Pane plusGrid;

	private MpsInteroperability mpsIF;

	private ArrayList<RTTPackage> componentPackages;

	private Node nodeAttachedToMouse;

	private double lastMouseX = 0;
	private double lastMouseY = 0;

	public MainController(MpsInteroperability mpsif) throws NullPointerException {
		if (mpsif == null) {
			throw new NullPointerException("(MpsInteroperability) mpsif is null!");
		}
		mpsIF = mpsif;
	}

	public MpsInteroperability getMpsIF() {
		return mpsIF;
	}

	public void setMpsIF(MpsInteroperability mpsIF) throws NullPointerException {
		if (mpsIF == null) {
			throw new NullPointerException("(MpsInteroperability) mpsIF is null!");
		}
		this.mpsIF = mpsIF;
	}

	public void loadPackageButtonClicked() {
		// test create tree

		TreeItem<String> rootTreeObject = new TreeItem<String>();
		componenttree.setRoot(rootTreeObject);

		componenttree.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				Dragboard db = componenttree.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(componenttree.getSelectionModel().selectedItemProperty().getValue().getValue());
				db.setContent(content);
				e.consume();
				componenttree.getStyleClass().add("drag-item");
			}
		});

		componenttree.setOnDragDone(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent e) {
				e.consume();
				componenttree.getStyleClass().remove("drag-item");
			}
		});

		componenttree.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case ESCAPE:
					nodeAttachedToMouse = null;
					break;
				default:
					break;
				}
			}
		});

		componenttree.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					TreeItem<String> item = componenttree.getSelectionModel().getSelectedItem();
					if (item.isLeaf()) {
						System.out.println("Selected Text : " + item.getValue());

						// TODO
						// generate component preview
						String packageName = item.parentProperty().getValue().getValue();
						String componentClassName = item.getValue();
						RTTComponent candidateComp = null;
						// find component:
						// TODO for the future, do this with ids!
						for (RTTPackage rttPackage : componentPackages) {
							if (rttPackage.getName().equals(packageName)) {
								for (RTTComponent rttcomponent : rttPackage.getComponents()) {
									if (rttcomponent.getClassName().equals(componentClassName)) {
										candidateComp = rttcomponent;
										break;
									}
								}
							}
						}

						if (candidateComp == null) {
							// error component not found! - shouldnt happen tho!
							return;
						}

						// spawn component
						ComponentSceneNode compSceneNode = new ComponentSceneNode(
								new RTTComponentInstance(candidateComp.getClassName() + "1", candidateComp));
						compSceneNode.setOnMousePressed(componentSceneNodeOnMousePressedEventHandler);
						compSceneNode.setOnMouseDragged(componentSceneNodeOnMouseDraggedEventHandler);
						nodeAttachedToMouse = compSceneNode;

					} else {
						if (nodeAttachedToMouse != null) {
							nodeAttachedToMouse = null;
						}
					}
				}
			}
		});

		componentPackages = mpsIF.updateAvailableComponents();
		if (componentPackages == null) {
			rootTreeObject.setValue("Components could not be loaded...");
			componenttree.setShowRoot(true);
			return;
		}

		componenttree.setShowRoot(false);

		TreeItem<String> pkgItemTmp = makeTreeItem(componentPackages.get(0).getName(), rootTreeObject);
		makeTreeItem(componentPackages.get(0).getComponents().get(0).getClassName(), pkgItemTmp);

		pkgItemTmp = makeTreeItem(componentPackages.get(1).getName(), rootTreeObject);
		makeTreeItem(componentPackages.get(1).getComponents().get(0).getClassName(), pkgItemTmp);

		pkgItemTmp = makeTreeItem(componentPackages.get(2).getName(), rootTreeObject);
		makeTreeItem(componentPackages.get(2).getComponents().get(0).getClassName(), pkgItemTmp);
		makeTreeItem(componentPackages.get(2).getComponents().get(1).getClassName(), pkgItemTmp);
	}

	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		// Add listener to set ScrollPane FitToWidth/FitToHeight when viewport
		// bounds changes
		rootpane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				Node content = rootpane.getContent();
				rootpane.setFitToWidth(content.prefWidth(-1) < newValue.getWidth());
				rootpane.setFitToHeight(content.prefHeight(-1) < newValue.getHeight());
			}
		});

		rootpane.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent e) {
				if (e.getGestureSource() != rootpane && e.getDragboard().hasString()) {
					e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				e.consume();

				lastMouseX = e.getX();
				lastMouseY = e.getY();
			}
		});

		rootpane.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent e) {
				boolean success = false;
				if (e.getDragboard().hasString()) {
					plusGrid.setOpacity(0);
					success = true;
				}
				e.setDropCompleted(success);
				e.consume();

				lastMouseX = e.getX();
				lastMouseY = e.getY();
			}
		});

		rootpane.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (nodeAttachedToMouse != null) {
					if (nodeAttachedToMouse instanceof ComponentSceneNode) {
						ComponentSceneNode componentSN = (ComponentSceneNode) nodeAttachedToMouse;

						double width_half = rootpane.getWidth() * 0.5;
						double height_half = rootpane.getHeight() * 0.5;

						componentSN.setTranslateX(event.getX() - width_half);
						componentSN.setTranslateY(event.getY() - height_half);

						lastMouseX = event.getX();
						lastMouseY = event.getY();
					}
					rootstackpane.getChildren().add(nodeAttachedToMouse);
				}
			}
		});

		rootpane.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (nodeAttachedToMouse != null) {

					nodeAttachedToMouse
							.setTranslateX(nodeAttachedToMouse.getTranslateX() + (event.getX() - lastMouseX));
					nodeAttachedToMouse
							.setTranslateY(nodeAttachedToMouse.getTranslateY() + (event.getY() - lastMouseY));

					lastMouseX = event.getX();
					lastMouseY = event.getY();
				}
			}
		});

		rootpane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 1) {
					// if a node is currently attached to the mouse.
					if (nodeAttachedToMouse != null) {
						nodeAttachedToMouse = null;
					}
				}
			}
		});

		rootpane.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (nodeAttachedToMouse != null) {
					rootstackpane.getChildren().remove(nodeAttachedToMouse);
				}
			}
		});

		rootpane.setFitToWidth(true);
		rootpane.setFitToHeight(true);

		plusGrid = new PlusGrid();
		rootstackpane.getChildren().add(plusGrid);

		System.out.println("Init the SampleController!");
	}

	private TreeItem<String> makeTreeItem(String pkgname, TreeItem<String> parent) {
		TreeItem<String> newItem = new TreeItem<String>(pkgname);
		newItem.setExpanded(true);
		if (parent != null)
			parent.getChildren().add(newItem);
		return newItem;
	}

	public void sceneOnKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
		case ESCAPE:
			System.out.println("ESC Pressed!");
			nodeAttachedToMouse = null;
			// remove component from mouse
			break;
		default:
			break;
		}
	}


	// TODO auslagern!
	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;


	private EventHandler<MouseEvent> componentSceneNodeOnMousePressedEventHandler =
        new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((ComponentSceneNode)(t.getSource())).getTranslateX();
            orgTranslateY = ((ComponentSceneNode)(t.getSource())).getTranslateY();

            ((ComponentSceneNode)(t.getSource())).toFront();
        }
    };

    private EventHandler<MouseEvent> componentSceneNodeOnMouseDraggedEventHandler =
        new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            ((ComponentSceneNode)(t.getSource())).setTranslateX(newTranslateX);
            ((ComponentSceneNode)(t.getSource())).setTranslateY(newTranslateY);
        }
    };

}
