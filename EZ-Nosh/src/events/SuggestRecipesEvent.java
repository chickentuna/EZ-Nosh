package events;

import java.util.List;

import model.Recipe;

public class SuggestRecipesEvent {

	private List<Recipe> list;
	private boolean append;
	
	public SuggestRecipesEvent(List<Recipe> list) {
		this(list, false);
	}
	
	public SuggestRecipesEvent(List<Recipe> list, boolean b) {
		this.list = list;
		append = b;
	}

	public List<Recipe> getList() {
		return list;
	}

	public boolean isAppend() {
		return append;
	}

}
