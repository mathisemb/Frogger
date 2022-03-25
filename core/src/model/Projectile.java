package model;

public class Projectile extends DynamicGameElement {
	Frogger frog;
	float distance;
	int nbTirs;
	float initialPos;
    
	public Projectile(Frogger frog) {
		super(frog.getX(), frog.getY(), World.getInstance().getHeightProjectile(), World.getInstance().getWidthProjectile(), (float)0.1, frog.getDirection(), "projectile");
		this.frog = frog;
		distance = World.getInstance().getTirDistance();
		nbTirs= World.getInstance().getNbTirs();
		if (frog.getDirection()==180 ||frog.getDirection()==0 )
			initialPos = frog.getY();
		else
			initialPos = frog.getX();
	}
	
	public void majProjectile() {
		switch(getDirection()) {
			case 180 : // vers le haut
				this.y+=deplacement;
				break;
			case 0 : // vers le bas
				this.y-=deplacement;
				break;
			case 90 : // vers la droite
				this.x+=deplacement;
				break;
			case 270 : // vers la gauche
				this.x-=deplacement;
				break;
			default:break;
		}
	}
	
	public float distanceParcourue() {
		switch(getDirection()) {
			case 180 : // vers le haut
				return getY() - initialPos;
			case 0 : // vers le bas
				return initialPos - getY();
			case 90 : // vers la droite
				return getX() - initialPos;
			default : // vers la gauche
				return initialPos - getX();
		}
	}

}
