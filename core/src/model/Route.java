package model;

import com.badlogic.gdx.utils.Array;

public class Route extends GameElementLineaire {
	
	Array<Vehicule> lesVehicules;
		Route(float x, float y, float height, float width, float speed, float distance) {
		super(x, y, height, width, speed, distance, "route");
		lesVehicules = new Array<Vehicule>();
	}

	public Array<Vehicule> getLesVehicules() {
		return lesVehicules;
	}
	
}
