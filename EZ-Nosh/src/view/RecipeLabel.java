package view;

import javax.swing.JLabel;

import model.Recipe;

public class RecipeLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	Recipe rec;
	
	public RecipeLabel(Recipe rec) {
		super("<html>"+rec+"</html>");
		this.rec = rec;
	}

	public Recipe getRecipe() {
		return rec;
	}

	public void setRecipe(Recipe rec) {
		this.rec = rec;
		setText("<html>"+rec+"</html>");
		validate();
		repaint();		
	}
}
