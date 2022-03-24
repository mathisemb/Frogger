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
	float animTime;
	float waitTime; // temps ecoule depuis que le joueur a commence a appuyer sur une fleche alors que le frogger etait deja en train de bouger
	float timeBetweenJump; // duree fixe qui determine cb de temps il faut attendre pour refaire un saut si on reste appuye
	boolean allowed; // determine si frogger a le droit de se deplacer
	
	float next; // prochaine position
	float frameTime; // temps durant lequel une frame de l'animation a deja ete affichee
	float frameLenght; // duree d'affichage d'une image de l'animation
	float animLenght; // duree de l'animation entiere
	int nbFrames; // nombre d'image dans l'animation
	
	int cptImage; // numero de la texture a afficher
	boolean isMoving;
	boolean isMovingUp;
	boolean isMovingDown;
	boolean isMovingRight;
	boolean isMovingLeft;
	
	float deltaPos;
	
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
	
	public ControllerFrogger() {
		this.isMoving = false;
		
		// fixe
		this.timeBetweenJump = (float)10;
		this.nbFrames = 6;
		this.animLenght = (float)1;
		this.frameLenght = animLenght/nbFrames;
		deltaPos = World.getInstance().getFrog().getDeplacement()/nbFrames;
		
		// variable
		this.frameTime=0;
		this.animTime = 0;
		this.waitTime = 0;
		
		cptImage = 1; // initialisation
		
		isMovingUp = false;
		isMovingDown = false;
		isMovingRight = false;
		isMovingLeft = false;
		ecrase = false;
		noye = false;

		allowed = true;
	}	     

	public void majFrogger(float delta, Frogger frog) { // methode appelee dans WorldRenderer pour mettre a jour frogger
		// on suppose que frogger n'est pas mort
		ecrase = false;
		noye = false;
		
		// --------------- Mouvement du frogger lorsqu'il est sur un tronc ou des tortues ---------------
		if (!isMoving) {
			for(Riviere riviere : World.getInstance().getLesRivieres()) {
				
				if (frog.getRectangle().overlaps(riviere.getRectangle())) noye = true; // si frogger est au niveau d'une riviere on suppose qu'il est noye
				
				for(GameElementLineaire elem : riviere.getLesElements()) {
					if (elem instanceof Tortue) {
						if (((Tortue) elem).isFirst()) { // on créer un rectangle qui englobe toutes les tortues du groupe
							Rectangle rectangle = new Rectangle(elem.getX(), elem.getY(), ((Tortue) elem).getNbTortues(), elem.getHeight());
							if (frog.getRectangle().overlaps(rectangle)) { // si frogger est sur des tortues alors il n'est plus noye
								frog.setX(frog.getX()+delta*riviere.getSpeed());
								noye = false;
							}
						}
					}
					else { // Tronc
						if (frog.getRectangle().overlaps(elem.getRectangle())) { // s'il est sur un tronc alors il n'est plus noye
							frog.setX(frog.getX()+delta*riviere.getSpeed());
							noye = false;
						}
					}
				}
			}	
		}
		
		// On verifie que le frogger n'est pas ecrase pas une voiture
		for(Route route : World.getInstance().getLesRoutes()) {
			for(Vehicule v : route.getLesVehicules())
				if (frog.getRectangle().overlaps(v.getRectangle())) ecrase = true; // s'il touche une voiture alors il est ecrase
		}
		
		// Si un des quatres booleen de mouvement est vrai alors le boolean general isMoving est vrai
		if (isMovingUp || isMovingDown || isMovingRight || isMovingLeft) {
			isMoving = true;
			allowed = false;
		}
		else { // frogger ne bouge pas
			isMoving = false;
			frog.setState("frogger6"); // pour etre sur de d'avoir toujours la meme texure quand frogger ne bouge pas
			allowed = true; // frogger ne bouge pas donc est autorise se deplacer ou il veut
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			wantToJump("UP", frog, delta);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			wantToJump("DOWN", frog, delta);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			wantToJump("RIGHT", frog, delta);
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			wantToJump("LEFT", frog, delta);
		}
		animationFrogger(frog, delta);


	    if (ecrase || noye) frog.setState("deadFrogger");
		
		/*
		if (!(Gdx.input.isTouched()) && 
			(!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) &&
			!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))) {
			isMoving = false;
			animTime = 0;
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
    				if (animTime < timeBetweenJump)
    					animTime += delta;
    				else {
    					animTime = 0;
    					isMoving = false;
    				}
    			}
            }
            else if( (h/w)*y>=x && x>=(-h/w)*y+h ) { // BAS
    			if (frog.getY()-frog.getDeplacement()>=0) {
    				if (!isMoving) frog.deplacerBas();
    				isMoving = true;
    				if (animTime < timeBetweenJump)
    					animTime += delta;
    				else {
    					animTime = 0;
    					isMoving = false;
    				}
    			}
            }
            else if ( (h/w)*x<=y && y<=(-h/w)*x+h) { // GAUCHE
    			if (frog.getX()-frog.getDeplacement()>=0) {
    				if (!isMoving) frog.deplacerGauche();
    				isMoving = true;
    				if (animTime < timeBetweenJump)
    					animTime += delta;
    				else {
    					animTime = 0;
    					isMoving = false;
    				}
    			}
            }
            else if ( (h/w)*x>=y && y>=(-h/w)*x+h) { // DROITE
    			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
    				if (!isMoving) frog.deplacerDroite();
    				isMoving = true;
    				if (animTime < timeBetweenJump)
    					animTime += delta;
    				else {
    					animTime = 0;
    					isMoving = false;
    				}
    			}
            }
        }
        
		// ----------------- METHODE A : par un appui sur les flèches du clavier -----------------
		/*
		if (Gdx.input.isKeyPressed(Keys.UP)) { // HAUT
			if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) {
				if (!isMoving) frog.deplacerHaut();
				isMoving = true;
				if (animTime < timeBetweenJump)
					animTime += delta;
				else {
					animTime = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) { // BAS
			if (frog.getY()-frog.getDeplacement()>=0) {
				if (!isMoving) frog.deplacerBas();
				isMoving = true;
				if (animTime < timeBetweenJump)
					animTime += delta;
				else {
					animTime = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) { // GAUCHE
			if (frog.getX()-frog.getDeplacement()>=0) {
				if (!isMoving) frog.deplacerGauche();
				isMoving = true;
				if (animTime < timeBetweenJump)
					animTime += delta;
				else {
					animTime = 0;
					isMoving = false;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) { // DROITE
			if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) {
				if (!isMoving) frog.deplacerDroite();
				isMoving = true;
				if (animTime < timeBetweenJump)
					animTime += delta;
				else {
					animTime = 0;
					isMoving = false;
				}
			}
		}
		
		if (!(Gdx.input.isTouched()) && 
			(!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN) &&
			!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))) {
			isMoving = false;
			animTime = 0;
		}
		*/
        
	}
	
	public void wantToJump(String Direction, Frogger frog, float delta) {
	    switch(Direction) {
	        case "UP":
				if (Gdx.input.isKeyJustPressed(Keys.UP)) {
					if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) { // frogger ne depasse pas de lecran
						if (!isMoving) { // s'il n'est pas deja en train de sauter il peut commencer
							isMovingUp = true;
							next = frog.getY()+frog.getDeplacement();
							frog.setDirection(180);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
						}
					}
				}
				else {
					waitTime+=delta;
					System.out.println("waitTime = "+waitTime);
					if (waitTime>timeBetweenJump && !isMovingUp) {
						isMovingUp = true;
						next = frog.getY()+frog.getDeplacement();
						frog.setDirection(180);
						cptImage = 1;
						frameTime = 0;
						animTime = 0;
						waitTime=0;
					}
				}
	            break;
	        case "DOWN":
				if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
					if (frog.getY()-frog.getDeplacement()<=0) { // frogger ne depasse pas de lecran
						if (!isMoving) { // s'il n'est pas deja en train de sauter il peut commencer
							isMovingDown = true;
							next = frog.getY()-frog.getDeplacement();
							frog.setDirection(0);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
						}
					}
				}
				else {
					waitTime+=delta;
					System.out.println("waitTime = "+waitTime);
					if (waitTime>timeBetweenJump && !isMovingDown) {
						isMovingDown = true;
						next = frog.getY()-frog.getDeplacement();
						frog.setDirection(0);
						cptImage = 1;
						frameTime = 0;
						animTime = 0;
						waitTime=0;
					}
				}
	            break;
	        case "RIGHT":
				if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
					if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) { // frogger ne depasse pas de lecran
						if (!isMoving) { // s'il n'est pas deja en train de sauter il peut commencer
							isMovingRight = true;
							next = frog.getX()+frog.getDeplacement();
							frog.setDirection(90);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
						}
					}
				}
				else {
					waitTime+=delta;
					System.out.println("waitTime = "+waitTime);
					if (waitTime>timeBetweenJump && !isMovingRight) {
						isMovingRight = true;
						next = frog.getX()+frog.getDeplacement();
						frog.setDirection(90);
						cptImage = 1;
						frameTime = 0;
						animTime = 0;
						waitTime=0;
					}
				}
	            break;
	        case "LEFT":
				if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
					if (frog.getX()-frog.getDeplacement()>=0) { // frogger ne depasse pas de lecran
						if (!isMoving) { // s'il n'est pas deja en train de sauter il peut commencer
							isMovingLeft = true;
							next = frog.getX()-frog.getDeplacement();
							frog.setDirection(270);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
						}
					}
				}
				else {
					waitTime+=delta;
					System.out.println("waitTime = "+waitTime);
					if (waitTime>timeBetweenJump && !isMovingLeft) {
						isMovingLeft = true;
						next = frog.getX()-frog.getDeplacement();
						frog.setDirection(270);
						cptImage = 1;
						frameTime = 0;
						animTime = 0;
						waitTime=0;
					}
				}
	            break;
	        default: break;
		}
	}
	
	public void animationFrogger(Frogger frog, float delta) {
		if (isMovingUp) { // il faut deplacer le frogger et l'animation
			frameTime+=delta;
			animTime+=delta;
			if (frameTime>frameLenght && cptImage<6) { // il faut changer l'image et augmenter la position du frogger
				frog.setY((float)(frog.getY()+deltaPos));
				cptImage++;
				frameTime = 0;
			}
			if (animTime>animLenght) { // il faut arreter l'animation
				frog.setY((float) (next));
				isMovingUp = false;
				animTime = 0;
				frameTime=0;
			}
			frog.setState("frogger"+cptImage);
		}
		else if (isMovingDown) { // il faut deplacer le frogger et l'animation
			frameTime+=delta;
			animTime+=delta;
			if (frameTime>frameLenght && cptImage<6) { // il faut changer l'image et augmenter la position du frogger
				frog.setY((float)(frog.getY()-deltaPos));
				cptImage++;
				frameTime = 0;
			}
			if (animTime>animLenght) { // il faut arreter l'animation
				frog.setY((float) (next));
				isMovingDown = false;
				animTime = 0;
				frameTime=0;
			}
			frog.setState("frogger"+cptImage);
		}
		else if (isMovingRight) { // il faut deplacer le frogger et l'animation
			frameTime+=delta;
			animTime+=delta;
			if (frameTime>frameLenght && cptImage<6) { // il faut changer l'image et augmenter la position du frogger
				frog.setX((float)(frog.getY()+deltaPos));
				cptImage++;
				frameTime = 0;
			}
			if (animTime>animLenght) { // il faut arreter l'animation
				frog.setX((float) (next));
				isMovingRight = false;
				animTime = 0;
				frameTime=0;
			}
			frog.setState("frogger"+cptImage);
		}
		else if (isMovingLeft) { // il faut deplacer le frogger et l'animation
			frameTime+=delta;
			animTime+=delta;
			if (frameTime>frameLenght && cptImage<6) { // il faut changer l'image et augmenter la position du frogger
				frog.setX((float)(frog.getY()-deltaPos));
				cptImage++;
				frameTime = 0;
			}
			if (animTime>animLenght) { // il faut arreter l'animation
				frog.setX((float) (next));
				isMovingLeft = false;
				animTime = 0;
				frameTime=0;
			}
			frog.setState("frogger"+cptImage);
		}
	}
}
