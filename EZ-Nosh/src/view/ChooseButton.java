package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;

import com.google.common.eventbus.EventBus;

import events.SuggestRecipesEvent;

import model.Recipe;

public class ChooseButton extends JButton {
	
	private Recipe rec;
	private EventBus _bus;
	
	public ChooseButton(Recipe r, EventBus bus) {
		super(r.getName());
		
		this.rec = r;
		this._bus = bus;
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List <Recipe> l = new LinkedList<Recipe>();
				l.add(new Recipe(rec.getName(), rec.getType(), rec.inferStrongType()));
				_bus.post(new SuggestRecipesEvent(l, true));
			}
		});
	}
	
	
}
