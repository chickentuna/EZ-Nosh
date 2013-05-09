package view;

import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import model.Recipe;

public class YumPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private HashMap<Class<?>, LinkedList<Component>> comps;
	
	
	public YumPanel() {
		comps = new HashMap<Class<?>, LinkedList<Component>>();
	}
	
	@Override
	public Component add(Component c) {
		super.add(c);
		
		if (!comps.containsKey(c.getClass())) {
			comps.put(c.getClass(), new LinkedList<Component>());
		}
		comps.get(c.getClass()).add(c);
		
		return c;
	}

	@Override
	public void removeAll() {
		comps.clear();
		super.removeAll();
	}
	
	@Override
	public void remove(Component c) {
		
		comps.get(c.getClass()).remove(c);
		
		super.remove(c);
	}

	public List<Recipe> getRecipes() {
		LinkedList<Recipe> recps = new LinkedList<>();
		Iterator<Component> it = comps.get(YumPanel.class).iterator();
		while (it.hasNext()) {
			YumPanel entry = (YumPanel) it.next();
			recps.add(entry.getRecipe());
		}		
		return recps;
	}

	public Recipe getRecipe() {
		Recipe r;
		LinkedList<Component> it = comps.get(RecipeLabel.class);
		return ((RecipeLabel)it.get(0)).getRecipe();
	
	}
	
	
}
