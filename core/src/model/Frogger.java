package model;

import com.badlogic.gdx.utils.Array;

public class Frogger extends DynamicGameElement {
	
	int score;
	int vie;
	
	Array<Projectile> lesProjectiles;

	public Frogger(float x, float y, float height, float width, float deplacement, String state, int vie, int score) {
		super(x, y, height, width, deplacement, 0, state);
		this.vie = vie;
		this.score=score;
		lesProjectiles = new Array<Projectile>();
	}

	public int getVie() {
		return vie;
	}

	public void setVie(int vie) {
		this.vie = vie;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Array<Projectile> getLesProjectiles() {
		return lesProjectiles;
	}
	
	
	
	
}
