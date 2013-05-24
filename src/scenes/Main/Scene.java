package scenes.Main;

import logic.Engine;
import logic.Consts.DataDir;

import org.lwjgl.opengl.Display;

import scenes.Main.ui.FieldDisplay;
import scenes.Main.ui.KeyDisplay;
import scenes.Main.ui.ScoreField;
import scenes.Main.ui.StatBars;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.shipvgdc.sugdk.tween.Tweens;

public class Scene implements Screen {

	Stage ui;
	OrthographicCamera camera;
	private boolean ready;
	
	private Sprite statBars;
	private Sprite scoreField;
	private FieldDisplay field;
	private KeyDisplay keydisp;
	
	public Scene()
	{
		ui = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		ui.setCamera(camera);
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
		
		//wait for assets to load
		if (Engine.assets.update())
		{
			if (!ready)
			{
				create();
				ready = true;
			}
			camera.update();
			field.act(delta);
			SpriteBatch sb = ui.getSpriteBatch();
			
			sb.begin();
			field.draw(sb);
			statBars.draw(sb);
			scoreField.draw(sb);
			keydisp.draw(sb, 1.0f);
			sb.end();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		ui.setViewport(240, 320, false);
		ui.draw();
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
		statBars = new StatBars();
		statBars.setPosition(0, 74);
		
		keydisp = new KeyDisplay();
		ui.addListener(keydisp.inputListener);
		
		scoreField = new ScoreField();
		scoreField.setPosition(50, 295);
		
		field = new FieldDisplay();
		field.setPosition(50, 75);
		ui.addListener(field.inputListener);
		
		Gdx.input.setInputProcessor(ui);
	}
	
	/**
	 * Loads all visual assets of the scene into memory
	 */
	public void load()
	{
		KeyDisplay.loadAssets();
		StatBars.loadAssets();
		ScoreField.loadAssets();
		FieldDisplay.loadAssets();
		
		ready = false;
	}
	
	/**
	 * Removes all visual assets of the scene from memory
	 */
	public void unload()
	{
		ui.clear();
	}

}
