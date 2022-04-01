package model;

public class MoucheVolante extends DynamicGameElement{
	
	boolean morte;
	float a, b; // coeff de la droite que suit la mouche volante
	boolean gaucheADroite;
	float x2,y2;

	public MoucheVolante(float x1, float y1, float x2, float y2, float height, float width, float speed) {
		super(x1, y1, height, width, speed, 0, "mouche");
		morte = false;
		a = (y2-y1)/(x2-x1);
		gaucheADroite = x1<x2;
		if (gaucheADroite) {
			b = y1;
		}
		else 
			b= y2;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void hit() {
		morte = true;
		this.setX(-this.getX());
		this.setY(-this.getY());
	}
	
	public void deplacer(float delta) {
		if (gaucheADroite)
			this.setX(getX()+getDeplacement()*delta);
		else
			this.setX(getX()-getDeplacement()*delta);
		
		this.setY(a*getX()+b);
		
		if (isOutOfScreen()) {
			morte = true;
		}
	}
	
	public boolean isDead() {
		return morte;
	}
	
	public boolean isOutOfScreen() {
		return getX()+getWidth() < 0 || getX() > 9
				|| getY()+getHeight() < 0 || getY() > 13;
	}

	public boolean isMorte() {
		return morte;
	}

	public void setMorte(boolean morte) {
		this.morte = morte;
	}
	
	

}
