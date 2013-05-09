package events;

import model.Recipe;
import view.DiceButton;
import view.RecipePointer;

public class RollDiceEvent {

	private RecipePointer rp;
	private Recipe rec;
	
	public RollDiceEvent(RecipePointer recipePointer, Recipe r) {
		this.rp = recipePointer;
		this.rec = r;
	}

	public RecipePointer getRecipePointer() {
		return rp;
	}

	public Recipe getRecipe() {
		return rec;
	}


}
