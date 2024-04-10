import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Draw {

	private Canvas canvas;
	private Color color = Color.WHITE;
	private JLabel filenameBar, thicknessStat;
	private JSlider thicknessSlider;
	private int width, height;
	private JButton clearButton, blackButton, blueButton, greenButton, redButton,
			colorPicker, magentaButton, grayButton, orangeButton, yellowButton,
			pinkButton, cyanButton, lightGrayButton, saveButton, loadButton,
			saveAsButton, pencilButton, undoButton, redoButton;
	private File file;
	private int saveCounter = 0;
	private final Map<String, Icon> icons = new HashMap<>();

	/**
	 * Sets the Nimbus look and feel for the application.
	 * This method tries to find the Nimbus look and feel among the installed look and feels
	 * and sets it as the current look and feel for the application.
	 * If the Nimbus look and feel is not found or an exception occurs during the process,
	 * an exception stack trace is printed.
	 */
	public static void setNimbusFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Draw() {
		loadIcons();
	}

	/**
	 * Loads the icons used in the application.
	 * The icons are retrieved from the "/assets" directory.
	 * Each icon is associated with a specific key in the "icons" map.
	 */
	private void loadIcons() {
		icons.put("save", new ImageIcon(getClass().getResource("/assets/save.png")));
		icons.put("undo", new ImageIcon(getClass().getResource("/assets/undo.png")));
		icons.put("redo", new ImageIcon(getClass().getResource("/assets/redo.png")));
		icons.put("pencil", new ImageIcon(getClass().getResource("/assets/pencil.png")));
		icons.put("rect", new ImageIcon(getClass().getResource("/assets/rect.png")));
		icons.put("circle", new ImageIcon(getClass().getResource("/assets/circle.png")));
		icons.put("rightTriangle", new ImageIcon(getClass().getResource("/assets/right_triangle.png")));
		icons.put("line", new ImageIcon(getClass().getResource("/assets/line.png")));
		icons.put("triangle", new ImageIcon(getClass().getResource("/assets/triangle.png")));
		icons.put("diamond", new ImageIcon(getClass().getResource("/assets/diamond.png")));
		icons.put("arrow", new ImageIcon(getClass().getResource("/assets/arrow.png")));
		icons.put("pentagon", new ImageIcon(getClass().getResource("/assets/pentagon.png")));
	}

	private ChangeListener thicknessListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			thicknessStat.setText(String.format("%s",
					thicknessSlider.getValue()));
			canvas.setThickness(thicknessSlider.getValue());
		}
	};
	
	/**
	 * The ActionListener interface represents an object that can receive action events.
	 * 
	 * An action event occurs when a user interacts with a GUI component, such as clicking a button.
	 * 
	 * Classes that implement this interface must provide an implementation for the actionPerformed method,
	 * which is called when an action event occurs.
	 */
	private ActionListener listener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			JFileChooser fileChooser;
			if (event.getSource() == clearButton) {
				canvas.clear();
			} else if (event.getActionCommand() == "color") {
				JButton button = (JButton) event.getSource();
				canvas.setColor(button.getBackground());
			} else if (event.getSource() == undoButton) {
				canvas.undo();
			} else if (event.getSource() == redoButton) {
				canvas.redo();
			} else if (event.getActionCommand() == "rect") {
				canvas.rect();
			} else if (event.getActionCommand() == "circle") {
				canvas.circle();
			} else if (event.getActionCommand() == "rightTriangle") {
				canvas.rightTriangle();
			} else if (event.getActionCommand() == "triangle") {
				canvas.triangle();
			} else if (event.getActionCommand() == "line") {
				canvas.line();
			} else if (event.getActionCommand() == "diamond") {
				canvas.diamond();
			} else if (event.getActionCommand() == "arrow") {
				canvas.arrow();
			} else if (event.getActionCommand() == "pentagon") {
				canvas.pentagon();
			} else if (event.getSource() == pencilButton) {
				canvas.pencil();
			} else if (event.getSource() == saveButton) {
				if (saveCounter == 0) {
					fileChooser = new JFileChooser();
					if (fileChooser.showSaveDialog(saveButton) == JFileChooser.APPROVE_OPTION) {
						file = fileChooser.getSelectedFile();
						saveCounter = 1;
						filenameBar.setText(file.toString());
						canvas.save(file);
					}
				} else {
					filenameBar.setText(file.toString());
					canvas.save(file);
				}
			} else if (event.getSource() == saveAsButton) {
				saveCounter = 1;
				fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(saveAsButton) == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					filenameBar.setText(file.toString());
					canvas.save(file);
				}
			} else if (event.getSource() == loadButton) {
				fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(loadButton) == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					filenameBar.setText(file.toString());
					canvas.load(file);
				}
			} else if (event.getSource() == colorPicker) {
				color = JColorChooser.showDialog(null, "Pick your color!",
						color);
				if (color == null)
					color = (Color.WHITE);
				canvas.picker(color);
			}
		}
	};

	/**
	 * Sets the dimensions of the window.
	 *
	 * @param width  the width of the window
	 * @param height the height of the window
	 */
	public void setWindowDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Creates a JButton with the specified action command and icon.
	 *
	 * @param actionCommand the action command for the button
	 * @param icon the icon for the button
	 * @return the created JButton
	 */
	private JButton createLeftbarButton(String actionCommand, Icon icon) {
		JButton button = new JButton(icon);
		button.setPreferredSize(new Dimension(40, 40));
		button.setActionCommand(actionCommand);
		button.addActionListener(listener);
		return button;
	}

	/**
	 * Creates a JButton with the specified background color.
	 * 
	 * @param color the background color of the button
	 * @return the created JButton
	 */
	private JButton createColorButton(Color color) {
		JButton button = new JButton();
		button.setBackground(color);
		button.setPreferredSize(new Dimension(40, 40));
		button.setActionCommand("color");
		button.addActionListener(listener);
		return button;
	}

	/**
	 * Adds shape buttons to the given box.
	 * 
	 * @param box The box to which the shape buttons will be added.
	 */
	private void addShapeButtons(Box box) {
		for (Map.Entry<String, Icon> entry : this.icons.entrySet()) {
			if (entry.getKey().equals("save") || entry.getKey().equals("undo") || entry.getKey().equals("redo")
					|| entry.getKey().equals("pencil")) {
				continue;
			}

			JButton button = createLeftbarButton(entry.getKey(), entry.getValue());
			box.add(button, BorderLayout.NORTH);
			box.add(Box.createVerticalStrut(5));
		}
	}

	/**
	 * Opens the Paint application window.
	 * Sets up the user interface components, such as buttons, sliders, and canvas.
	 * Initializes the JFrame, container, and panels.
	 * Adds event listeners to the buttons and sliders.
	 * Sets the size and title of the JFrame.
	 * Displays the JFrame and makes it visible to the user.
	 */
	public void openPaint() {
		Main.setNimbusFeel();
		JFrame frame = new JFrame("Paint (" + width + "X" + height + ")");
		Container container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		canvas = new Canvas();

		container.add(canvas, BorderLayout.CENTER);

		JPanel panel = new JPanel();

		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());

		Box box = Box.createVerticalBox();
		Box box1 = Box.createHorizontalBox();

		thicknessSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 1);
		thicknessSlider.setMajorTickSpacing(25);
		thicknessSlider.setPaintTicks(true);
		thicknessSlider.setPreferredSize(new Dimension(40, 40));
		thicknessSlider.addChangeListener(thicknessListener);

		pencilButton = createLeftbarButton("pencil", icons.get("pencil"));
		undoButton = createLeftbarButton("undo", icons.get("undo"));
		redoButton = createLeftbarButton("redo", icons.get("redo"));

		ArrayList<JButton> colorButtons = new ArrayList<>();
		blackButton = createColorButton(Color.BLACK);
		blueButton = createColorButton(Color.BLUE);
		greenButton = createColorButton(Color.GREEN);
		redButton = createColorButton(Color.RED);
		magentaButton = createColorButton(Color.MAGENTA);
		grayButton = createColorButton(Color.GRAY);
		orangeButton = createColorButton(Color.ORANGE);
		yellowButton = createColorButton(Color.YELLOW);
		pinkButton = createColorButton(Color.PINK);
		cyanButton = createColorButton(Color.CYAN);
		lightGrayButton = createColorButton(Color.LIGHT_GRAY);
		colorButtons.addAll(
				java.util.Arrays.asList(
						blackButton, blueButton, greenButton, redButton, magentaButton,
						grayButton, orangeButton, yellowButton, pinkButton, cyanButton,
						lightGrayButton));

		saveButton = new JButton(icons.get("save"));
		saveButton.addActionListener(listener);
		saveAsButton = new JButton("Save As");
		saveAsButton.addActionListener(listener);
		loadButton = new JButton("Load");
		loadButton.addActionListener(listener);
		colorPicker = new JButton("Color Picker");
		colorPicker.addActionListener(listener);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(listener);

		filenameBar = new JLabel("No file");
		thicknessStat = new JLabel("1");

		box.add(Box.createVerticalStrut(40));
		box1.add(thicknessSlider, BorderLayout.NORTH);
		box1.add(thicknessStat, BorderLayout.NORTH);
		box.add(box1, BorderLayout.NORTH);
		panel1.add(filenameBar, BorderLayout.SOUTH);
		box.add(Box.createVerticalStrut(20));
		box.add(undoButton, BorderLayout.NORTH);
		box.add(Box.createVerticalStrut(5));
		box.add(redoButton, BorderLayout.NORTH);
		box.add(Box.createVerticalStrut(5));
		box.add(pencilButton, BorderLayout.NORTH);
		box.add(Box.createVerticalStrut(5));

		addShapeButtons(box);

		for (JButton colorButton : colorButtons) {
			panel.add(colorButton);
		}

		panel.add(saveButton);
		panel.add(saveAsButton);
		panel.add(loadButton);
		panel.add(colorPicker);
		panel.add(clearButton);

		container.add(panel, BorderLayout.NORTH);
		container.add(panel1, BorderLayout.SOUTH);
		container.add(box, BorderLayout.WEST);

		frame.setVisible(true);
		frame.setSize(width + 79, height + 11);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
