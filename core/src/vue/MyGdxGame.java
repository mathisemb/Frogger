package vue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import controller.WorldRenderer;


public class MyGdxGame extends Game {
	SpriteBatch batch;
	WorldRenderer worldRenderer;
	
	boolean debug;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		worldRenderer = new WorldRenderer();
		setScreen(new IntroScreen(this));
	}

	// render() dans GameScreen
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
