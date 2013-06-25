package view;

import javax.swing.JButton;

import model.Recipe;

public class DiceButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RecipeLabel rec;
	
	public DiceButton(RecipeLabel rec) {
		super("dice");
		this.rec = rec;
	}
	
	public Recipe getRecipe() {
		return rec.getRecipe();
	}
}
