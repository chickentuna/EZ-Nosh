package model;

import java.util.LinkedList;


public class Recipe {
	public static final int NORMAL = 1;
	public static final int SPEEDY = 2;
	public static final int PICNIC = 4;
	public static final int DESSERT = 8;
		
	private String name;
	private LinkedList<Item> items;
	private int type;
	
	public Recipe(String name, int type) {
		this.name= name;
		items = new LinkedList<Item>();
		this.type = type;
	}

	public void add(Item i) {
		items.add(i);
		
	}
	
	public int getType() {
		return type;
	}

}
