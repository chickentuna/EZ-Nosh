package events;

public class RequestGenerateEvent {

	private int normals, speedies, picnics, desserts, fancy;

	public RequestGenerateEvent(int normals, int speedies, int picnics,
			int fancy, int desserts) {
		super();
		this.normals = normals;
		this.speedies = speedies;
		this.picnics = picnics;
		this.fancy = fancy;
		this.desserts = desserts;
	}

	@Override
	public String toString() {
		return "RequestGenerateEvent [normals=" + normals + ", speedies="
				+ speedies + ", picnics=" + picnics + ", fancies="+fancy+", desserts=" + desserts
				+ "]";
	}

	public int getNormals() {
		return normals;
	}

	public int getSpeedies() {
		return speedies;
	}

	public int getPicnics() {
		return picnics;
	}

	public int getDesserts() {
		return desserts;
	}
	public int getFancies() {
		return fancy;
	}
	
	
	
	

}
