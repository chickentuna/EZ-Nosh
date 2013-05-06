package control;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import com.google.common.eventbus.EventBus;

import view.Window;

public class Controller  {

	private EventBus bus;
	
	public static void main(String[] args) {
		new Controller();
	}

	public Controller() {
		bus = new EventBus();
		Window view = new Window();
		bus.register(view);
	}

	
}
