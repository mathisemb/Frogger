package model;

public class Tortue extends GameElementLineaire {
	
	boolean isFirst;
	int nbTortues;

	public Tortue(float x, float y, float height, float width, String state) {
		super(x, y, height, width, (float)0.1, 180, state);
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public int getNbTortues() {
		return nbTortues;
	}

	public void setNbTortues(int nbTortues) {
		this.nbTortues = nbTortues;
	}
	
	

}
