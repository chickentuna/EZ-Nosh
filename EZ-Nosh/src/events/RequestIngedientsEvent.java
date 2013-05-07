package events;

import java.util.List;

import model.Recipe;

public class RequestIngedientsEvent {
	private List<Recipe> list;
	
	public RequestIngedientsEvent(List<Recipe> list) {
		this.list = list;
	}
	
	public List<Recipe> getList() {
		return list;
	}
	
}
