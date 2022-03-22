package model;

public class Fond extends StaticGameElement{

	Fond(float x, float y, float height, float width) {
		super(x, y, height, width, "fond");
	}
	
	public String getState() {
		return state;
	}

}
