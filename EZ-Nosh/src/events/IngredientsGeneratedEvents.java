package events;

import model.Amounts;

public class IngredientsGeneratedEvents {

	Amounts list;
	
	public IngredientsGeneratedEvents(Amounts ings) {
		list = ings;
	}
	
	public Amounts getIngredients() {
		return list;
	}

}
