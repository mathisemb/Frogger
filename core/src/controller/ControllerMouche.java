package controller;

import java.util.ArrayList;
import java.util.Random;

import model.Mouche;
import model.World;
import model.Refuge;

public class ControllerMouche {
	Mouche mouche;
	float time;
	float intervalle;
	ArrayList<Refuge> lesRefuges;
	
	public ControllerMouche(Mouche mouche) {
		this.mouche = mouche;
		this.time = 0;
		this.intervalle = (float)1;
		lesRefuges = new ArrayList<Refuge>();
		lesRefuges = World.getInstance().getLesRefuges();

	}
 
	public void majMouche(float delta) { // methode appelee dans WorldRenderer pour mettre a jour la position de la mouche
							  // en fonction du temps ecoule
		if (time < intervalle)
			time += delta;
		else {
			time = 0;
			// Position aléatoire = indice aléatoire dans le tableau de refuges possibles
			Random r = new Random();
			int i = r.nextInt(5);
			mouche.setX(lesRefuges.get(i).getX());
			mouche.setY(lesRefuges.get(i).getY());

			// Intervalle aléatoire
			this.time = (float)Math.random(); // Math.random()*(Max-Min)
		}
	}
}
