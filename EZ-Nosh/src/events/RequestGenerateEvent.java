package events;

public class RequestGenerateEvent {

	private int normals, speedies, picnics, desserts;

	public RequestGenerateEvent(int normals, int speedies, int picnics,
			int desserts) {
		super();
		this.normals = normals;
		this.speedies = speedies;
		this.picnics = picnics;
		this.desserts = desserts;
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
	
	
	
	

}
