import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * The class represents the main class of the program.
 */
public class Main {
	public int width;
	public int height;
	Draw draw = new Draw();

	/**
	 * Constructor for Main class. It initializes the application by displaying the
	 * input dialog.
	 */
	public Main() {
		showInput();
	}

	/**
	 * Displays an input dialog to prompt the user for the canvas dimensions.
	 * It checks that dimensions meet a minimum size requirement and handles invalid
	 * inputs.
	 * If the user inputs values below the minimum required size or invalid formats,
	 * defaults are set and the paint window is opened.
	 */
	private void showInput() {
		setNimbusFeel();
		JPanel p = new JPanel(new BorderLayout(5, 5));
		JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
		JPanel labels1 = new JPanel(new FlowLayout());
		labels.add(new JLabel("Width", SwingConstants.RIGHT));
		labels.add(new JLabel("Height", SwingConstants.RIGHT));
		labels1.add(new JLabel("Minimum Width:900, Height: 800"));
		p.add(labels, BorderLayout.WEST);
		p.add(labels1, BorderLayout.SOUTH);

		JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
		JTextField widthField = new JTextField();
		controls.add(widthField);
		JTextField heightField = new JTextField();
		heightField.addAncestorListener(new RequestFocusListener(false));
		controls.add(heightField);
		p.add(controls, BorderLayout.CENTER);
		JOptionPane.showMessageDialog(null, p, "Enter Canvas Width and Height",
				JOptionPane.QUESTION_MESSAGE);
		try {
			width = Integer.parseInt(widthField.getText());
			height = Integer.parseInt(heightField.getText());
			if (width < 900 || height < 800) {
				JOptionPane.showMessageDialog(null, p,
						"W:900,H:800 Minimum required", JOptionPane.ERROR_MESSAGE);
			}
			draw.setWindowDimensions(width, height);
			draw.openPaint();
		} catch (IllegalArgumentException e) {
			draw.setWindowDimensions(1000, 1000);
			draw.openPaint();
		}
	}

	/**
	 * Sets the application's look and feel to Nimbus if available.
	 */
	public static void setNimbusFeel() {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}

	/**
	 * The main method which is the entry point for the application. It ensures that
	 * the application
	 * is started within the AWT event dispatch thread.
	 *
	 * @param args Command line arguments, not used in this application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});
	}

}

/**
 * Listener class that requests focus on a component when it is added to a
 * container.
 * This is particularly useful for ensuring that a text field is ready for input
 * as soon as it is displayed.
 */
class RequestFocusListener implements AncestorListener {
	private boolean removeListener;

	public RequestFocusListener() {
		this(true);
	}

	/**
	 * Constructs a RequestFocusListener.
	 * 
	 * @param removeListener If true, the listener will be removed after the
	 *                       component gains focus.
	 */
	public RequestFocusListener(boolean removeListener) {
		this.removeListener = removeListener;
	}

	/**
	 * Called when a component is added to the hierarchy and possibly becomes
	 * visible.
	 * Requests focus on the component and removes the listener if specified.
	 * 
	 * @param e the AncestorEvent
	 */
	@Override
	public void ancestorAdded(AncestorEvent e) {
		JComponent component = e.getComponent();
		component.requestFocusInWindow();

		if (removeListener)
			component.removeAncestorListener(this);
	}

	@Override
	public void ancestorMoved(AncestorEvent e) {
	}

	@Override
	public void ancestorRemoved(AncestorEvent e) {
	}
}
