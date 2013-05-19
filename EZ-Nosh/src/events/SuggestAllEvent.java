package events;

import java.util.List;

import model.Recipe;

public class SuggestAllEvent {
	List<Recipe> recs;
	
	public SuggestAllEvent(List<Recipe> recs) {
		this.recs = recs;
	}
	
	public List<Recipe> getList() {
		return recs;
	}
}
