package model;

public class Item {
	String ingredient;
	float amount;
	String unit;
	
	public Item(String ing, float amount, String unit) {
		ing = ing.toLowerCase();
		if (ing.endsWith("s")) {
			ing.substring(0, ing.length()-1);
		}
		
		unit = unit.toLowerCase();
		if (unit.endsWith("s")) {
			unit.substring(0, unit.length()-1);
		}
		
		this.ingredient = ing;
		this.amount = amount;
		this.unit = unit;
	}

	public String getIngredient() {
		return ingredient;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

}
