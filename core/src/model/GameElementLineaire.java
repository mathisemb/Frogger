package model;

public class GameElementLineaire extends DynamicGameElement {

	float speed; // speed < 0 => de droite à gauche
	float distance;

	GameElementLineaire(float x, float y, float height, float width, float speed, float distance, String state) {
		super(x, y, height, width, speed, 0, state);
		this.speed = speed;
		this.distance = distance;
	}

	public float getSpeed() {
		return speed;
	}

	public float getDistance() {
		return distance;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	
	
}
