package application;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import dlw.mps.interfaces.MpsInteroperability;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class JFxWrapper {

	private Scene scene;
	private MpsInteroperability mpsIF;

    public JFxWrapper(Container container, String htmlCode, Object controller, MpsInteroperability mpsif) throws IOException, NullPointerException {
        Platform.setImplicitExit(false);
        System.out.println("GETTING STARTED");
        JFXPanel jfxPanel = new JFXPanel();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));

        if (mpsif == null) {
        	throw new NullPointerException("(MpsInteroperability) mpsif is null!");
        }

        mpsIF = mpsif;
        MainController c = new MainController(mpsIF);
        loader.setController(c);

        Parent root = (Parent)loader.load();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                c.sceneOnKeyPressed(event);
            }
        });



        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("CREATING ANIMATION");

                jfxPanel.setScene(scene);
            }
        };


        if(Platform.isFxApplicationThread()) {
            System.out.println("IN FX APP THREAD");
            runnable.run();
        } else {
            FutureTask<Object> task = new FutureTask<>((Callable<Object>) () -> {
                runnable.run();
                return null;
            });
            System.out.println("RUNNING THE TASK LATER");
            Platform.runLater(task);
            try {
                task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        container.setLayout(new BorderLayout());
        container.add(jfxPanel, BorderLayout.CENTER);

        System.out.println("Exit Constructor");
    }

	public MpsInteroperability getMpsIF() {
		return mpsIF;
	}

	public void setMpsIF(MpsInteroperability mpsIF) {
		this.mpsIF = mpsIF;
	}
}