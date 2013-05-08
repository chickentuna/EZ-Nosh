package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class Window {

	/** Constants **/
	private static final String TITLE = "EZ-Nosh";
	private static final Dimension FIELD_DIMENSION = new Dimension(100, 20);
	private static final Dimension BOX_DIMENSION = new Dimension(20, 0);
	private static final int FONT_SIZE = 18;

	/** Components **/
	private JFrame frame;
	private ImagePanel panel;

	/** Control **/
	private EventBus bus;

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
				//row_title.setAlignmentX(Component.CENTER_ALIGNMENT);
				//north_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				north_panel
						.add(new Box.Filler(null, new Dimension(0, 10), null));
				north_panel.add(row_title);
				north_panel
						.add(new Box.Filler(null, new Dimension(0, 30), null));
			}
			panel.add(north_panel, BorderLayout.NORTH);

			JPanel south_panel = new JPanel();
			{
				south_panel.setLayout(new BorderLayout());
				south_panel.setOpaque(false);

				JPanel panel_1 = new JPanel();
				//panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

				addField(panel_1, "Nombres de repas à prévoir : ", 25);
				panel_1.add(new Box.Filler(null, null, new Dimension(0, 10)));
				addField(20, panel_1, "Rapide :");
				addField(20, panel_1, "Picnic :");
				addField(20, panel_1, "Chic :");
				addField(20, panel_1, "Desserts :");

				ImagePanel tomato = new ImagePanel("tomato.png");
				tomato.setAlignmentX(Component.LEFT_ALIGNMENT);
				panel_1.add(tomato);

				panel_1.setOpaque(false);
				//south_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
				south_panel.add(panel_1, BorderLayout.CENTER);
			}
			panel.add(south_panel, BorderLayout.CENTER);
			JPanel east_panel = new JPanel();
			{
				east_panel.setOpaque(false);
				
				JPanel orange = new ImagePanel("orange.jpg", ImagePanel.STRETCH);
				
				east_panel.add(orange);
			}
			panel.add(east_panel, BorderLayout.EAST);
			
		}

		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle(TITLE);

	}

	private void addField(int offset, JPanel panel_1, String string) {
		addField(offset, panel_1, string, FONT_SIZE);

	}

	private void addField(JPanel panel_1, String string, int size) {
		addField(6, panel_1, string, size);
	}

	private void addField(int offset, JPanel panel_1, String string, int size) {
		JPanel panel_3 = new JPanel();
		{
			panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
			panel_3.setOpaque(false);
			JLabel label = new JLabel(string);
			label.setFont(new Font("Serif", Font.PLAIN, size));
			//label.setAlignmentX(Component.LEFT_ALIGNMENT);
			panel_3.add(new Box.Filler(null, null, new Dimension(offset, 0)));
			panel_3.add(label);
			panel_3.add(new Box.Filler(null, null, BOX_DIMENSION));
			JTextField field = new JTextField("0");
			field.setMaximumSize(FIELD_DIMENSION);
			panel_3.add(field);
		}
		panel_3.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel_1.add(panel_3);
		panel_1.add(new Box.Filler(null, null, new Dimension(0, 10)));
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
		// label_test.setText(text+"</html>");
	}

	private String parseEntry(Pair<String, String> entry, Float value) {
		return entry.getKey() + " : " + value + entry.getValue();
	}

}