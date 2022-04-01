package controller;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import model.DynamicGameElement;
import model.Frogger;
import model.GameElement;
import model.GameElementLineaire;
import model.Mouche;
import model.MoucheVolante;
import model.Projectile;
import model.Refuge;
import model.Riviere;
import model.Route;
import model.StaticGameElement;
import model.Vehicule;
import model.World;
import vue.Debug;
import vue.TextureFactory;

public class WorldRenderer {
   private static World world;
   private static ControllerFrogger controllerFrogger;
   private static ControllerMouche controllerMouche;
   private static ControllerMoucheVolante controllerMoucheVolante;

   private ArrayList<ControllerRoute> lesControllerRoute;
   private ArrayList<ControllerRiviere> lesControllerRiviere;
   
   BitmapFont fontScore;
   BitmapFont fontVies;
   CharSequence strScore = "Score = ";
   CharSequence strVies = "Vies = ";
   
   float timeMoucheMorte;

	public WorldRenderer() {
		WorldRenderer.world = World.getInstance();
		WorldRenderer.controllerFrogger = new ControllerFrogger();
		for(GameElement elem:world.getLesElements()) {
			if (elem instanceof Mouche) {
				controllerMouche = new ControllerMouche((Mouche)elem);
			}
			if (elem instanceof MoucheVolante) {
				controllerMoucheVolante = new ControllerMoucheVolante((MoucheVolante)elem);
			}
		}
		
		lesControllerRoute = new ArrayList<ControllerRoute>();
		for (Route route : world.getLesRoutes()) {
			lesControllerRoute.add(new ControllerRoute(route));
		}
		lesControllerRiviere = new ArrayList<ControllerRiviere>();
		for (Riviere riviere : world.getLesRivieres()) {
			lesControllerRiviere.add(new ControllerRiviere(riviere));
		}
		
		fontScore = new BitmapFont();
		fontVies = new BitmapFont();
		
		timeMoucheMorte = 0;
	}

