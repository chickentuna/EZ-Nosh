package model;

import java.util.LinkedList;


public class Recipe {
	public static final int NORMAL = 1;
	public static final int SPEEDY = 2;
	public static final int PICNIC = 4;
	public static final int DESSERT = 8;
		
	private String name;
	private LinkedList<Item> items;
	
	public Recipe(String name) {
		this.name= name;
		items = new LinkedList<Item>();
		
	}

	public void add(Item i) {
		items.add(i);
		
	}

}
