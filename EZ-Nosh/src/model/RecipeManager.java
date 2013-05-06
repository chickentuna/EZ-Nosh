package model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class RecipeManager {
	private static RecipeManager self = null;
	
	public static RecipeManager get() {
		if (self==null)
			try {
				self = new RecipeManager();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return self;
	}
	
	private LinkedList<Recipe> recipes;
	private BufferedReader input;
	
	private RecipeManager() throws Exception {
		recipes = new LinkedList<Recipe>();
		loadRecipes();
	}

	private void loadRecipes() throws Exception {
		String fname = "recipes.ez";
		input = new BufferedReader(new FileReader(fname));
		Recipe r;
		r = getNextRecipe();
		while (r != null) {
			recipes.add(r);
			r = getNextRecipe();
		}
	}

	private Recipe getNextRecipe() throws IOException {
		Recipe r;
		String name = input.readLine();
		if (name == null || name.equals("")) {
			return null;
		}
		int type = Integer.parseInt(input.readLine());
		r = new Recipe(name, type);
		Item i;
		i = getNextItem();
		
		while (i != null) {
			r.add(i);
			i = getNextItem();
		}
		return r;
	}

	private Item getNextItem() throws IOException {
		Item i;
		String line = input.readLine();
		if (line.equals("*")) {
			return null;
		}
		String[] opts = line.trim().split(",");
		String ing = opts[0];
		String num = opts[1];
		String unit = "";
		if (opts.length>=3) {
			unit = opts[2];
		}
		i = new Item(ing,Float.parseFloat(num),unit);
		
		return i;
	}
	
	
	
	
}
