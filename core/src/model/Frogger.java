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
		this.setDirection(180);
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
	
	public boolean isNextProjPossible() {
		if (lesProjectiles.size == 0) // pas de projectile
			return true;
		else {
			Projectile last = lesProjectiles.get(lesProjectiles.size - 1); // on prend le dernier projectile tire
			
			if (direction != last.getDirection())
				return true;
			else {
				switch(direction) {
					case 180 : // vers le haut
						return getY()+World.getInstance().getHeightProjectile()<last.getY();
					case 0 : // vers le bas
						return getY()-World.getInstance().getHeightProjectile()>last.getY();
					case 90 : // vers la droite
						return getX()+World.getInstance().getWidthProjectile()<last.getX();
					case 270 : // vers la gauche
						return getX()-World.getInstance().getWidthProjectile()>last.getX();
					default:
						return false;
				}
			}	
		}
	}
}
