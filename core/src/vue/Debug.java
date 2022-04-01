package vue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import model.GameElement;
import model.GameElementLineaire;
import model.Projectile;
import model.Riviere;
import model.Route;
import model.Vehicule;
import model.World;

public class Debug {
	
	public boolean afficherGrille;
	public boolean collisionsVoiture;
	public boolean collisionEau;
	
	private static Debug instance; // design pattern singleton

	public static final Debug getInstance() { // design pattern singleton
		if (instance == null)
			instance = new Debug();
		return instance;
	}
	
	ShapeRenderer shapeRenderer;
	int width;
	int height;
	int unit;
	
	Debug() {
		this.width = 9;
		this.height = 13;
		this.unit = 50;
		shapeRenderer = new ShapeRenderer();
		JsonReader json = new JsonReader();
        JsonValue config = json.parse(Gdx.files.internal("configuration.json"));
        JsonValue debug = config.get("Debug");
        this.afficherGrille = debug.getBoolean("afficherGrille");
        this.collisionsVoiture = debug.getBoolean("collisionsVoiture");
        this.collisionEau = debug.getBoolean("collisionEau");
	}
	
	public void afficherDebug() {
		shapeRenderer.begin(ShapeType.Line);
		// Grille
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				shapeRenderer.rect(j*unit, i*unit, unit, unit);
			}
		}
		
		shapeRenderer.setColor(Color.GREEN);
		for(GameElement elem : World.getInstance().getLesElements()) {
			shapeRenderer.rect(elem.getX()*50, elem.getY()*50, elem.getWidth()*50, elem.getHeight()*50);
		}
		for(Riviere riviere : World.getInstance().getLesRivieres()) {
			for(GameElementLineaire elem : riviere.getLesElements())
				shapeRenderer.rect(elem.getX()*50, elem.getY()*50, elem.getWidth()*50, elem.getHeight()*50);
		}
		for(Route route : World.getInstance().getLesRoutes()) {
			for(Vehicule veh : route.getLesVehicules())
				shapeRenderer.rect(veh.getX()*50, veh.getY()*50, veh.getWidth()*50, veh.getHeight()*50);
		}
		for(Projectile proj : World.getInstance().getFrog().getLesProjectiles()) {
			shapeRenderer.rect(proj.getX()*50, proj.getY()*50, proj.getWidth()*50, proj.getHeight()*50);
		}
		shapeRenderer.end();
	}

	public boolean isAfficherGrille() {
		return afficherGrille;
	}

	public void setAfficherGrille(boolean afficherGrille) {
		this.afficherGrille = afficherGrille;
	}

	public boolean isCollisionsVoiture() {
		return collisionsVoiture;
	}

	public void setCollisionsVoiture(boolean collisionsVoiture) {
		this.collisionsVoiture = collisionsVoiture;
	}

	public boolean isCollisionEau() {
		return collisionEau;
	}

	public void setCollisionEau(boolean collisionEau) {
		this.collisionEau = collisionEau;
	}
	
	
}
