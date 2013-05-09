package view;

import javax.swing.JButton;

import model.Recipe;

public class DiceButton extends JButton {

	private RecipeLabel rec;
	
	public DiceButton(RecipeLabel rec) {
		super("dice");
		this.rec = rec;
	}
	
	public Recipe getRecipe() {
		return rec.getRecipe();
	}
}
