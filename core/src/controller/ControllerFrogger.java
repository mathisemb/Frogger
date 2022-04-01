package controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import model.Frogger;
import model.GameElementLineaire;
import model.Projectile;
import model.Refuge;
import model.Riviere;
import model.Route;
import model.Vehicule;
import model.World;
import vue.Debug;

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
	boolean win;
	
	boolean flotte;
	boolean surTortue;
	Riviere laRiviere;
	
	float lifeTime; // duree pendant laquelle le frogger est reste vivant utilisee pour le score
	
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
		this.timeBetweenJump = (float)0.4;
		this.nbFrames = 6;
		this.animLenght = (float)0.2;
		this.frameLenght = animLenght/nbFrames;
		deltaPos = World.getInstance().getFrog().getDeplacement()/nbFrames;
		
		// variable
		this.frameTime = 0;
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
		
		lifeTime = 0;
		
		win = false;
		
		flotte = false;
		laRiviere = null;
	}

	public void majFrogger(float delta, Frogger frog) { // methode appelee dans WorldRenderer pour mettre a jour frogger
		// on suppose que frogger n'est pas mort
		ecrase = false;
		noye = false;
		win = false;
		
		lifeTime+=delta;
		
		if (Gdx.input.isKeyJustPressed(Keys.A)) {
			Debug.getInstance().setAfficherGrille(!Debug.getInstance().afficherGrille);
		}
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			Debug.getInstance().setCollisionsVoiture(!Debug.getInstance().collisionsVoiture);
		}
		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			Debug.getInstance().setCollisionEau(!Debug.getInstance().collisionEau);
		}
		
		// --------------- Mouvement du frogger lorsqu'il est sur un tronc ou des tortues ---------------
		if (!isMoving) {
			if (Debug.getInstance().collisionEau) {
				for(Riviere riviere : World.getInstance().getLesRivieres()) {
					
					if (frog.getRectangle().overlaps(riviere.getRectangle())) {
						noye = true; // si frogger est au niveau d'une riviere on suppose qu'il est noye
						//surTortue = false;
						flotte = false;
					}
					
					for(GameElementLineaire elem : riviere.getLesElements()) {
						if (frog.getRectangle().overlaps(elem.getRectangle())) { // s'il est sur un tronc alors il n'est plus noye
							flotte = true;
							noye = false;
							laRiviere = riviere;
						}				
					}
				}
			}

			if (flotte) {
				if (onWaterinScreen(delta*laRiviere.getSpeed(), delta*laRiviere.getSpeed())) frog.setX(frog.getX()+delta*laRiviere.getSpeed());
				flotte = false;
			}
			
			if (Debug.getInstance().collisionsVoiture) {
				// On verifie que le frogger n'est pas ecrase pas une voiture
				for(Route route : World.getInstance().getLesRoutes()) {
					for(Vehicule v : route.getLesVehicules())
						if (frog.getRectangle().overlaps(v.getRectangle())) ecrase = true; // s'il touche une voiture alors il est ecrase
				}
				
				collisionProjectile(frog);
			}
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
		
		// ----------------- METHODE A : par un appui sur les fleches du clavier -----------------
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (!this.nextIsHerbe(frog))
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
		
		for(Refuge refuge : World.getInstance().getLesRefuges()) {
			if (World.getInstance().getFrog().getY()==refuge.getY()) {
				if ( World.getInstance().getFrog().getX() + (World.getInstance().getFrog().getWidth()/2) < refuge.getX() + refuge.getWidth() ) {
					if ( World.getInstance().getFrog().getX() + (World.getInstance().getFrog().getWidth()/2) > refuge.getX()) {
						if (!refuge.isOccupied() && !refuge.moucheIsHere()) {
							System.out.println("Refuge atteint avec succès");
							win = true;
							World.getInstance().frogInitPos(true); // on place le frogger a sa pos initiale sans enlever de vie
							//World.getInstance().createFroggerRefuge(refuge); // on cree un frogger dans le refuge
							int s = World.getInstance().getFrog().getScore();
							World.getInstance().getFrog().setScore(s+100-(int)(lifeTime));
							lifeTime = 0;
							World.getInstance().speedUp();
						}
						else {
							if (refuge.moucheIsHere()) {
								System.out.println("Refuge atteint avec succès + mouche mangée");
								win = true;
								World.getInstance().frogInitPos(true);
								int s = World.getInstance().getFrog().getScore();
								World.getInstance().getFrog().setScore(s+100-(int)(lifeTime));
								World.getInstance().getMouche().hide();
								World.getInstance().getFrog().setVie(World.getInstance().getFrog().getVie()+1);
								World.getInstance().speedUp();
								lifeTime = 0;
							}
						}
					}
					else {
						if (!refuge.moucheIsHere())
							frog.setY(frog.getY()-frog.getDeplacement());
					}
				}
			}
		}
		
		//System.out.println("lifeTime = " + lifeTime);

	    if ((ecrase || noye) && !win) {
	    	isMoving = false;
	    	isMovingUp = false;
	    	isMovingDown = false;
	    	isMovingRight = false;
	    	isMovingLeft = false;
	    	World.getInstance().frogInitPos(false);
	    	lifeTime = 0;
	    }
	    
	    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
	    	if (World.getInstance().getFrog().getLesProjectiles().size < World.getInstance().getNbTirs()) {
	    		if (frog.isNextProjPossible()) {
	    			World.getInstance().getFrog().getLesProjectiles().add(new Projectile(World.getInstance().getFrog()));
	    		}
	    			
	    	}
	    }
	    for (Projectile proj : World.getInstance().getFrog().getLesProjectiles()) {
	    	if (proj.distanceParcourue()>World.getInstance().getTirDistance())
	    		World.getInstance().getFrog().getLesProjectiles().removeValue(proj, false);
	    	else
	    		proj.majProjectile();
	    }
		
		// ----------------- METHODE B : par un appui sur lâ€™Ã©cran sÃ©parÃ© en 4 zones -----------------
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
	}
	
	public void wantToJump(String Direction, Frogger frog, float delta) {
	    switch(Direction) {
	        case "UP":
	        	if (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight()) { // frogger ne depasse pas de lecran
					if (Gdx.input.isKeyJustPressed(Keys.UP)) {
						if (!isMoving) { // s'il n'est pas deja en train de sauter il peut commencer
							isMovingUp = true;
							next = frog.getY()+frog.getDeplacement();
							frog.setDirection(180);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
							frog.setScore(frog.getScore()+10);
						}
					}
					else {
						waitTime+=delta;
						//System.out.println("waitTime = "+waitTime);
						if (waitTime>timeBetweenJump && !isMovingUp) {
							isMovingUp = true;
							next = frog.getY()+frog.getDeplacement();
							frog.setDirection(180);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
							frog.setScore(frog.getScore()+10);
						}
					}
	        	}
	            break;
	        case "DOWN":
	        	if (frog.getY()-frog.getDeplacement()>=0) { // frogger ne depasse pas de lecran
					if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
						if (!isMoving) { // s'il n'est pas deja en train de sauter il peut commencer
							isMovingDown = true;
							next = frog.getY()-frog.getDeplacement();
							frog.setDirection(0);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
							frog.setScore(frog.getScore()-10);
						}
					}
					else {
						waitTime+=delta;
						//System.out.println("waitTime = "+waitTime);
						if (waitTime>timeBetweenJump && !isMovingDown) {
							isMovingDown = true;
							next = frog.getY()-frog.getDeplacement();
							frog.setDirection(0);
							cptImage = 1;
							frameTime = 0;
							animTime = 0;
							waitTime=0;
							frog.setScore(frog.getScore()-10);
						}
					}
	        	}
	            break;
	        case "RIGHT":
	        	if (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth()) { // frogger ne depasse pas de lecran
					if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
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
					else {
						waitTime+=delta;
						//System.out.println("waitTime = "+waitTime);
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
	        	}
	            break;
	        case "LEFT":
	        	if (frog.getX()-frog.getDeplacement()>=0) { // frogger ne depasse pas de lecran
					if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
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
					else {
						waitTime+=delta;
						//System.out.println("waitTime = "+waitTime);
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
	        	}
	            break;
	        default: break;
		}
	}
	
	public void animationFrogger(Frogger frog, float delta) {
		if (isMovingUp) { // il faut deplacer le frogger et l'animation
			frog.setDirection(180);
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
		if (isMovingDown) { // il faut deplacer le frogger et l'animation
			frog.setDirection(0);
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
		if (isMovingRight) { // il faut deplacer le frogger et l'animation
			frog.setDirection(90);
			frameTime+=delta;
			animTime+=delta;
			if (frameTime>frameLenght && cptImage<6) { // il faut changer l'image et augmenter la position du frogger
				frog.setX((float)(frog.getX()+deltaPos));
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
		if (isMovingLeft) { // il faut deplacer le frogger et l'animation
			frog.setDirection(270);
			frameTime+=delta;
			animTime+=delta;
			if (frameTime>frameLenght && cptImage<6) { // il faut changer l'image et augmenter la position du frogger
				frog.setX((float)(frog.getX()-deltaPos));
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
	
	public boolean nextMoveIsInScreen() {
		Frogger frog = World.getInstance().getFrog();
    	return (frog.getY()+frog.getHeight()+frog.getDeplacement()<=World.getInstance().getHeight())
    			&& (frog.getY()-frog.getDeplacement()>=0)
    			&& (frog.getX()+frog.getWidth()+frog.getDeplacement()<=World.getInstance().getWidth())
    			&& (frog.getX()-frog.getDeplacement()>=0);

	}
	
	public boolean onWaterinScreen(float dx, float dy) {
		Frogger frog = World.getInstance().getFrog();
    	return (frog.getY()+frog.getHeight()+dy<=World.getInstance().getHeight())
    			&& (frog.getY()-dy>=0)
    			&& (frog.getX()+frog.getWidth()+dx<=World.getInstance().getWidth())
    			&& (frog.getX()-dx>=0);

	}
	
	public void collisionProjectile(Frogger frog) {
        for (Projectile Proj : frog.getLesProjectiles()) {
            for(Route route : World.getInstance().getLesRoutes()) {
                for (Vehicule Veh : route.getLesVehicules()){
                	
                    if (Proj.getRectangle().overlaps(Veh.getRectangle())) {
                    	System.out.println("ok");
                    	route.getLesVehicules().removeValue(Veh, true);
                        frog.getLesProjectiles().removeValue(Proj, true);
                    }
    
                }
            }
        }
	}
	
	public boolean nextIsHerbe(Frogger frog) {
		if (frog.getY() + frog.getDeplacement() >= World.getInstance().getLesRefuges().get(0).getY()) {
			for(Refuge refuge : World.getInstance().getLesRefuges()) {
				if ( World.getInstance().getFrog().getX() + (World.getInstance().getFrog().getWidth()/2) < refuge.getX() + refuge.getWidth() ) {
					if ( World.getInstance().getFrog().getX() + (World.getInstance().getFrog().getWidth()/2) > refuge.getX()) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

}
