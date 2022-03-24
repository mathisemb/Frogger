package model;

public class Refuge extends StaticGameElement {
	
	boolean occupied;

	public Refuge(float x, float y, float height, float width) {
		super(x, y, height, width, "refuge");
		this.occupied = false;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	

}
