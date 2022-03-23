package controller;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import model.DynamicGameElement;
import model.Frogger;
import model.GameElement;
import model.GameElementLineaire;
import model.Mouche;
import model.Riviere;
import model.Route;
import model.StaticGameElement;
import model.Tortue;
import model.Vehicule;
import model.World;
import vue.TextureFactory;

public class WorldRenderer {
   private static World world;
   private static ControllerFrogger controllerFrogger;
   private static ControllerMouche controllerMouche;

   private ArrayList<ControllerRoute> lesControllerRoute;
   private ArrayList<ControllerRiviere> lesControllerRiviere;

	public WorldRenderer() {
		WorldRenderer.world = World.getInstance();
		WorldRenderer.controllerFrogger = new ControllerFrogger(world.getFrog());
		for(GameElement elem:world.getLesElements()) {
			if (elem instanceof Mouche) {
				WorldRenderer.controllerMouche = new ControllerMouche((Mouche)elem);
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
	}

	public void render(SpriteBatch batch, float delta) {

		// ----------------- MAJ DU MODELE EN FONCTION DES ACTIONS DU JOUEUR -----------------
		controllerFrogger.majFrogger(delta);
		controllerMouche.majMouche(delta);
		
		// ------------------------------ AFFICHAGE SUR LE BATCH ------------------------------
		for(GameElement elem : world.getLesElements()) {
			if (elem instanceof DynamicGameElement) {
				if (!(elem instanceof Frogger)) {
					DynamicGameElement dynamic = (DynamicGameElement)elem;
					Sprite s = new Sprite(TextureFactory.getInstance().getTexture(elem));
					//s.setRotation(dynamic.getDirection());
					s.setSize(mapper(elem.getWidth()), mapper(elem.getHeight()));
					s.setPosition(mapper(dynamic.getX()), mapper(dynamic.getY()));
					s.draw(batch);
				}
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
		
	}
	
	public int mapper(float d) {
		// traduit les coordonnées et taille de world en taille en pixel pour l'affichage libgdx
		return (int)(d*50);
	}
	
}
