package model;

public abstract class DynamicGameElement extends GameElement {
	float deplacement;
	int direction; // 0 -> bas
	 			   // 90 -> droite
	   			   // 180 -> haut
	   			   // 270 -> gauche
	
	DynamicGameElement(float x, float y, float height, float width, float deplacement, int direction, String state) {
		super(x, y, height, width, state);
		this.deplacement = deplacement;
		this.direction = direction;
	}
	
	// deplacement
	public void deplacerGauche() {
		setX(x-deplacement);
		setDirection(270);
	}
	public void deplacerDroite() {
		setX(x+deplacement);
		setDirection(90);
	}
	public void deplacerBas() {
		setY(y-deplacement);
		setDirection(0);
	}
	public void deplacerHaut() {
		setY(y+deplacement);
		setDirection(180);
	}

	
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		if (direction == 0 || direction == 90 || direction == 180 || direction == 270) this.direction = direction;
	}

	public float getDeplacement() {
		return deplacement;
	}

	
	

}