	public void render(SpriteBatch batch, float delta) {
		
		System.out.println("Collisions véhicules = " + Debug.getInstance().collisionsVoiture + "	| Collision rivière = " + Debug.getInstance().collisionEau);

		// ----------------- MAJ DU MODELE EN FONCTION DES ACTIONS DU JOUEUR -----------------
		controllerFrogger.majFrogger(delta, world.getFrog());
		
		if (world.getMouche().isDead()) {
			timeMoucheMorte += delta;
			if (timeMoucheMorte > 1) {
				world.getMouche().reset();
				timeMoucheMorte = 0;
			}
		}
		else {
			controllerMouche.majMouche(delta);
		}

		
		if (controllerMoucheVolante.getMouche().isOutOfScreen()) {
			controllerMoucheVolante.getMouche().setMorte(true);
			this.newMoucheVolante();
			// System.out.println("new mouche : " + controllerMoucheVolante.getMouche().getX() + " , " + controllerMoucheVolante.getMouche().getY());
		}
		else {
			controllerMoucheVolante.majMouche(delta);
		}
		/*
		if (!controllerMoucheVolante.getMouche().isDead()) {
			controllerMoucheVolante.majMouche(delta);
		}
		else {
			this.newMoucheVolante();
		}
		*/
		
		
		// ------------------------------ AFFICHAGE SUR LE BATCH ------------------------------
		for(GameElement elem : world.getLesElements()) {
			if (elem instanceof DynamicGameElement) {
				if (!(elem instanceof Frogger) && !(elem instanceof MoucheVolante)) {
					DynamicGameElement dynamic = (DynamicGameElement)elem;
					Sprite s = new Sprite(TextureFactory.getInstance().getTexture(elem));
					//s.setRotation(dynamic.getDirection());
					s.setSize(mapper(elem.getWidth()), mapper(elem.getHeight()));
					s.setPosition(mapper(dynamic.getX()), mapper(dynamic.getY()));
					s.draw(batch);
				}
				/*
				else {
					if (!elem.equals(world.getFrog())) { // les froggers dans les refuges
						DynamicGameElement dynamic = (DynamicGameElement)elem;
						Sprite s = new Sprite(TextureFactory.getInstance().getTexture(elem));
						s.setRotation(dynamic.getDirection());
						s.setPosition(mapper(dynamic.getX()), mapper(dynamic.getY()));
						s.draw(batch);
					}
				}
				*/
			} else if (elem instanceof StaticGameElement)
				batch.draw(TextureFactory.getInstance().getTexture(elem), mapper(elem.getX()), mapper(elem.getY()), mapper(elem.getWidth()), mapper(elem.getHeight()));
		}
		
		int i = 0;
		for(Riviere riviere : world.getLesRivieres()) {
			lesControllerRiviere.get(i).majRiviere(delta);
			i++;
		}
		for(Riviere riviere : world.getLesRivieres()) {
			for(GameElementLineaire elem : riviere.getLesElements()) {
				Sprite s = new Sprite(TextureFactory.getInstance().getTexture(elem));
				s.setSize(mapper(elem.getWidth()), mapper(elem.getHeight()));
				s.setPosition(mapper(elem.getX()), mapper(elem.getY()));
				s.draw(batch);
			}
		}
		
		for(Refuge refuge : world.getLesRefuges()) {
			if (refuge.isOccupied()) {
				Sprite s = new Sprite(TextureFactory.getInstance().getTexture(refuge.getInside()));
				s.setRotation(refuge.getInside().getDirection());
				s.setPosition(mapper(refuge.getInside().getX()), mapper(refuge.getInside().getY()));
				s.draw(batch);
			}
		}
		
		Sprite s = new Sprite(TextureFactory.getInstance().getTexture(world.getFrog()));
		s.setRotation(world.getFrog().getDirection());
		//s.setSize(mapper(world.getFrog().getWidth()), mapper(world.getFrog().getHeight()));
		s.setPosition(mapper(world.getFrog().getX()), mapper(world.getFrog().getY()));
		s.draw(batch);
		
		i = 0;
		for(Route route : world.getLesRoutes()) {
			lesControllerRoute.get(i).majRoute(delta);
			i++;
		}
		for(Route route : world.getLesRoutes()) {
			for(Vehicule v : route.getLesVehicules()) {
				s = new Sprite(TextureFactory.getInstance().getTexture(v));
				//s.setRotation(v.getDirection());
				s.setSize(mapper(v.getWidth()), mapper(v.getHeight()));
				s.setPosition(mapper(v.getX()), mapper(v.getY()));
				s.draw(batch);
			}
		}
		
		fontScore.draw(batch, strScore +  Integer.toString(world.getFrog().getScore()), 10, 40);
		fontVies.draw(batch, strVies +  Integer.toString(world.getFrog().getVie()), 10, 25);
		
		
		for(Projectile proj : world.getFrog().getLesProjectiles()) {
			s = new Sprite(TextureFactory.getInstance().getTexture(proj));
			//s.setRotation(proj.getDirection());
			s.setSize(mapper(proj.getWidth()), mapper(proj.getHeight()));
			s.setPosition(mapper(proj.getX()), mapper(proj.getY()));
			s.draw(batch);
		}
		
		s = new Sprite(TextureFactory.getInstance().getTexture(world.getMoucheVolante()));
		//s.setRotation(proj.getDirection());
		s.setSize(mapper(world.getMoucheVolante().getWidth()), mapper(world.getMoucheVolante().getHeight()));
		s.setPosition(mapper(world.getMoucheVolante().getX()), mapper(world.getMoucheVolante().getY()));
		s.draw(batch);
		
	}
	
	public int mapper(float d) {
		// traduit les coordonnées et taille de world en taille en pixel pour l'affichage libgdx
		return (int)(d*50);
	}
	
