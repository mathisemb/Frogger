package controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;

import model.Frogger;
import model.GameElementLineaire;
import model.Riviere;
import model.Route;
import model.Tortue;
import model.Vehicule;
import model.World;

public class ControllerFrogger {
	Frogger frog;
	float mvtTime;
	float timeBetweenJump;
	float cptBetweenJump;
	boolean allowed;
	
	float next; // prochaine position
	float frameLenght; // durÈe d'affichage d'une image de l'animation
	float animLenght; // durÈe de l'animation entiËre
	int nbFrames; // nombre d'image dans l'animation
	
	int cpt;
	boolean isMoving;
	boolean isMovingUp;
	boolean isMovingDown;
	boolean isMovingRight;
	boolean isMovingLeft;
	
	float speed;
	
	boolean ecrase;
	boolean noye;
	
	
	
	
	/*
	1 animation mouvement = 6 images
	1 distance mouvement = frog.getDeplacement()
	=> on divise frog.getDeplacement() en 6 pour avoir la distance que parcourt chaque image
	  frog.getDeplacement()
	<----------------------->
	  1   2   3   4   5   6
	| _ | _ | _ | _ | _ | _ |
	========================>
	        frameLenght
	 */
	int animationState; // = 1, 2, 3, 4, 5 ou 6
	
	public ControllerFrogger(Frogger frog) {
		this.frog = frog;
		this.isMoving = false;
		this.timeBetweenJump = (float)0.1;
		this.nbFrames = 6;
		this.animLenght = (float)0.06;
		this.frameLenght = animLenght/nbFrames;
		this.mvtTime = 0;
		
		cpt = 1;
		isMovingUp = false;
		isMovingDown = false;
		isMovingRight = false;
		isMovingLeft = false;
		
		ecrase = false;
		noye = false;
		
		speed = frog.getDeplacement()/6;
		
		allowed = true;
	}

