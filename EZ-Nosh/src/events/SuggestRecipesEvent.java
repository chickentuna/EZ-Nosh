package events;

import java.util.List;

import model.Recipe;

public class SuggestRecipesEvent {

	private List<Recipe> list;
	
	public SuggestRecipesEvent(List<Recipe> list) {
		this.list = list;
	}
	
	public List<Recipe> getList() {
		return list;
	}

}