	public void newMoucheVolante() {
		/*
		float y1 = (float) (Math.random() * 13);
		float y2 = (float) (Math.random() * 13);
		
		float x1;
		float x2;
		Random r = new Random();
		int i = r.nextInt(2);
		switch(i) {
			case 0: // gauche a droite
				x1 = -world.getMoucheVolante().getWidth();
				x2 = 9;
				break;
			default: // droite a gauche
				x1 = 9;
				x2 = -world.getMoucheVolante().getWidth();
				break;
		}
		*/
		
		
		
		float x1;
		float x2;
		float y1;
		float y2;
		Random r = new Random();
		int i = r.nextInt(4);
		switch(i) {
			case 0: // cote gauche
				x1 = -world.getMoucheVolante().getWidth();
				y1 = (float) (Math.random() * 13);
				r = new Random();
				i = r.nextInt(3);
				switch(i) {
					case 0: // cote bas
						x2 = (float) (Math.random() * 10);
						y2 = -world.getMoucheVolante().getHeight();
						break;
					case 1: // cote droit
						x2 = 9 + world.getMoucheVolante().getWidth();
						y2 = (float) (Math.random() * 13);
						break;
					default: // cote haut
						x2 = (float) (Math.random() * 10);
						y2 = 13 + world.getMoucheVolante().getHeight();
						break;
				}
				break;
			case 1: // cote bas
				x1 = (float) (Math.random() * 10);
				y1 = -world.getMoucheVolante().getHeight();
				r = new Random();
				i = r.nextInt(3);
				switch(i) {
					case 0: // cote gauche
						x2 = -world.getMoucheVolante().getWidth();
						y2 = (float) (Math.random() * 13);
						break;
					case 1: // cote droit
						x2 = 9 + world.getMoucheVolante().getWidth();
						y2 = (float) (Math.random() * 13);
						break;
					default: // cote haut
						x2 = (float) (Math.random() * 10);
						while(x2==x1) {
							x2 = (float) (Math.random() * 10);
						}
						y2 = 13 + world.getMoucheVolante().getHeight();
						break;
				}
				break;
			case 2: // cote droit
				x1 = 9 + world.getMoucheVolante().getWidth();
				y1 = (float) (Math.random() * 13);
				r = new Random();
				i = r.nextInt(3);
				switch(i) {
					case 0: // cote bas
						x2 = (float) (Math.random() * 10);
						y2 = -world.getMoucheVolante().getHeight();
						break;
					case 1: // cote gauche
						x2 = -world.getMoucheVolante().getWidth();
						y2 = (float) (Math.random() * 13);
						break;
					default: // cote haut
						x2 = (float) (Math.random() * 10);
						y2 = 13 + world.getMoucheVolante().getHeight();
						break;
				}
				break;
			default: // cote haut
				x1 = (float) (Math.random() * 10);
				y1 = 13 + world.getMoucheVolante().getHeight();
				r = new Random();
				i = r.nextInt(3);
				switch(i) {
					case 0: // cote bas
						x2 = (float) (Math.random() * 10);
						while(x2==x1) {
							x2 = (float) (Math.random() * 10);
						}
						y2 = -world.getMoucheVolante().getHeight();
						break;
					case 1: // cote droit
						x2 = 9 + world.getMoucheVolante().getWidth();
						y2 = (float) (Math.random() * 13);
						break;
					default: // cote gauche
						x2 = -world.getMoucheVolante().getWidth();
						y2 = (float) (Math.random() * 13);
						break;
				}
				break;
		}
		world.setMoucheVolante(new MoucheVolante(x1, y1, x2, y2, world.getMoucheVolante().getHeight(), world.getMoucheVolante().getWidth(), world.getMoucheVolante().getDeplacement()));
		WorldRenderer.controllerMoucheVolante.setMouche(world.getMoucheVolante());
	}
	
}
