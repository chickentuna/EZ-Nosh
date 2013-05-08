package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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

import model.Amounts;
import model.Pair;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import control.Controller;
import events.IngredientsGeneratedEvents;
import events.RequestGenerateEvent;
import events.RequestIngedientsEvent;
import events.SuggestRecipesEvent;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;


public class Window {

	/** Constants **/
	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 10, 10, 10);
	private static final String L_LABEL_TITLE = "EZ-Nosh";

	/** Components **/
	private JFrame frame;
	private ImagePanel panel;
	
	/** Control **/
	private EventBus bus ;
	
	public Window(EventBus bus) throws IOException {
		this.bus = bus;
		initComponents();
		initActions();
		buildFrame();
	}

	private void buildFrame() throws IOException {

		panel.setPreferredSize(new Dimension(640, 480));
		panel.setLayout(new BorderLayout());
		{
			// SETUP cpane:top:center_panel:fc_panel
			JPanel north_panel = new JPanel();
			north_panel.setOpaque(false);
			north_panel.setLayout(new BoxLayout(north_panel, BoxLayout.Y_AXIS));
			{
				// SETUP cpane:top:center_panel:fc_panel:row0
				JPanel row_title = new ImagePanel("title.png");
				row_title.setOpaque(false);
				row_title.setLayout(new BoxLayout(row_title, BoxLayout.X_AXIS));
				row_title.setBorder(new EmptyBorder(0, 0, 0, 0));
				{
					JComponent row = row_title;
				}
				north_panel.add(row_title);

				// SETUP cpane:top:center_panel:fc_panel:row1
				JPanel row_filechooser = new JPanel();
				row_filechooser.setOpaque(false);
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
			south_panel.setOpaque(false);
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

	private void initComponents() throws IOException {
		frame = new javax.swing.JFrame();
		panel = new ImagePanel("lgreen.jpg", ImagePanel.STRETCH);
		
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
	
	@Subscribe
	public void on(SuggestRecipesEvent e) {
		System.out.println("Recipes suggested ");
		bus.post(new RequestIngedientsEvent(e.getList()));
	}
	
	@Subscribe
	public void on(IngredientsGeneratedEvents e) {
		String text = "<html>";
		Amounts a = e.getIngredients();
		Iterator<Pair<String, String>> it = a.keySet().iterator();
		while (it.hasNext()) {
			Pair<String, String> entry = it.next();
			text = text + parseEntry(entry, a.get(entry)) + "<br>";
		}
		//label_test.setText(text+"</html>");
	}

	private String parseEntry(Pair<String, String> entry, Float value) {
		return entry.getKey() + " : " + value + entry.getValue();  
	}
	
}