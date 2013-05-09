package view;

import javax.swing.JButton;

import model.Recipe;

public class BrocoliButton extends JButton {
	
	private RecipeLabel rec;
	
	public BrocoliButton(RecipeLabel rec) {
		super("brocoli");
		this.rec = rec;
	}
	
	public Recipe getRecipe() {
		return rec.getRecipe();
	}
}
