package controller;

import java.util.Random;

import model.Route;
import model.Vehicule;

public class ControllerRoute {
	
	private Route route;
	
	public ControllerRoute(Route route) {
		this.route = route;
	}
	
	public void majRoute(float delta) {
		float startPos;
		Vehicule last, nouveau;
		if (route.getSpeed() < 0) { // on va de droite à gauche
			if (route.getLesVehicules().size == 0) {
				route.getLesVehicules().add(getVehiculeAleatoire(route.getWidth()));
			}
			else {
				last = route.getLesVehicules().get(route.getLesVehicules().size - 1);
				startPos = last.getX() + last.getWidth() + route.getDistance();
				route.getLesVehicules().add(getVehiculeAleatoire(startPos));
			}

			for(Vehicule v : route.getLesVehicules()) {
				if (v.getX() + v.getWidth() <=route.getX())
					route.getLesVehicules().removeValue(v, true);
				v.setX(v.getX()+delta*route.getSpeed());
			}
		}
		else { // on va de gauche à droite
			if (route.getLesVehicules().size == 0) {
				route.getLesVehicules().add(getVehiculeAleatoire(-4));
			}
			else {
				last = route.getLesVehicules().get(route.getLesVehicules().size - 1);
				nouveau = getVehiculeAleatoire(-4);
				nouveau.setX(last.getX()-route.getDistance()-nouveau.getWidth());
				route.getLesVehicules().add(nouveau);
			}

			for(Vehicule v : route.getLesVehicules()) {
				if (v.getX() >= route.getWidth())
					route.getLesVehicules().removeValue(v, true);
				v.setX(v.getX()+delta*route.getSpeed());
			}
		}
	}
	
	
	public Vehicule getVehiculeAleatoire(float x) {
		Random r = new Random();
		int i = r.nextInt(5);
		switch(i) {
			case 0: return new Vehicule(x,(float)route.getY(),(float)1, (float)2, route.getSpeed(), route.getDistance(), "voitureJaune");
			case 1: return new Vehicule(x,(float)route.getY(),(float)1, (float)2, route.getSpeed(), route.getDistance(), "voitureBleue");
			case 2: return new Vehicule(x,(float)route.getY(),(float)1, (float)2, route.getSpeed(), route.getDistance(), "voitureVerte");
			case 3: return new Vehicule(x,(float)route.getY(),(float)1, (float)4, route.getSpeed(), route.getDistance(), "camion");
			default: return new Vehicule(x,(float)route.getY(),(float)1, (float)3, route.getSpeed(), route.getDistance(), "pompier");
		}
	}
	
}
