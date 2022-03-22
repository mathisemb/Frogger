package model;

import com.badlogic.gdx.utils.Array;

public class Route extends GameElementLineaire {
	
	Array<Vehicule> lesVehicules;
	
		super(x, y, height, width, speed, distance, "route");
		lesVehicules = new Array<Vehicule>();
	}

	public Array<Vehicule> getLesVehicules() {
		return lesVehicules;
	}
	
}