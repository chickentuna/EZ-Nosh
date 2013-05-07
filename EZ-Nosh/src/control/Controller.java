package control;

import java.io.FileNotFoundException;
import java.util.List;

import model.Amounts;
import model.Recipe;
import model.RecipeManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.IngredientsGeneratedEvents;
import events.RequestGenerateEvent;
import events.RequestIngedientsEvent;
import events.SuggestRecipesEvent;

import view.Window;

public class Controller  {

	private EventBus bus;
	
	public static void main(String[] args) throws FileNotFoundException {
		new Controller();
	}

	public Controller() {
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
		
		List<Recipe> list = RecipeManager.get().getRandomRecipes(normals, speedies, picnics, desserts);
		bus.post(new SuggestRecipesEvent(list));
	}
	
	@Subscribe
	public void on(RequestIngedientsEvent e) {
		List<Recipe> list = e.getList();
		Amounts ings = RecipeManager.get().getIngredientsFor(list);
		bus.post(new IngredientsGeneratedEvents(ings));
	}

	
}
