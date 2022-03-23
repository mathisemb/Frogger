package controller;

import java.util.Random;

import model.GameElementLineaire;
import model.Riviere;
import model.Tortue;
import model.Tronc;

public class ControllerRiviere {
	
	private Riviere riviere;
	
	public ControllerRiviere(Riviere riviere) {
		this.riviere = riviere;
	}
	
	public void majRiviere(float delta) {
		float startPos;
		GameElementLineaire last, nouveau;
		if (riviere.getSpeed() < 0) { // on va de droite à gauche
			if (riviere.getLesElements().size == 0) {
				if (riviere.getState().equals("tortues")) { // ajouter 2 ou 3 tortues aux éléments
					Random r = new Random();
					int i = r.nextInt(3);
					if (i==0) {
						Tortue tortue = new Tortue(riviere.getWidth(), riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(2);
						riviere.getLesElements().add(tortue);
						riviere.getLesElements().add(new Tortue(riviere.getWidth()+1, riviere.getY(), 1, 1, "tortue"));
					}
					else {
						Tortue tortue = new Tortue(riviere.getWidth(), riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(3);
						riviere.getLesElements().add(tortue);
						riviere.getLesElements().add(new Tortue(riviere.getWidth()+1, riviere.getY(), 1, 1, "tortue"));
						riviere.getLesElements().add(new Tortue(riviere.getWidth()+2, riviere.getY(), 1, 1, "tortue"));
					}
				}
				else { // on ajoute un tronc de taille aléatoire
					riviere.getLesElements().add(getTroncAleatoire(riviere.getWidth()));
				}
			}
			else if (riviere.getLesElements().size<4){
				last = riviere.getLesElements().get(riviere.getLesElements().size - 1);
				startPos = last.getX() + last.getWidth() + riviere.getDistance();
				if (riviere.getState().equals("tortues")) { // ajouter 2 ou 3 tortues aux éléments
					Random r = new Random();
					int i = r.nextInt(3);
					if (i==0) {
						Tortue tortue = new Tortue(startPos, riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(2);
						riviere.getLesElements().add(tortue);
						riviere.getLesElements().add(new Tortue(startPos+1, riviere.getY(), 1, 1, "tortue"));
					}
					else {
						Tortue tortue = new Tortue(startPos, riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(3);
						riviere.getLesElements().add(tortue);
						riviere.getLesElements().add(new Tortue(startPos+1, riviere.getY(), 1, 1, "tortue"));
						riviere.getLesElements().add(new Tortue(startPos+2, riviere.getY(), 1, 1, "tortue"));
					}
				}
				else { // on ajoute un tronc de taille aléatoire
					riviere.getLesElements().add(getTroncAleatoire(startPos));
				}
			}

			for(GameElementLineaire elem : riviere.getLesElements()) {
				if (elem.getX() + elem.getWidth() < riviere.getX())
					riviere.getLesElements().removeValue(elem, true);
				else
					elem.setX(elem.getX()+delta*riviere.getSpeed());
			}
		}
		else { // on va de gauche à droite
			if (riviere.getLesElements().size == 0) {
				if (riviere.getState().equals("tortues")) { // ajouter 2 ou 3 tortues aux éléments
					Random r = new Random();
					int i = r.nextInt(3);
					if (i==0) {
						riviere.getLesElements().add(new Tortue(-3, riviere.getY(), 1, 1, "tortue"));						
						Tortue tortue = new Tortue(-4, riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(2);
						riviere.getLesElements().add(tortue);
					}
					else {
						riviere.getLesElements().add(new Tortue(-3, riviere.getY(), 1, 1, "tortue"));
						riviere.getLesElements().add(new Tortue(-4, riviere.getY(), 1, 1, "tortue"));						
						Tortue tortue = new Tortue(-5, riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(3);
						riviere.getLesElements().add(tortue);
					}
				}
				else { // on ajoute un tronc de taille aléatoire
					riviere.getLesElements().add(getTroncAleatoire(-4));
				}
			}
			else if (riviere.getLesElements().size<4) {
				if (riviere.getState().equals("tortues")) { // ajouter 2 ou 3 tortues aux éléments
					last = riviere.getLesElements().get(riviere.getLesElements().size - 1);
					
					Random r = new Random();
					int i = r.nextInt(3);
					if (i==0) {
						riviere.getLesElements().add(new Tortue(last.getX()-riviere.getDistance(), riviere.getY(), 1, 1, "tortue"));
						Tortue tortue = new Tortue(last.getX()-riviere.getDistance()-1, riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(2);
						riviere.getLesElements().add(tortue);
					}
					else {
						riviere.getLesElements().add(new Tortue(last.getX()-riviere.getDistance(), riviere.getY(), 1, 1, "tortue"));
						riviere.getLesElements().add(new Tortue(last.getX()-riviere.getDistance()-1, riviere.getY(), 1, 1, "tortue"));
						
						Tortue tortue = new Tortue(last.getX()-riviere.getDistance()-2, riviere.getY(), 1, 1, "tortue");
						tortue.setFirst(true);
						tortue.setNbTortues(3);
						riviere.getLesElements().add(tortue);
					}
				}
				else { // on ajoute un tronc de taille aléatoire
					last = riviere.getLesElements().get(riviere.getLesElements().size - 1);
					nouveau = getTroncAleatoire(-4);
					nouveau.setX(last.getX()-riviere.getDistance()-nouveau.getWidth());
					riviere.getLesElements().add(nouveau);
				}
			}

			for(GameElementLineaire elem : riviere.getLesElements()) {
				if (elem.getX() >= riviere.getWidth())
					riviere.getLesElements().removeValue(elem, true);
				else
					elem.setX(elem.getX()+delta*riviere.getSpeed());
			}
		}
	}
	
	public Tronc getTroncAleatoire(float x) {
		Random r = new Random();
		int i = r.nextInt(3);
		switch(i) {
			case 0: return new Tronc(x, riviere.getY(), riviere.getHeight(), 3, riviere.getSpeed(), riviere.getDistance(), "tronc1");
			case 1: return new Tronc(x, riviere.getY(), riviere.getHeight(), (float) 4.5, riviere.getSpeed(), riviere.getDistance(), "tronc2");
			default: return new Tronc(x, riviere.getY(), riviere.getHeight(), 6, riviere.getSpeed(), riviere.getDistance(), "tronc3");
		}
	}	
}
