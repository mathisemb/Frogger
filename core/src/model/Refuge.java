package model;

public class Refuge extends StaticGameElement {

	Frogger inside;

	public Refuge(float x, float y, float height, float width) {
		super(x, y, height, width, "refuge");
		this.inside = null;
	}

	public boolean isOccupied() {
		return inside!=null;
	}

	public void setInside(Frogger inside) {
		this.inside = inside;
	}
	
	public Frogger getInside() {
		return inside;
	}
	
	public boolean moucheIsHere() {
		return World.getInstance().getMouche().getX() == this.getX() && World.getInstance().getMouche().getY() == this.getY();
	}
	
}
