package vue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import model.GameElement;
import model.GameElementLineaire;
import model.Riviere;
import model.Route;
import model.Vehicule;
import model.World;

public class Debug {
	
	ShapeRenderer shapeRenderer;
	int width;
	int height;
	int unit;
	
	Debug(int height, int width, int unit) {
		this.width = width;
		this.height = height;
		this.unit = unit;
		shapeRenderer = new ShapeRenderer();
	}
	
	public void afficherDebug() {
		shapeRenderer.begin(ShapeType.Line);
		/* Grille
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				shapeRenderer.rect(j*unit, i*unit, unit, unit);
			}
		}
		*/
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
		shapeRenderer.end();
	}
}
