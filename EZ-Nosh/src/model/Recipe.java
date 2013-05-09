package model;

import java.util.LinkedList;
import java.util.List;


public class Recipe {
	public static final int NORMAL = 1;
	public static final int SPEEDY = 2;
	public static final int PICNIC = 4;
	public static final int DESSERT = 8;
	public static final int FANCY = 16;
		
	private String name;
	private LinkedList<Item> items;
	private int type;
	private int strongType;
	
	
	public Recipe(String name, int type) {
		this(name, type, 0);
	}

	public Recipe(String name, int type, int strongType) {
		this.name= name;
		items = new LinkedList<Item>();
		this.type = type;
		this.strongType = strongType;
	}

	public void add(Item i) {
		items.add(i);
		
	}
	
	public String getName() {
		return name;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public int getType() {
		return type;
	}
	
	public int getStrongType() {
		return strongType;
	}
	
	public String toString() {
		return name;
	}

	public Recipe clone(int type2) {
		Recipe r = new Recipe(getName(), getType(), type2);
		r.setItems(items);
		return r;
	}
	
	public void setItems(LinkedList<Item> items) {
		this.items = items;
	}

}
