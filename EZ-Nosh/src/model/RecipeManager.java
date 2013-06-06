package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RecipeManager {
	private static RecipeManager self = null;

	public static RecipeManager get() {
		if (self == null)
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
		input = new BufferedReader(new InputStreamReader(new FileInputStream(
				fname), "UTF8"));

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
		cache.put(Recipe.FANCY, new LinkedList<Recipe>());
		cache.put(Recipe.DESSERT, new LinkedList<Recipe>());

		Iterator<Recipe> it = recipes.iterator();
		while (it.hasNext()) {
			recip = it.next();

			int[] types = { Recipe.NORMAL, Recipe.SPEEDY, Recipe.PICNIC, Recipe.FANCY,
					Recipe.DESSERT };

			for (int i = 0; i < types.length; i++) {
				if (isOfType(recip, types[i])) {
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
		String ing = opts[0].trim();
		String num = opts[1].trim();
		String unit = "";
		if (opts.length >= 3) {
			unit = opts[2].trim();
		}
		i = new Item(ing, Float.parseFloat(num), unit);

		return i;
	}

	public List<Recipe> getRandomRecipes(int normals, int speedies,
			int picnics, int fancies, int desserts) {
		List<Recipe> random = new LinkedList<Recipe>();

		int[] types = { Recipe.NORMAL, Recipe.SPEEDY, Recipe.PICNIC,
				Recipe.FANCY, Recipe.DESSERT };
		int[] needs = { normals, speedies, picnics, fancies, desserts };

		for (int i = 0; i < types.length; i++) {
			for (int k = 0; k < needs[i]; k++) {
				//System.out.println("has " + types[i]);
				Recipe rec = getRandomRecipe(types[i]);
				if (rec != null)
					random.add(rec);

			}
		}

		return random;
	}

	public Recipe getRandomRecipe(int type) {
		LinkedList<Recipe> list = cache.get(type);
		if (list == null || list.size() == 0)
			return null;

		int index = (int) (Math.random() * list.size());

		Recipe r = list.get(index);

		return r.clone(type);
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

				if (amounts.containsKey(name, unit)) {
					amounts.put(name, amounts.get(name, unit) + i.getAmount(), unit);
				} else {
					amounts.put(name, i.getAmount(), unit);
				}
			}
		}
		return amounts;
	}

	public List<Recipe> getAllRecipes() {

		return recipes;
	}

}
