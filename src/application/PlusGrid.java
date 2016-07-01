package application;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A node that draws a grid of + using canvas
 */
public class PlusGrid extends Pane {
	private double spacing_x = 80;
	private double spacing_y = 80;
	private Canvas canvas = new Canvas();
	private Font myFont;
	private FontMetrics metrics;

	public PlusGrid() {
		init();
	}

	public void init() {
		myFont = Font.loadFont(getClass().getResource("Quicksand-Regular.otf").toExternalForm(), 12.0);
		metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(myFont);
		getChildren().add(canvas);
	}

	public PlusGrid(double spacingX, double spacingY) {
		spacing_x = spacingX;
		spacing_y = spacingY;
		init();
	}

	@Override
	protected void layoutChildren() {
		final int top = (int) snappedTopInset();
		final int right = (int) snappedRightInset();
		final int bottom = (int) snappedBottomInset();
		final int left = (int) snappedLeftInset();
		final int w = (int) getWidth() - left - right;
		final int h = (int) getHeight() - top - bottom;
		canvas.setLayoutX(left);
		canvas.setLayoutY(top);
		float plusWidth = metrics.computeStringWidth("+");

		if (w != canvas.getWidth() || h != canvas.getHeight()) {
			canvas.setWidth(w);
			canvas.setHeight(h);
			GraphicsContext g = canvas.getGraphicsContext2D();
			g.setFont(myFont);
			g.clearRect(0, 0, w, h);

			g.setStroke(Color.web("#1c1c1c", 0.1));
			double linespacing_x = spacing_x / 5;
			double linespacing_y = spacing_y / 5;

			for (int x = 0; x <= w; x += linespacing_x) {
				for (int y = 0; y < h; y += linespacing_y) {
					g.strokeLine(0, y + 0.5, w, y + 0.5);
					g.strokeLine(x + 0.5, 0, x + 0.5, h);
				}
			}

			g.setStroke(Color.gray(0.35, 0.7));
			for (int x = 0; x <= w; x += spacing_x) {
				for (int y = 0; y < h; y += spacing_y) {
					g.strokeText("+", x - plusWidth * 0.5, y + metrics.getXheight() * 0.75);
				}
			}
		}
	}
}