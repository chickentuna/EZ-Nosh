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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import model.Amounts;
import model.Pair;
import model.Recipe;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.IngredientsGeneratedEvents;
import events.RequestGenerateEvent;
import events.RequestIngedientsEvent;
import events.SuggestRecipesEvent;
import javax.swing.border.LineBorder;


public class Window {

	/** Constants **/
	private static final String TITLE = "EZ-Nosh";
	private static final Dimension FIELD_DIMENSION = new Dimension(100, 20);
	private static final Dimension BOX_DIMENSION = new Dimension(20, 0);
	private static final int FONT_SIZE = 18;

	/** Components **/
	private JFrame frame;
	private ImagePanel panel;
	private LinkedList<JSpinner> input;
	private JButton button_gen;
	JPanel recipe_area;
	
	/** Control **/
	private EventBus bus;
	private JLabel recipe_text;
	private JButton b_valider;
	private JButton b_choose;
	
	public Window(EventBus bus) throws IOException {
		this.bus = bus;
		initComponents();
		buildFrame();
		initActions();
	}

	private void buildFrame() throws IOException {

		panel.setPreferredSize(new Dimension(640, 540));
		panel.setLayout(new BorderLayout());
		{
			
			JPanel north_panel = new JPanel();
			north_panel.setOpaque(false);
			north_panel.setLayout(new BoxLayout(north_panel, BoxLayout.Y_AXIS));
			{
				
				JPanel row_title = new ImagePanel("title.png");
				row_title.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				north_panel
						.add(new Box.Filler(null, new Dimension(0, 10), null));
				north_panel.add(row_title);
				north_panel
						.add(new Box.Filler(null, new Dimension(0, 30), null));
				addField(north_panel, "Nombres de repas à prévoir : ", 25);
			}
			panel.add(north_panel, BorderLayout.NORTH);

			JPanel center_panel = new JPanel();
			{
				center_panel.setLayout(new BorderLayout());
				center_panel.setOpaque(false);

				JPanel panel_1 = new JPanel();
				//panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

				
				panel_1.add(new Box.Filler(null, null, new Dimension(0, 10)));
				addField(20, panel_1, "Rapide :");
				addField(20, panel_1, "À emporter :");
				addField(20, panel_1, "Chic :");
				addField(20, panel_1, "Desserts :");

				JPanel panel_tomato = new JPanel(); 
				{
					panel_tomato.setLayout(new BoxLayout(panel_tomato, BoxLayout.X_AXIS));
					panel_tomato.setOpaque(false);
					ImagePanel tomato = new ImagePanel("tomato.png");
					tomato.setAlignmentX(Component.LEFT_ALIGNMENT);
					//panel_tomato.setAlignmentX(Component.LEFT_ALIGNMENT);
					panel_tomato.add(new Box.Filler(null, null, BOX_DIMENSION));
					panel_tomato.add(tomato);
					panel_tomato.setAlignmentX(Component.LEFT_ALIGNMENT);
				}
				panel_1.add(panel_tomato);
				panel_1.setOpaque(false);
				//south_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
				center_panel.add(panel_1, BorderLayout.NORTH);
				
				button_gen = new JButton("Générer");
				registerToEnter(button_gen, JComponent.WHEN_IN_FOCUSED_WINDOW);
				button_gen.setFont(new Font("Arial", Font.BOLD,24));
				button_gen.setMaximumSize(new Dimension(200,200));
				center_panel.add(button_gen, BorderLayout.CENTER);

			}
			panel.add(center_panel, BorderLayout.CENTER);
			JPanel east_panel = new JPanel();
			east_panel.setLayout(new BorderLayout());
			{
				east_panel.setOpaque(false);
				
				recipe_area = new JPanel();//TextArea(20,34);
				{
					recipe_area.setPreferredSize(new Dimension(380,0));
					//recipe_area.setOpaque(false);
					recipe_area.setBorder(new LineBorder(Color.ORANGE, 4));
					recipe_area.setBackground(new Color(255, 152, 83));
					recipe_area.setLayout(new BoxLayout(recipe_area, BoxLayout.Y_AXIS));
					recipe_text = new JLabel();
					recipe_area.add(recipe_text);

					
				}
				east_panel.add(recipe_area, BorderLayout.CENTER);
				JPanel b_panel = new JPanel();
				{
					b_panel.setLayout(new FlowLayout());
					b_valider = new JButton("Valider");
					b_choose = new JButton("Choisir");
					b_panel.add(b_valider);
					b_panel.add(b_choose);
					b_panel.setOpaque(false);
				}
				east_panel.add(b_panel, BorderLayout.SOUTH);
				
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

	private void initComponents() throws IOException {
		frame = new javax.swing.JFrame();
		panel = new ImagePanel("lgreen.jpg", ImagePanel.STRETCH);
		input = new LinkedList<JSpinner>(); 
	
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
			JSpinner field = new JSpinner(new SpinnerNumberModel(0,0,99,1));
			input.add(field);
			field.setMaximumSize(FIELD_DIMENSION);
			panel_3.add(field);
		}
		panel_3.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel_1.add(panel_3);
		panel_1.add(new Box.Filler(null, null, new Dimension(0, 10)));
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
		button_gen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int t = (int) input.get(0).getValue();
				int s = (int) input.get(1).getValue();
				int p = (int) input.get(2).getValue();
				int f = (int) input.get(3).getValue();
				int d = (int) input.get(4).getValue();
				
				//t -= d;
				int n = t - s - p - f; 
				recipe_area.add(new JLabel("Floub"));
				recipe_area.validate();
				recipe_area.repaint();
				
				
				//bus.post(new RequestGenerateEvent(n, s, p, f, d));
			}
		});
	}

	@Subscribe
	public void on(SuggestRecipesEvent e) {
		List<Recipe> sugg = e.getList();
		Iterator<Recipe> it = sugg.iterator();
		recipe_text.setText(("<html>\t\tSuggestions :"));
		while (it.hasNext()) {
			Recipe rec = it.next();
			recipe_text.setText(recipe_text.getText()+"<br>\t"+rec);
		}
		recipe_text.setText(recipe_text.getText()+"</html>");
		
		
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