package view;

import model.Recipe;

public class RecipePointer {

	private RecipeLabel rl;
	private BrocoliButton b;
	private DiceButton d;

	public RecipePointer(RecipeLabel rl, BrocoliButton b, DiceButton d) {
		this.rl = rl;
		this.b = b;
		this.d = d;
	}

	public Recipe getRecipe() {
		return rl.getRecipe();
	}

	public RecipeLabel getLabel() {
		return rl;
	}

	public BrocoliButton getBrocoli() {
		return b;
	}
	
	public DiceButton getDice() {
		return d;
	}

	@Override
	public String toString() {
		return "RecipePointer [rl=" + rl.getText() + ", b=" + b.getText() + ", d=" + d.getText() + "]";
	}

}
