package model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



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
	private HashMap<Integer, LinkedList<Recipe>> cache;
	private BufferedReader input;
	
	private RecipeManager() throws Exception {
		recipes = new LinkedList<Recipe>();
		loadRecipes();
	}

	private void loadRecipes() throws Exception {
		String fname = "recipes.ez";
		input = new BufferedReader(new FileReader(fname));
		Recipe recip;
		recip = getNextRecipe();
		while (recip != null) {
			recipes.add(recip);
			recip = getNextRecipe();
		}
		
		cache = new HashMap<Integer, LinkedList<Recipe>>();
		cache.put(Recipe.NORMAL, new LinkedList<Recipe>());
		cache.put(Recipe.SPEEDY, new LinkedList<Recipe>());
		cache.put(Recipe.PICNIC, new LinkedList<Recipe>());
		cache.put(Recipe.DESSERT, new LinkedList<Recipe>());
		
		Iterator<Recipe> it = recipes.iterator();
		while (it.hasNext()) {
			recip = it.next();
		
			int [] types = {Recipe.NORMAL, Recipe.SPEEDY, Recipe.PICNIC, Recipe.DESSERT};
			
			for (int i = 0; i < types.length; i++) {
				if (isOfType(recip,types[i])) {
					cache.get(types[i]).add(recip);
				}
			}
		}
		
	}

	private boolean isOfType(Recipe recip, int type) {
		return (recip.getType() & type) == type;
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

	public List<Recipe> getRandomRecipes(int normals, int speedies, int picnics, int desserts) {
		List<Recipe> random = new LinkedList<Recipe>();
		
		for (int k = 0; k<normals; k++) {
			random.add(getRandomRecipe(Recipe.NORMAL));
		}
		for (int k = 0; k<speedies; k++) {
			random.add(getRandomRecipe(Recipe.SPEEDY));
		}
		for (int k = 0; k<picnics; k++) {
			random.add(getRandomRecipe(Recipe.PICNIC));
		}
		for (int k = 0; k<desserts; k++) {
			random.add(getRandomRecipe(Recipe.DESSERT));
		}
				
		return random;
	}

	private Recipe getRandomRecipe(int type) {
		LinkedList<Recipe> list = cache.get(type);
		int index = (int) (Math.random() * list.size());
		return list.get(index);
	}

	public Amounts getIngredientsFor(List<Recipe> list) {
		Amounts amounts = new Amounts();
		Iterator<Recipe> it = list.iterator();
		while (it.hasNext()) {
			Recipe r = it.next();
			Iterator<Item> ing = r.getItems().iterator();
			while (ing.hasNext()) {
				Item i = ing.next();
				String name = i.getIngredient().toLowerCase();
				String unit = i.getUnit().toLowerCase();
				
				if (amounts.containsKey(name,unit)) {
					amounts.put(name, amounts.get(name) + i.getAmount(), unit);
				} else {
					amounts.put(name, i.getAmount(), unit);
				}
			}
		}
		return amounts;
	}
	
	
	
	
}