	public void majFrogger(float delta) { // methode appelee dans WorldRenderer pour mettre a jour frogger
		
		ecrase = false;
		noye = false;
		
		if (!isMoving) {
			for(Riviere riviere : World.getInstance().getLesRivieres()) {
				if (frog.getRectangle().overlaps(riviere.getRectangle()))
						noye = true;
				
				for(GameElementLineaire elem : riviere.getLesElements()) {
					if (elem instanceof Tortue) {
						if (((Tortue) elem).isFirst()) { // on cr√©er un rectangle qui englobe toutes les tortues du groupe
							Rectangle rectangle = new Rectangle(elem.getX(), elem.getY(), ((Tortue) elem).getNbTortues(), elem.getHeight());
							if (frog.getRectangle().overlaps(rectangle)) {
								frog.setX(frog.getX()+delta*riviere.getSpeed());
								noye = false;
							}
						}
					}
					else { // Tronc
						if (frog.getRectangle().overlaps(elem.getRectangle())) {
							frog.setX(frog.getX()+delta*riviere.getSpeed());
							noye = false;
						}
					}
				}
			}	
		}
		
		for(Route route : World.getInstance().getLesRoutes()) {
			for(Vehicule v : route.getLesVehicules()) {
				if (frog.getRectangle().overlaps(v.getRectangle()))
					ecrase = true;
			}
		}

		// ----------------- METHODE A : par un appui sur les fl√®ches du clavier -----------------
		/*
		if (Gdx.input.isKeyPressed(Keys.UP)) { // HAUT
			if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) {
				if (!isMoving) frog.deplacerHaut();
				isMoving = true;
				if (mvtTime < timeBetweenJump)
					mvtTime += delta;
				else {
					mvtTime = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) { // BAS
			if (frog.getY()-frog.getDeplacement()>=0) {
				if (!isMoving) frog.deplacerBas();
				isMoving = true;
				if (mvtTime < timeBetweenJump)
					mvtTime += delta;
				else {
					mvtTime = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) { // GAUCHE
			if (frog.getX()-frog.getDeplacement()>=0) {
				if (!isMoving) frog.deplacerGauche();
				isMoving = true;
				if (mvtTime < timeBetweenJump)
					mvtTime += delta;
				else {
					mvtTime = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) { // DROITE
			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
				if (!isMoving) frog.deplacerDroite();
				isMoving = true;
				if (mvtTime < timeBetweenJump)
					mvtTime += delta;
				else {
					mvtTime = 0;
					isMoving = false;
				}
			}
		}
		
		if (!(Gdx.input.isTouched()) && 
			(!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) &&
			!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))) {
			isMoving = false;
			mvtTime = 0;
		}
		*/
		if (isMovingUp || isMovingDown || isMovingRight || isMovingLeft) {
			isMoving = true;
		}
		else {
			isMoving = false;
			frog.setState("frogger6");
		}
		if (isMoving) {
			cptBetweenJump+=delta;
			mvtTime+=delta;
		}
		if (cptBetweenJump<timeBetweenJump) {
			allowed = false;
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP) && allowed) { // HAUT
			if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) {
				if (!isMoving) {
					frog.setDirection(180);
					next = frog.getY()+frog.getDeplacement();
					isMovingUp = true;
				}
			}
		}
		if (isMovingUp) {
			mvtTime += delta;
			if (frog.getY()+speed>next) { // on a atteint next
				frog.setY((float) (next));
				cpt = 0;
				isMovingUp = false;
			}
			else { // on continue ‡ avancer
				if (frog.getY()<next) {
					frog.setY((float) (frog.getY()+speed));
					if (mvtTime>animLenght && cpt<6) {
						cpt++;
						mvtTime = 0;
					}
					frog.setState("frogger"+cpt);
				}
				else {
					cpt = 0;
					isMovingUp = false;
				}
			}
		}
		
		
		if (Gdx.input.isKeyPressed(Keys.DOWN) && allowed) { // BAS
			if (frog.getY()-frog.getDeplacement()>=0) {
				if (!isMoving) {
					frog.setDirection(0);
					next = frog.getY()-frog.getDeplacement();
					isMovingDown = true;
				}
			}
		}
		if (isMovingDown) {
			mvtTime += delta;
			if (frog.getY()-speed<next) {
				frog.setY((float) (next));
				cpt = 0;
				isMovingDown = false;
			}
			else {
				if (frog.getY()>next) {
					frog.setY((float) (frog.getY()-speed));
					if (mvtTime>animLenght && cpt<6) {
						cpt++;
						mvtTime = 0;
					}
					frog.setState("frogger"+cpt);
				}
				else {
					cpt = 0;
					isMovingDown = false;
				}
			}
		}
		
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT) && allowed) { // RIGHT
			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
				if (!isMoving) {
					frog.setDirection(90);
					next = frog.getX()+frog.getDeplacement();
					isMovingRight = true;
				}
			}
		}
		if (isMovingRight) {
			mvtTime += delta;
			if (frog.getX()+speed>next) {
				frog.setX((float) (next));
				cpt = 0;
				isMovingRight = false;
			}
			else {
				if (frog.getX()<next) {
					frog.setX((float) (frog.getX()+speed));
					if (mvtTime>animLenght && cpt<6) {
						cpt++;
						mvtTime = 0;
					}
					frog.setState("frogger"+cpt);
				}
				else {
					cpt = 0;
					isMovingRight = false;
				}
			}
		}
		
		
		if (Gdx.input.isKeyPressed(Keys.LEFT) && allowed) { // LEFT
			if (frog.getX()-frog.getDeplacement()>=0) {
				if (!isMoving) {
					frog.setDirection(270);
					next = frog.getX()-frog.getDeplacement();
					isMovingLeft = true;
				}
			}
		}
		if (isMovingLeft) {
			mvtTime += delta;
			if (frog.getX()-speed<next) {
				frog.setX((float) (next));
				cpt = 0;
				isMovingLeft = false;
			}
			else {
				if (frog.getX()>next) {
					frog.setX((float) (frog.getX()-speed));
					if (mvtTime>animLenght && cpt<6) {
						cpt++;
						mvtTime = 0;
					}
					frog.setState("frogger"+cpt);
				}
				else {
					cpt = 0;
					isMovingLeft = false;
				}
			}
		}
		
		/*
		if (!(Gdx.input.isTouched()) && 
			(!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) &&
			!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))) {
			isMoving = false;
			mvtTime = 0;
		}
		*/
		
		
		// ----------------- METHODE B : par un appui sur l‚Äô√©cran s√©par√© en 4 zones -----------------
        if (Gdx.input.isTouched()) {
            int w = Gdx.graphics.getWidth();
            int h = Gdx.graphics.getHeight();
            float x =  Gdx.input.getX();
            float y =  Gdx.input.getY();
            
            if( (h/w)*y<=x && x<=(-h/w)*y+h ) { // HAUT
    			if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) {
    				if (!isMoving) frog.deplacerHaut();
    				isMoving = true;
    				if (mvtTime < timeBetweenJump)
    					mvtTime += delta;
    				else {
    					mvtTime = 0;
    					isMoving = false;
    				}
    			}
            }
            else if( (h/w)*y>=x && x>=(-h/w)*y+h ) { // BAS
    			if (frog.getY()-frog.getDeplacement()>=0) {
    				if (!isMoving) frog.deplacerBas();
    				isMoving = true;
    				if (mvtTime < timeBetweenJump)
    					mvtTime += delta;
    				else {
    					mvtTime = 0;
    					isMoving = false;
    				}
    			}
            }
            else if ( (h/w)*x<=y && y<=(-h/w)*x+h) { // GAUCHE
    			if (frog.getX()-frog.getDeplacement()>=0) {
    				if (!isMoving) frog.deplacerGauche();
    				isMoving = true;
    				if (mvtTime < timeBetweenJump)
    					mvtTime += delta;
    				else {
    					mvtTime = 0;
    					isMoving = false;
    				}
    			}
            }
            else if ( (h/w)*x>=y && y>=(-h/w)*x+h) { // DROITE
    			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
    				if (!isMoving) frog.deplacerDroite();
    				isMoving = true;
    				if (mvtTime < timeBetweenJump)
    					mvtTime += delta;
    				else {
    					mvtTime = 0;
    					isMoving = false;
    				}
    			}
            }
        }
        
        if (ecrase || noye) frog.setState("deadFrogger");
        
	}
}
