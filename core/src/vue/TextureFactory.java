package vue;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

import model.GameElement;

public class TextureFactory {
	//private Map<Class<?>, Texture> lesTextures;
	private Map<String, Texture> lesTextures;
	
	private Texture intro;
	private Texture fond;
	private Texture frogger1, frogger2, frogger3, frogger4, frogger5, frogger6;
	private Texture voitureBleue;
	private Texture voitureVerte;
	private Texture voitureJaune;
	private Texture pompier;
	private Texture camion;
	private Texture tortue;
	private Texture tronc1;
	private Texture tronc2;
	private Texture tronc3;
	private Texture mouche;
	private Texture dead;
	
	private static TextureFactory instance;
	
	private TextureFactory() { // chargement des textures
		fond = new Texture("Fond.png");
		frogger1 = new Texture("frogger/f_02.png");
		frogger2 = new Texture("frogger/f_03.png");
		frogger3 = new Texture("frogger/f_04.png");
		frogger4 = new Texture("frogger/f_05.png");
		frogger5 = new Texture("frogger/f_06.png");
		frogger6 = new Texture("frogger/f_07.png");
		
		dead = new Texture("items/i_03.png");

		// Les véhicules
		camion = new Texture("vehicle/veh_01.png");
		pompier = new Texture("vehicle/veh_02.png");
		voitureJaune = new Texture("vehicle/veh_03.png");
		voitureBleue = new Texture("vehicle/veh_04.png");
		voitureVerte = new Texture("vehicle/veh_05.png");
		
		tortue = new Texture("turtle/move/tm_02.png");
		tronc1 = new Texture("log/log_01.png");
		tronc2 = new Texture("log/log_02.png");
		tronc3 = new Texture("log/log_03.png");
		mouche = new Texture("mouche/mouche.png");
		
		intro = new Texture("intro.png");
		
		/* Hashmap de class, texture pour éviter de faire des instanceof
		lesTextures = new HashMap<>();
	    lesTextures.put(Fond.class, fond);
	    lesTextures.put(Frogger.class, frog);
	    lesTextures.put(Vehicule.class, voitureBleue);
	    lesTextures.put(Tortue.class, tortue);
	    lesTextures.put(Tronc.class, tronc);
	    lesTextures.put(Mouche.class, mouche);
	    */
		
		// Hashmap de class, texture pour éviter de faire des instanceof
		lesTextures = new HashMap<>();
	    lesTextures.put("fond", fond);
	    lesTextures.put("frogger", frogger1);
	    lesTextures.put("frogger1", frogger1);
	    lesTextures.put("frogger2", frogger2);
	    lesTextures.put("frogger3", frogger3);
	    lesTextures.put("frogger4", frogger4);
	    lesTextures.put("frogger5", frogger5);
	    lesTextures.put("frogger6", frogger6);
	    lesTextures.put("deadFrogger", dead);
	    lesTextures.put("voitureJaune", voitureJaune);
	    lesTextures.put("voitureBleue", voitureBleue);
	    lesTextures.put("voitureVerte", voitureVerte);
	    lesTextures.put("camion", camion);
	    lesTextures.put("pompier", pompier);
	    lesTextures.put("tortue", tortue);
	    lesTextures.put("tronc1", tronc1);
	    lesTextures.put("tronc2", tronc2);
	    lesTextures.put("tronc3", tronc3);
	    lesTextures.put("mouche", mouche);
	}

	
	public Texture getTexture(GameElement elem) {
		//return (Texture)lesTextures.get(elem.getClass());
		return (Texture)lesTextures.get(elem.getState());
	}
	
	public static final TextureFactory getInstance() { // design pattern singleton
		if (instance == null)
			instance = new TextureFactory();
		return instance;
	}
	
	public Texture getTextureFond() {
		return fond;
	}
	
	public Texture getTextureFrogger() {
		return frogger1;
	}

	public Texture getTextureVoitureBleue() {
		return voitureBleue;
	}
	public Texture getTextureVoitureVerte() {
		return voitureVerte;
	}
	public Texture getTextureVoitureJaune() {
		return voitureJaune;
	}
	public Texture getTexturePompier() {
		return pompier;
	}
	public Texture getTextureCamion() {
		return camion;
	}
	public Texture getTextureTronc() {
		return tronc1;
	}
	public Texture getTextureTortue() {
		return tortue;
	}
	public Texture getTextureMouche() {
		return mouche;
	}
	public Texture getTextureIntro() {
		return intro;
	}	
}
