package model;

import com.badlogic.gdx.utils.Array;

public class Riviere extends GameElementLineaire {
	
	Array<GameElementLineaire> lesElements;

		super(x, y, height, width, speed, distance, type);
		lesElements = new Array<GameElementLineaire>();
	}

	public Array<GameElementLineaire> getLesElements() {
		return lesElements;
	}
	
}