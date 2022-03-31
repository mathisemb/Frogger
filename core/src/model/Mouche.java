package model;

import java.util.Random;

public class Mouche extends DynamicGameElement{
	
	boolean morte;

	public Mouche(float x, float y, float height, float width, float deplacement, int direction, String state) {
		super(x, y, height, width, deplacement, direction, state);
		morte = false;
	}
	
	public void reset() {
		morte = false;
		Random r = new Random();
		int i = r.nextInt(5);
		this.setX(World.getInstance().getLesRefuges().get(i).getX());
		this.setY(World.getInstance().getLesRefuges().get(i).getY());
	}
	
	public void hide() {
		morte = true;
		this.setX(-this.getX());
		this.setY(-this.getY());
	}
	
	public boolean isDead() {
		return morte;
	}

}
