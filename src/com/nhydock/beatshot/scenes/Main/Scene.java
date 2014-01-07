package com.nhydock.beatshot.scenes.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.nhydock.beatshot.CEF.PlayerInputSystem;
import com.nhydock.beatshot.Factories.ExplosionFactory;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.logic.level.Level;
import com.nhydock.beatshot.scenes.Main.ui.KeyDisplay;
import com.nhydock.beatshot.scenes.Main.ui.ScoreField;
import com.nhydock.beatshot.scenes.Main.ui.StatBars;
import com.nhydock.beatshot.util.Tools;
import com.nhydock.beatshot.util.SpriteSheet;

import static com.nhydock.beatshot.core.BeatshotGame.INTERNAL_RES;
import static com.nhydock.beatshot.CEF.RenderSystem.FOV;

public class Scene implements Screen {

	private Sprite statBars;
	private Sprite scoreField;
	//private FieldDisplay field;
	private KeyDisplay keydisp;
	
	private Level level;		//current loaded level
	private String nextLevel;	//name of next level to load
	
	private SpriteBatch batch;
	
	Matrix4 normalProjection;

	//loading booleans
	private boolean uiReady;
	private boolean levelReady;
	private boolean doneLoading;

	//curtains
	private Sprite curtainLeft;
	private Sprite curtainRight;
		
	private InputMultiplexer input;
	
	private Music nextBgm;	//preloaded bgm;

	private Array<FileHandle> bgmPaths;
	
	public Scene()
	{
		batch = new SpriteBatch();
		normalProjection = new Matrix4().setToOrtho2D(0, 0, INTERNAL_RES[0], INTERNAL_RES[1]);
		uiReady = false;
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}
	
	@Override
	public void pause() {}

	@Override
	public void render(float delta) {
		
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		//wait for assets to load
		if (doneLoading = Tools.assets.update())
		{
			if (!uiReady)
			{
				create();
				uiReady = true;
			}
			if (!this.levelReady && level != null && nextLevel == null)
			{
				level.start();
				this.levelReady = true;
				
				Gdx.input.setInputProcessor(input);
			}
		}
		
		if (this.uiReady)
		{

			if (level != null && doneLoading)
			{
				level.advance(delta);
				level.draw(batch);	
			}
			
			batch.begin();
			if (!this.levelReady)
			{
				//draw curtains
				if (curtainLeft.getX() < 0)
				{
					curtainLeft.translate(curtainLeft.getWidth()*.75f*delta, 0f);
					if (curtainLeft.getX() >= 0)
					{
						curtainLeft.setX(0);		
					}
				}
				
				if (curtainRight.getX() > 0)
				{
					curtainRight.translate(-curtainRight.getWidth()*.75f*delta, 0f);
					if (curtainRight.getX() <= 0)
					{		
						curtainRight.setX(0);
					}
				}
				curtainLeft.draw(batch);
				curtainRight.draw(batch);
				
				if (curtainLeft.getX() >= 0 && curtainRight.getX() <= 0 && nextLevel != null)
				{
					BeatshotGame.GameOver = false;
					if (level != null)
					{
						level.unloadAssets();
					}
					level = new Level(nextLevel);
					level.loadAssets();
					refreshInput();
					nextLevel = null;
				}
			}
			else
			{
				//hide curtains
				if (curtainLeft.getX() > -curtainLeft.getWidth())
				{
					curtainLeft.translate(-curtainLeft.getWidth()*.75f*delta, 0f);
					if  (curtainLeft.getX() <= -curtainLeft.getWidth())
					{
						curtainLeft.setX(-curtainLeft.getWidth());		
					}
					curtainLeft.draw(batch);
				}
				
				if (curtainRight.getX() < FOV[3])
				{
					curtainRight.translate(curtainRight.getWidth()*.75f*delta, 0f);
					if (curtainRight.getX() >= FOV[3])
					{
						curtainRight.setX(FOV[3]);
					}
					curtainRight.draw(batch);
				}
			}
			
			batch.end();
			batch.setProjectionMatrix(normalProjection);
			batch.begin();
			scoreField.draw(batch);
			statBars.draw(batch);
			keydisp.draw(batch, 1.0f);
			batch.end();
		}
		
		if (BeatshotGame.bgm != null && !BeatshotGame.bgm.isPlaying())
		{
			BeatshotGame.bgm.dispose();
			BeatshotGame.bgm = nextBgm;
			BeatshotGame.bgm.play();
			
			FileHandle n = bgmPaths.get((int)(Math.random()*bgmPaths.size));
			nextBgm = Gdx.audio.newMusic(n);
			nextBgm.setLooping(false);
		}
	}

	private void refreshInput() {
		input = new InputMultiplexer();
		input.addProcessor(Tools.utils);
		input.addProcessor(new SystemKeys(this));
		input.addProcessor(this.keydisp.inputListener);
		input.addProcessor(level.world.getSystem(PlayerInputSystem.class));
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void resize(int width, int height) {
		Vector2 size = Scaling.fit.apply(INTERNAL_RES[0], INTERNAL_RES[1], width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Set a new level to go to
	 * @param levelName
	 */
	public void setLevel(String levelName)
	{
		this.nextLevel = levelName;
		this.levelReady = false;
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
		
		scoreField = new ScoreField();
		scoreField.setPosition(25, 295);
		
		SpriteSheet curtainTex = new SpriteSheet(Gdx.files.internal(DataDir.Ui + "curtain.png"), 2, 1);
		curtainLeft = new Sprite(curtainTex.getFrame(0));
		curtainRight = new Sprite(curtainTex.getFrame(1));

		uiReady = true;
		
		if (bgmPaths.size > 0)
		{
			FileHandle b = bgmPaths.get((int)(Math.random()*bgmPaths.size));
			FileHandle n = bgmPaths.get((int)(Math.random()*bgmPaths.size));
			if (BeatshotGame.bgm != null)
				BeatshotGame.bgm.dispose();
			BeatshotGame.bgm = Gdx.audio.newMusic(b);
			nextBgm = Gdx.audio.newMusic(n);
			nextBgm.setLooping(false);
			BeatshotGame.bgm.setLooping(false);
			BeatshotGame.bgm.play();
		}
	}
	
	/**
	 * Loads all visual assets of the scene into memory
	 */
	public void load()
	{
		KeyDisplay.loadAssets();
		StatBars.loadAssets();
		ScoreField.loadAssets();
		ExplosionFactory.loadAssets();
		uiReady = false;
		
		bgmPaths = new Array<FileHandle>(Gdx.files.internal(DataDir.BGM).list());
	
		this.setLevel("level001");
	}
	
	private class SystemKeys implements InputProcessor
	{
		Scene s;
		
		public SystemKeys(Scene s)
		{
			super();
			this.s = s;
		}
		
		@Override
		public boolean keyDown(int key) {
			
			if (key == Input.Keys.ENTER)
			{
				if (BeatshotGame.GameOver)
				{
					s.setLevel("level001");
				}
				return true;
			}
			if (key == Input.Keys.F1)
			{
				Level.switchMode();
				s.setLevel("level001");
				return true;
			}
			
			return false;
		}

		@Override
		public boolean keyTyped(char arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mouseMoved(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDragged(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
