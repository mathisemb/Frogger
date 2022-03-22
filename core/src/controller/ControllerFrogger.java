package controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;

import model.Frogger;
import model.GameElementLineaire;
import model.Riviere;
import model.Tortue;
import model.World;

public class ControllerFrogger {
	Frogger frog;
	Boolean isMoving;
	float time;
	float intervalle;
	
	float next;
	float frameLenght;
	
	int cpt;
	Boolean isMovingUp;
	Boolean isMovingDown;
	Boolean isMovingRight;
	Boolean isMovingLeft;
	
	public ControllerFrogger(Frogger frog) {
		this.frog = frog;
		this.isMoving = false;
		this.intervalle = (float)0.5;
		this.frameLenght = (float)0.01;
		this.time = 0;
		
		cpt = 1;
		isMovingUp = false;
		isMovingDown = false;
		isMovingRight = false;
		isMovingLeft = false;
	}

	public void majFrogger(float delta) { // methode appelee dans WorldRenderer pour mettre a jour frogger
		
		
		for(Riviere riviere : World.getInstance().getLesRivieres()) {
			for(GameElementLineaire elem : riviere.getLesElements()) {
				if (elem instanceof Tortue) {
					if (((Tortue) elem).isFirst()) { // on créer un rectangle qui englobe toutes les tortues du groupe
						Rectangle rectangle = new Rectangle(elem.getX(), elem.getY(), ((Tortue) elem).getNbTortues(), elem.getHeight());
						if (frog.getRectangle().overlaps(rectangle))
							frog.setX(frog.getX()+delta*riviere.getSpeed());
					}
				}
				else { // Tronc
					if (frog.getRectangle().overlaps(elem.getRectangle()))
						frog.setX(frog.getX()+delta*riviere.getSpeed());
				}
			}
		}

		// ----------------- METHODE A : par un appui sur les flèches du clavier -----------------
		/*
		if (Gdx.input.isKeyPressed(Keys.UP)) { // HAUT
			if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) {
				if (!isMoving) frog.deplacerHaut();
				isMoving = true;
				if (time < intervalle)
					time += delta;
				else {
					time = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) { // BAS
			if (frog.getY()-frog.getDeplacement()>=0) {
				if (!isMoving) frog.deplacerBas();
				isMoving = true;
				if (time < intervalle)
					time += delta;
				else {
					time = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) { // GAUCHE
			if (frog.getX()-frog.getDeplacement()>=0) {
				if (!isMoving) frog.deplacerGauche();
				isMoving = true;
				if (time < intervalle)
					time += delta;
				else {
					time = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) { // DROITE
			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
				if (!isMoving) frog.deplacerDroite();
				isMoving = true;
				if (time < intervalle)
					time += delta;
				else {
					time = 0;
					isMoving = false;
				}
			}
		}
		
		if (!(Gdx.input.isTouched()) && 
			(!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) &&
			!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))) {
			isMoving = false;
			time = 0;
		}
		*/
		
		if (Gdx.input.isKeyPressed(Keys.UP)) { // HAUT
			frog.setDirection(180);
			if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) {
				if (!isMovingUp) {
					next = frog.getY()+frog.getDeplacement();
					isMovingUp = true;
				}
			}
		}
		if (isMovingUp) {
			time += delta;
			if (frog.getY()<=next) {
				frog.setY((float) (frog.getY()+0.1));
				if (time>frameLenght && cpt<6) {
					cpt++;
					time = 0;
				}
				frog.setState("frogger"+cpt);
			}
			else {
				cpt = 0;
				isMovingUp = false;
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.DOWN)) { // BAS
			if (frog.getY()-frog.getDeplacement()>=0) {
				if (!isMovingDown) frog.deplacerBas();
				isMovingDown = true;
				if (time < intervalle)
					time += delta;
				else {
					time = 0;
					isMovingDown = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) { // GAUCHE
			if (frog.getX()-frog.getDeplacement()>=0) {
				if (!isMovingDown) frog.deplacerGauche();
				isMovingDown = true;
				if (time < intervalle)
					time += delta;
				else {
					time = 0;
					isMovingDown = false;
				}
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) { // DROITE
			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
				if (!isMovingRight) frog.deplacerDroite();
				isMovingRight = true;
				if (time < intervalle)
					time += delta;
				else {
					time = 0;
					isMovingRight = false;
				}
			}
		}
		
		
		
		
		/*
		if (!(Gdx.input.isTouched()) && 
			(!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) &&
			!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))) {
			isMoving = false;
			time = 0;
		}
		*/
		
		
		// ----------------- METHODE B : par un appui sur l’écran séparé en 4 zones -----------------
        if (Gdx.input.isTouched()) {
            int w = Gdx.graphics.getWidth();
            int h = Gdx.graphics.getHeight();
            float x =  Gdx.input.getX();
            float y =  Gdx.input.getY();
            
            if( (h/w)*y<=x && x<=(-h/w)*y+h ) { // HAUT
    			if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) {
    				if (!isMoving) frog.deplacerHaut();
    				isMoving = true;
    				if (time < intervalle)
    					time += delta;
    				else {
    					time = 0;
    					isMoving = false;
    				}
    			}
            }
            else if( (h/w)*y>=x && x>=(-h/w)*y+h ) { // BAS
    			if (frog.getY()-frog.getDeplacement()>=0) {
    				if (!isMoving) frog.deplacerBas();
    				isMoving = true;
    				if (time < intervalle)
    					time += delta;
    				else {
    					time = 0;
    					isMoving = false;
    				}
    			}
            }
            else if ( (h/w)*x<=y && y<=(-h/w)*x+h) { // GAUCHE
    			if (frog.getX()-frog.getDeplacement()>=0) {
    				if (!isMoving) frog.deplacerGauche();
    				isMoving = true;
    				if (time < intervalle)
    					time += delta;
    				else {
    					time = 0;
    					isMoving = false;
    				}
    			}
            }
            else if ( (h/w)*x>=y && y>=(-h/w)*x+h) { // DROITE
    			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
    				if (!isMoving) frog.deplacerDroite();
    				isMoving = true;
    				if (time < intervalle)
    					time += delta;
    				else {
    					time = 0;
    					isMoving = false;
    				}
    			}
            }
        }
	}
}
