package model;

import com.badlogic.gdx.math.Rectangle;

public abstract class GameElement {
	float x;
	float y;
	
	float height;
	float width;
	
	String state;
	
	Rectangle rectangle;
	
	GameElement(float x, float y, float height, float width, String state) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.state = state;
		rectangle = new Rectangle(x, y, width, height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		this.rectangle.setX(x);
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		this.rectangle.setY(y);
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	
	
}
