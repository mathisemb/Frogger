package vue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import model.World;

public class GameScreen implements Screen {
	
	MyGdxGame game;
	Debug debug;
	
	public GameScreen(MyGdxGame game) {
		this.game = game;
		this.debug = new Debug(13,9,50);
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
	    	int s = World.getInstance().getFrog().getScore();
	    	World.getInstance().getFrog().setScore(0);
			this.dispose();
			game.setScreen(new GameOverScreen(game, s));
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
