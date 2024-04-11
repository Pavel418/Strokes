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
import java.util.ArrayList;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

public class Canvas extends JComponent {
	private int X1, Y1, X2, Y2;
	private Graphics2D g;
	private Image img;
	private final Stack<Image> history = new Stack<>();
	private final Stack<Image> undoStack = new Stack<>();
	private final Stack<Image> redoStack = new Stack<>();
	private Shape shape;
	private MouseMotionListener motion;
	private MouseListener listener;

	public Canvas() {
		setBackground(Color.WHITE);
		defaultListener();
	}

	protected void paintComponent(Graphics g1) {
		if (img == null) {
			img = createImage(getSize().width, getSize().height);
			g = (Graphics2D) img.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			clear();
		}
		g1.drawImage(img, 0, 0, null);
	}

	public void defaultListener() {
		setDoubleBuffered(false);
		listener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				saveToStack(img);
				addImage(img);
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

	public void clear() {
		g.setPaint(Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setPaint(Color.black);
		repaint();
	}

	public void undo() {
		if (!undoStack.isEmpty()) {
			Image undoTemp = undoStack.pop();
			redoStack.push(img);
			setImage(undoTemp);
		}
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			Image redoTemp = redoStack.pop();
			undoStack.push(img);
			setImage(redoTemp);
		}
	}

	public void pencil() {
		removeMouseListener(listener);
		removeMouseMotionListener(motion);
		defaultListener();
	}

	public void rect() {
		setShapeListener();
		shape = new Rectangle();
	}

	public void circle() {
		setShapeListener();
		shape = new Oval();
	}

	public void rightTriangle() {
		setShapeListener();
		shape = new RightTriangle();
	}

	public void triangle() {
		setShapeListener();
		shape = new Triangle();
	}

	public void line() {
		setShapeListener();
		shape = new Line();
	}

	public void diamond() {
		setShapeListener();
		shape = new Diamond();
	}

	public void pentagon() {
		setShapeListener();
		shape = new Pentagon();
	}

	public void arrow() {
		setShapeListener();
		shape = new Arrow();
	}

	private void setShapeListener() {
		removeMouseListener(listener);
		removeMouseMotionListener(motion);
		MyMouseListener ml = new MyMouseListener();
		addMouseListener(ml);
		addMouseMotionListener(ml);
		listener = ml;
		motion = ml;
	}

	private void addImage(Image img) {
		history.push(copyImage(img));
	}

	private void setImage(Image img) {
		g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.img = img;

		repaint();
	}

	private BufferedImage copyImage(Image img) {
		BufferedImage copyOfImage = new BufferedImage(getSize().width,
				getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics g = copyOfImage.createGraphics();
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		return copyOfImage;
	}

	private void saveToStack(Image img) {
		undoStack.push(copyImage(img));
	}

	public void setThickness(int thick) {
		g.setStroke(new BasicStroke(thick));
	}

	public void save(File file) {
		try {
			ImageIO.write((RenderedImage) img, "PNG", file);
		} catch (IOException ignored) {
		}
	}

	public void load(File file) {
		try {
			img = ImageIO.read(file);
			g = (Graphics2D) img.getGraphics();
			repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setColor(Color color) {
		g.setPaint(color);
	}

	public void picker(Color color) {
		g.setPaint(color);
	}

	class MyMouseListener extends MouseInputAdapter {
		boolean dragging = false;
		public void mousePressed(MouseEvent e) {
			saveToStack(img);
			addImage(img);
			Point startPoint = e.getPoint();
			shape.setPosition(startPoint);
			shape.resize(startPoint);
		}

		public void mouseDragged(MouseEvent e) {
			shape.resize(e.getPoint());
			if (!dragging) {
				dragging = true;
			} else {
				undo();
			}
			shape.draw(g);
			addImage(img);
			saveToStack(img);
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			shape.resize(e.getPoint());
			shape.draw(g);
			repaint();
		}
	}
}
