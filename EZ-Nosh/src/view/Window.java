package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.google.common.eventbus.EventBus;

import control.Controller;


public class Window {

	/** Constants **/
	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(
			0, 10, 10, 10);
	private static final String L_LABEL_TITLE = "EZ-Nosh";

	/** Components **/
	private JFrame frame;
	private JPanel panel;
	
	/** Control **/
	private EventBus bus ;
	
	public Window(EventBus bus) {
		this.bus = bus;
		initComponents();
		initActions();
		buildFrame();

	}

	private void buildFrame() {

		panel.setPreferredSize(new Dimension(640, 480));
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BorderLayout());
		{
			// SETUP cpane:top:center_panel:fc_panel
			JPanel north_panel = new JPanel();
			north_panel.setLayout(new BoxLayout(north_panel, BoxLayout.Y_AXIS));
			{
				// SETUP cpane:top:center_panel:fc_panel:row0
				JPanel row_title = new JPanel();
				row_title.setLayout(new BoxLayout(row_title, BoxLayout.X_AXIS));
				row_title.setBorder(EMPTY_BORDER);
				{
					JComponent row = row_title;
				}
				north_panel.add(row_title);

				// SETUP cpane:top:center_panel:fc_panel:row1
				JPanel row_filechooser = new JPanel();
				row_filechooser.setLayout(new BoxLayout(row_filechooser,
						BoxLayout.X_AXIS));
				row_filechooser.setBorder(EMPTY_BORDER);
				{
					JComponent row = row_filechooser;
				}
				north_panel.add(row_filechooser);
			}
			panel.add(north_panel, BorderLayout.NORTH);

			JPanel south_panel = new JPanel();
			south_panel.setLayout(new BorderLayout());

			panel.add(south_panel, BorderLayout.CENTER);
		}

		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("EZ-Nosh");

	}

	private void initComponents() {
		frame = new javax.swing.JFrame();
		panel = new JPanel();
		
	}

	private void registerToEnter(JButton button, int condition) {

		button.registerKeyboardAction(button.getActionForKeyStroke(KeyStroke
				.getKeyStroke(KeyEvent.VK_SPACE, 0, false)), KeyStroke
				.getKeyStroke(KeyEvent.VK_ENTER, 0, false), condition);
		button.registerKeyboardAction(button.getActionForKeyStroke(KeyStroke
				.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), KeyStroke
				.getKeyStroke(KeyEvent.VK_ENTER, 0, true), condition);
	}

	private void initActions() {

	}
}