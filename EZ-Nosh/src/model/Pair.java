package model;

public class Pair<T1, T2> {
	String key;
	String value;

	public Pair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String setValue(String value) {
		this.value = value;
		return value;
	}
}
