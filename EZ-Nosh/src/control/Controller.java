package control;

import java.io.FileNotFoundException;

import model.RecipeManager;

import com.google.common.eventbus.EventBus;
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
		RecipeManager.get();
	}

	
}
