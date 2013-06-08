package scenes.Main;

import logic.Engine;
import logic.level.Level;

import scenes.Main.ui.KeyDisplay;
import scenes.Main.ui.ScoreField;
import scenes.Main.ui.StatBars;

import EntitySystems.InputSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Scene implements Screen {

	OrthographicCamera camera;
	private boolean ready;
	
	private Sprite statBars;
	private Sprite scoreField;
	//private FieldDisplay field;
	private KeyDisplay keydisp;
	
	private Level level;
	
	Music bgm;
	
	private SpriteBatch batch;
	
	public Scene()
	{
		camera = new OrthographicCamera(240, 320);
		camera.setToOrtho(false);
		batch = new SpriteBatch();
	}
	
	@Override
	public void dispose() {
		unload();
	}

	@Override
	public void hide() {
		unload();
	}
	
	@Override
	public void pause() {}

	@Override
	public void render(float delta) {
		
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		//wait for assets to load
		if (Engine.assets.update())
		{
			if (!ready)
			{
				create();
				ready = true;
			}

			camera.update();
			level.advance(delta);
			
			
			level.draw(batch);
			
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			statBars.draw(batch);
			scoreField.draw(batch);
			keydisp.draw(batch, 1.0f);
			batch.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, 240, 320);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		load();
	}
	
	public void create()
	{
		level.start();
		
		statBars = new StatBars();
		statBars.setPosition(0, 74);
		
		keydisp = new KeyDisplay();
		
		scoreField = new ScoreField();
		scoreField.setPosition(50, 295);
		
		bgm = Engine.assets.get(level.data.bgm, Music.class);
		bgm.setLooping(true);
		bgm.play();
		
		Gdx.input.setInputProcessor(level.world.getSystem(InputSystem.class));
	}
	
	/**
	 * Loads all visual assets of the scene into memory
	 */
	public void load()
	{
		KeyDisplay.loadAssets();
		StatBars.loadAssets();
		ScoreField.loadAssets();
		level = new Level(Engine.level);
		level.loadAssets();
		ready = false;
	}
	
	/**
	 * Removes all visual assets of the scene from memory
	 */
	public void unload()
	{
	}
}
