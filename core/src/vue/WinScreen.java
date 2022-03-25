package vue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import model.World;

public class WinScreen implements Screen {
	
	MyGdxGame game;
	int score;
	
   BitmapFont fontScore;
   CharSequence strScore = "GAGNE ! VOTRE SCORE : ";
	
	public WinScreen(MyGdxGame game, int score) {
		this.game = game;
		this.score = score;
		fontScore = new BitmapFont();
		fontScore.getData().setScale(2, 2);
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
		game.batch.draw(TextureFactory.getInstance().getTextureIntro(), 0, 0);
		fontScore.draw(game.batch, strScore +  Integer.toString(score), 25, 120);
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			this.dispose();
			World.getInstance().getFrog().setVie(World.getInstance().getVieBegin());
			game.setScreen(new GameScreen(game));
		}
		game.batch.end();
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