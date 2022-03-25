package vue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import model.Refuge;
import model.Riviere;
import model.Route;
import model.World;

public class GameScreen implements Screen {
	
	MyGdxGame game;
	Debug debug;
	
	boolean allOccupied;
	
	public GameScreen(MyGdxGame game) {
		this.game = game;
		this.debug = new Debug(13,9,50);
		this.allOccupied = false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.worldRenderer.render(game.batch, delta);
	    if (World.getInstance().getFrog().getVie() <= 0) {
	    	this.dispose();
	    	
	    	// On supprime les obstacles et les frogger dans les refuges
	        for(Route route : World.getInstance().getLesRoutes()) {
	        	route.getLesVehicules().clear();
	        }
	        for(Riviere riviere : World.getInstance().getLesRivieres()) {
	        	riviere.getLesElements().clear();
	        }
			for(Refuge refuge : World.getInstance().getLesRefuges()) {
				if (refuge.isOccupied()) {
					refuge.setInside(null);
				}
			}
			
	    	int s = World.getInstance().getFrog().getScore(); // opn récupère le score pour l'afficher dans le GameOverScreen
	    	World.getInstance().getFrog().setScore(0);
			game.setScreen(new GameOverScreen(game, s));
	    }
	    
	    // On vérifie si le joueur a gagné
	    allOccupied = true;
	    for(Refuge refuge : World.getInstance().getLesRefuges()) {
			if (!refuge.isOccupied()) { // si un seul des refuges n'est pas occupe alors allOccupied est faux
				allOccupied = false;
			}
		}
	    if (allOccupied) {
	    	this.dispose();
	    	
	    	// On supprime les obstacles et les frogger dans les refuges
	        for(Route route : World.getInstance().getLesRoutes()) {
	        	route.getLesVehicules().clear();
	        }
	        for(Riviere riviere : World.getInstance().getLesRivieres()) {
	        	riviere.getLesElements().clear();
	        }
			for(Refuge refuge : World.getInstance().getLesRefuges()) {
				if (refuge.isOccupied()) {
					refuge.setInside(null);
				}
			}
			
	    	int s = World.getInstance().getFrog().getScore(); // on récupère le score pour l'afficher dans le GameOverScreen
	    	World.getInstance().getFrog().setScore(0);
			game.setScreen(new WinScreen(game, s));
	    }
	    
		game.batch.end();
		if (game.debug) debug.afficherDebug();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
