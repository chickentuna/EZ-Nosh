package model;

import java.util.HashMap;
import java.util.Map.Entry;

public class Amounts extends HashMap<Entry<String,String>, Float> {

	private static final long serialVersionUID = 7509483396246292620L;
	
	public Amounts() {
		super();
	}
	
	class Pair implements Entry<String, String> {

		String key;
		String value;
		
		public Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String setValue(String value) {
			this.value = value;
			return value;
		}

		
	}
	
	public boolean containsKey(String name, String unit) {
		Pair e = new Pair(name,unit);
		
		if (!super.containsKey(e)) {
			String s = (String)name;
			if (s.charAt(s.length())=='s') {
				return super.containsKey(s.substring(0, s.length()-1));
			} else {
				return super.containsKey(s+"s");
			}
		} else {
			return true;
		}
	}

	public void put(String name, float f, String unit) {
		Pair e = new Pair(name,unit);
		super.put(e, f);
		
	}
}
