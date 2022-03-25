package model;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class World {
	private ArrayList<GameElement> lesElements;
	private ArrayList<Route> lesRoutes;
	private ArrayList<Riviere> lesRivieres;
	private ArrayList<Refuge> lesRefuges;
	private Frogger frog;
	private Mouche mouche;
	
	private int height;
	private int width;
	
	private int nbTirs;
	private float distance;
	private float projHeight;
	private float projWidth;
	
	private static World instance; // design pattern singleton

	public World() throws IOException {		
		lesElements = new ArrayList<GameElement>();
		lesRoutes = new ArrayList<Route>();
		lesRivieres = new ArrayList<Riviere>();
		lesRefuges = new ArrayList<Refuge>();
	
		// configuration avec fichier .json
		JsonReader json = new JsonReader();
        JsonValue config = json.parse(Gdx.files.internal("configuration.json"));
        initWorld(config);
        initFond(config);
        initRoutes(config);
        initRivieres(config);
        initRefuges(config);
        initFrogger(config);
        initProjectile(config);
        mouche = new Mouche(0, 12, 1, 1, 2, 0, "mouche");
        lesElements.add(mouche);
	}

	public static final World getInstance() { // design pattern singleton
		if (instance == null)
			try {
				instance = new World();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return instance;
	}
	
	// Méthodes de configuration des éléments du monde à partir du fichier .json
    public void initWorld(JsonValue config) {
        JsonValue monde = config.get("World");
        this.height = monde.getInt("height");
        this.width = monde.getInt("width");
    }
    public void initRoutes(JsonValue config) {
        JsonValue Route = config.get("Route");
        for (int i = 0 ;i < Route.size ;i++) 
        	lesRoutes.add(new Route(Route.get(i).getFloat("x"),
        							Route.get(i).getFloat("y"), 
        							Route.get(i).getFloat("height"), 
        							Route.get(i).getFloat("width"), 
        							Route.get(i).getFloat("speed"),
        							Route.get(i).getFloat("distance")));
           
    }
    public void initRivieres(JsonValue config) {
        JsonValue Riviere = config.get("Riviere");
        for (int i = 0 ;i < Riviere.size ;i++) 
        	lesRivieres.add(new Riviere(Riviere.get(i).getFloat("x"),
        								Riviere.get(i).getFloat("y"), 
        								Riviere.get(i).getFloat("height"), 
        								Riviere.get(i).getFloat("width"), 
        								Riviere.get(i).getFloat("speed"), 
        								Riviere.get(i).getFloat("distance"),
        								Riviere.get(i).getString("type")));
    }
    public void initRefuges(JsonValue config) {
        JsonValue Refuge = config.get("Refuge");
        for (int i = 0 ;i < Refuge.size ;i++) 
            lesRefuges.add(new Refuge(Refuge.get(i).getFloat("x"),Refuge.get(i).getFloat("y"), Refuge.get(i).getFloat("height"), Refuge.get(i).getFloat("width")));
    }
    public void initFrogger(JsonValue config) {
        JsonValue Frogger = config.get("Frogger");
        this.frog = new Frogger(Frogger.getFloat("x"),Frogger.getFloat("y"), Frogger.getFloat("height"), Frogger.getFloat("width"), Frogger.getFloat("pas"),"frogger", Frogger.getInt("vie"), 0);
        lesElements.add(frog);
    }
    public void initFond(JsonValue config) {
        JsonValue Fond = config.get("Fond");
        lesElements.add(new Fond(Fond.getFloat("x"),Fond.getFloat("y"), Fond.getFloat("height"), Fond.getFloat("width")));
    }
    
    public void initProjectile(JsonValue config) {
        JsonValue projectile = config.get("Projectile");
        distance = projectile.getFloat("distance");
    	nbTirs = projectile.getInt("nbTirs");
    	projHeight = projectile.getFloat("height");
    	projWidth= projectile.getFloat("width");
    }
    
    public void frogInitPos(boolean win) {
    	JsonReader json = new JsonReader();
        JsonValue config = json.parse(Gdx.files.internal("configuration.json"));
        JsonValue Frogger = config.get("Frogger");
        frog.setX(Frogger.getFloat("x"));
        frog.setY(Frogger.getFloat("y"));
        frog.setState("frogger");
        if (!win)
        	frog.setVie(frog.getVie()-1);
    }
    
	public void createFroggerRefuge(Refuge refuge) {
		Frogger f = new Frogger(refuge.getX(), refuge.getY(), frog.getHeight(), frog.getWidth(), frog.getDeplacement(), "frogger", 0, 0);
		refuge.setInside(f);
	}
    
    
    public int getVieBegin() {
    	JsonReader json = new JsonReader();
        JsonValue config = json.parse(Gdx.files.internal("configuration.json"));
        JsonValue Frogger = config.get("Frogger");
        return Frogger.getInt("vie");
    }
    
    public void speedUp() {
    	JsonReader json = new JsonReader();
        JsonValue config = json.parse(Gdx.files.internal("configuration.json"));
        JsonValue LvlSup = config.get("LvlSup");
        int per = LvlSup.getInt("percentage");
        for(Riviere riviere : getLesRivieres()) {
        	riviere.setSpeed((float)(riviere.getSpeed()+(riviere.getSpeed()*(per/100.0))));
        }
        for(Route route : getLesRoutes()) {
        	route.setSpeed((float)(route.getSpeed()+(route.getSpeed()*(per/100.0))));
        }
    }
    
    public float getTirDistance() {
    	return distance;
    }
    public int getNbTirs() {
    	return nbTirs;
    }
    public float getHeightProjectile() {
    	return projHeight;
    }
    public float getWidthProjectile() {
    	return projWidth;
    }
       
	// Getters
	public ArrayList<GameElement> getLesElements() {
		return lesElements;
	}
	
	public Frogger getFrog() {
		return this.frog;
	}
	public void setFrog(Frogger frog) {
		this.frog = frog;
	}

	public ArrayList<Route> getLesRoutes() {
		return lesRoutes;
	}

	public ArrayList<Riviere> getLesRivieres() {
		return lesRivieres;
	}

	public ArrayList<Refuge> getLesRefuges() {
		return lesRefuges;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Mouche getMouche() {
		return mouche;
	}
	
	

}
