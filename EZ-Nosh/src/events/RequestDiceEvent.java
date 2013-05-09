package events;

import model.Recipe;
import view.DiceButton;
import view.RecipePointer;

public class RequestDiceEvent {

	private RecipePointer rec;

	public RequestDiceEvent(RecipePointer rp) {
		this.rec = rp;
	}

	public RecipePointer getRecipePointer() {
		return rec;
	}
	
	
}
