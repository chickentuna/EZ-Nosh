package control;

import java.io.FileNotFoundException;
import java.util.List;

import model.Recipe;
import model.RecipeManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.RequestGenerateEvent;

import view.Window;

public class Controller  {

	private EventBus bus;
	
	public static void main(String[] args) throws FileNotFoundException {
		new Controller();
	}

	public Controller() {
		bus = new EventBus();
		Window view = new Window();
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
		
		
	}
	
	

	
}
