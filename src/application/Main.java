package application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dlw.mps.interfaces.TestInteroperability;

public class Main extends JApplet {

	private JFxWrapper wrapper;


	@Override
	public void init() {
		JPanel jp = new JPanel(new BorderLayout());
		jp.setPreferredSize(new Dimension(1000, 600));
		try {
			TestInteroperability t = new TestInteroperability();
			wrapper = new JFxWrapper(jp, "", null, t);
		} catch (IOException e) {
			e.printStackTrace();
		}
		add(jp, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (Exception e) {
				}

				JFrame frame = new JFrame("Swing JTable");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JApplet applet = new Main();
				applet.init();

				frame.setContentPane(applet.getContentPane());

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);

				applet.start();
			}
		});
	}
}
