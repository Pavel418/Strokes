import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

/**
 * The class represents the canvas to draw on.
 */
public class Canvas extends JComponent {
	private int X1, Y1, X2, Y2;
	private Graphics2D g;
	private Image img;
	private final SizedStack<Image> undoStack = new SizedStack<>(30);
	private final SizedStack<Image> redoStack = new SizedStack<>(30);
	private Shape shape;
	private MouseMotionListener motion;
	private MouseListener listener;

	public Canvas() {
		setBackground(Color.WHITE);
		defaultListener();
	}

	/**
	 * This method is used to paint the component.
	 * @param g1 the graphics context
	 */
	protected void paintComponent(Graphics g1) {
		if (img == null) {
			img = createImage(getSize().width, getSize().height);
			g = (Graphics2D) img.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			clear();
		}
		g1.drawImage(img, 0, 0, null);
		if (shape != null) {
			Graphics2D g2 = (Graphics2D) g1;
			g2.setStroke(g.getStroke());
			g2.setColor(g.getColor());
			shape.draw(g2);
		}
	}

	/**
	 * This method is used to set the default listener for the canvas to draw with a pencil.
	 */
	public void defaultListener() {
		setDoubleBuffered(false);
		listener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				saveToStack(copyImage(img));
				X2 = e.getX();
				Y2 = e.getY();
			}
		};

		motion = new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				X1 = e.getX();
				Y1 = e.getY();

				if (g != null) {
					g.drawLine(X2, Y2, X1, Y1);
					repaint();
					X2 = X1;
					Y2 = Y1;
				}
			}
		};
		addMouseListener(listener);
		addMouseMotionListener(motion);
	}

	/**
	 * This method is used to clear the canvas.
	 */
	public void clear() {
		g.setPaint(Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setPaint(Color.black);
		repaint();
	}

	/**
	 * This method is used to undo the last action by displaying the previous image.
	 */
	public void undo() {
		if (!undoStack.isEmpty()) {
			Image undoTemp = undoStack.pop();
			redoStack.push(copyImage(img));
			setImage(undoTemp);
		}
	}

	/**
	 * This method is used to redo the last action by displaying the next image.
	 */
	public void redo() {
		if (!redoStack.isEmpty()) {
			Image redoTemp = redoStack.pop();
			undoStack.push(copyImage(img));
			setImage(redoTemp);
		}
	}

	/**
	 * This method is used to draw with a pencil.
	 */
	public void pencil() {
		removeMouseListener(listener);
		removeMouseMotionListener(motion);
		defaultListener();
	}

	/**
	 * This method is used to draw a rectangle.
	 */
	public void rect() {
		setShapeListener();
		shape = new Rectangle();
	}

	/**
	 * This method is used to draw a circle.
	 */
	public void circle() {
		setShapeListener();
		shape = new Oval();
	}

	/**
	 * This method is used to draw a right triangle.
	 */
	public void rightTriangle() {
		setShapeListener();
		shape = new RightTriangle();
	}

	/**
	 * This method is used to draw a triangle.
	 */
	public void triangle() {
		setShapeListener();
		shape = new Triangle();
	}

	/**
	 * This method is used to draw a line.
	 */
	public void line() {
		setShapeListener();
		shape = new Line();
	}

	/**
	 * This method is used to draw a star.
	 */
	public void diamond() {
		setShapeListener();
		shape = new Diamond();
	}

	/**
	 * This method is used to draw a hexagon.
	 */
	public void pentagon() {
		setShapeListener();
		shape = new Pentagon();
	}

	/**
	 * This method is used to draw an arrow.
	 */
	public void arrow() {
		setShapeListener();
		shape = new Arrow();
	}

	/**
	 * This method is used to set the shape listener when drawing shapes.
	 */
	private void setShapeListener() {
		removeMouseListener(listener);
		removeMouseMotionListener(motion);
		ShapeListener ml = new ShapeListener();
		addMouseListener(ml);
		addMouseMotionListener(ml);
		listener = ml;
		motion = ml;
	}

	/**
	 * This method is used to set the current image to draw on.
	 * @param img the image to set
	 */
	private void setImage(Image img) {
		this.img = img;
		g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke());
		g.setColor(Color.BLACK);
		repaint();
	}

	/**
	 * This method is used to copy the image tp prevent the original image from being modified.
	 * @param img the image to copy
	 * @return the copied image
	 */
	private BufferedImage copyImage(Image img) {
		BufferedImage copyOfImage = new BufferedImage(getSize().width,
				getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics g = copyOfImage.createGraphics();
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		return copyOfImage;
	}

	/**
	 * This method is used to save the image to the stack.
	 * @param img the image to save
	 */
	private void saveToStack(Image img) {
		undoStack.push(copyImage(img));
	}

	/**
	 * This method is used to set the thickness of the shape.
	 * @param thickness the thickness of the shape
	 */
	public void setThickness(int thickness) {
		g.setStroke(new BasicStroke(thickness));
	}

	/**
	 * This method is used to save the image to the file.
	 * @param file the file to save the image to
	 */
	public void save(File file) {
		try {
			ImageIO.write((RenderedImage) img, "PNG", file);
		} catch (IOException e) {
			System.err.println("Error saving image to file: " + file.getPath());
			System.err.println("Exception message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to load the image from the file.
	 * @param file the file to load the image from
	 */
	public void load(File file) {
		try {
			img = ImageIO.read(file);
			g = (Graphics2D) img.getGraphics();
			repaint();
		} catch (IOException e) {
			System.err.println("Error loading image from file: " + file.getPath());
			System.err.println("Exception message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to pick the color of the shape.
	 * @param color the color of the shape
	 */
	public void setColor(Color color) {
		g.setPaint(color);
	}

	/**
	 * The class handles the drawing of shapes on the canvas.	 */
	class ShapeListener extends MouseInputAdapter {
		/**
		 * Invoked when a mouse button has been pressed on a component.
		 * @param e the event to be processed
		 */
		public void mousePressed(MouseEvent e) {
			Point startPoint = e.getPoint();
			shape.setPosition(startPoint);
			shape.resize(startPoint);
			saveToStack(img);
		}

		/**
		 * Invoked when a mouse button is pressed on a component and then dragged.
		 * @param e the event to be processed
		 */
		public void mouseDragged(MouseEvent e) {
			shape.resize(e.getPoint());
			repaint();
		}

		/**
		 * Invoked when a mouse button has been released on a component.
		 * @param e the event to be processed
		 */
		public void mouseReleased(MouseEvent e) {
			shape.resize(e.getPoint());
			shape.draw(g);
			shape.start = shape.end;
			repaint();
		}
	}
}
