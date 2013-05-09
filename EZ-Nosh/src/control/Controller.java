package control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import model.Amounts;
import model.Recipe;
import model.RecipeManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.IngredientsGeneratedEvents;
import events.RequestDiceEvent;
import events.RequestGenerateEvent;
import events.RequestIngedientsEvent;
import events.RollDiceEvent;
import events.SuggestRecipesEvent;

import view.Window;

public class Controller  {

	private EventBus bus;
	
	public static void main(String[] args) throws FileNotFoundException {
		try {
			new Controller();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Controller() throws IOException {
		bus = new EventBus();
		Window view = new Window(bus);
		bus.register(view);
		bus.register(this);
		
		RecipeManager.get();
	}
	
	@Subscribe
	public void on(RequestGenerateEvent e) {
		int normals = e.getNormals();
		int speedies = e.getSpeedies();
		int picnics = e.getPicnics();
		int desserts = e.getDesserts();
		int fancies = e.getFancies();
		
		List<Recipe> list = RecipeManager.get().getRandomRecipes(normals, speedies, picnics, fancies, desserts);
		bus.post(new SuggestRecipesEvent(list));
	}
	
	@Subscribe
	public void on(RequestIngedientsEvent e) {
		List<Recipe> list = e.getList();
		System.out.println(list);
		Amounts ings = RecipeManager.get().getIngredientsFor(list);
		System.out.println(ings);
		
		bus.post(new IngredientsGeneratedEvents(ings));

	}

	@Subscribe
	public void on(RequestDiceEvent e) {
		
		Recipe r = RecipeManager.get().getRandomRecipe(e.getRecipePointer().getRecipe().getStrongType());
		System.out.println(e.getRecipePointer().getRecipe());
		System.out.println(e.getRecipePointer().getRecipe().getStrongType());
		bus.post(new RollDiceEvent(e.getRecipePointer(), r));
	}
	
}
