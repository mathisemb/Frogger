package vue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import controller.WorldRenderer;
import model.World;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	WorldRenderer worldRenderer;
	
	boolean debug;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		worldRenderer = new WorldRenderer();
		setScreen(new IntroScreen(this));
		JsonReader json = new JsonReader();
        JsonValue config = json.parse(Gdx.files.internal("configuration.json"));
        JsonValue debug = config.get("Debug");
        World.getInstance().debug = debug.getBoolean("value");
        this.debug = debug.getBoolean("value");
	}

	// render() dans GameScreen
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
