package model;

import java.util.HashMap;

public class Amounts extends HashMap<Pair<String,String>, Float> {

	private static final long serialVersionUID = 7509483396246292620L;
	
	public Amounts() {
		super();
	}
	
	public boolean containsKey(String name, String unit) {
		
		if (unit.length()>2 && unit.endsWith("s")) {
			unit = unit.substring(0, unit.length()-1).toLowerCase();
		}
		
		Pair<String, String> e = new Pair<String, String>(name,unit);
		if (!super.containsKey(e)) {
			String s = name;
			if (s.endsWith("s")) {
				return super.containsKey(new Pair<String, String>(s.substring(0, s.length()-1),unit));
			} else {
				return super.containsKey(new Pair<String, String>(s+"s",unit));
			}
		} else {
			return true;
		}
	}

	public void put(String name, float f, String unit) {
		if (unit.length()>2 && unit.charAt(unit.length()-1)=='s') {
			unit = unit.substring(0, unit.length()-1).toLowerCase();
		}
		
		Pair<String, String> e = new Pair<String, String>(name,unit);
		super.put(e, f);
		
	}

	public float get(String name, String unit) {
		if (unit.length()>2 && unit.charAt(unit.length()-1)=='s') {
			unit = unit.substring(0, unit.length()-1).toLowerCase();
		}
		Pair<String, String> p = new Pair<>(name, unit);
		return super.get(p);
	}
}
