package com.nhydock.beatshot.logic.level;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.artemis.components.*;
import com.badlogic.gdx.artemis.systems.*;
import com.nhydock.beatshot.CEF.*;
import com.nhydock.beatshot.CEF.Components.*;
import com.nhydock.beatshot.logic.level.LevelData.Background.*;
import com.nhydock.beatshot.Factories.PlayerFactory;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.util.Tools;
import com.nhydock.beatshot.util.SpriteSheet;

import static com.nhydock.beatshot.CEF.RenderSystem.FOV;

public class Level{
	
	/**
	 * Defines how we handle enemy spawning
	 * In endurance mode, enemy spawns are randomly selected and just occur every 1000 meters
	 * You also get points for how far you progress in endurance mode
	 * 
	 * In story mode, enemys spawn where they are laid out in the level and are capable of blocking progression until defeated
	 * Score bonuses are recieved for beating a level quickly and for defeating all enemies
	 */
	private static boolean EnduranceMode = false;
	
	public static void switchMode(){
		EnduranceMode = !EnduranceMode;
	}
	
	/**
	 * How often spawns should occur in Endurance Mode
	 */
	private static float ENDURANCERATE = 100f;

	/**
	 * Container of all the entities for this level
	 */
	public World world;
	
	private float distance;
	
	public LevelData data;
	
	/**
	 * Necessary for story/level progression in non-endurance mode.
	 * We need to keep track of the formation and the index in the order
	 * set.
	 */
	private int wave;
	
	RenderSystem rs;
	EnemySystem es;

	private Sound warningBeep;

	private float warning;
	
	/**
	 * Length of time that the warning message should be visible
	 */
	private static final float WARNING_WAIT = 2.0f;

	Health playerHealth;
	
	/**
	 * Loads and constructs a level
	 * @param id - name of the level
	 */
	public Level(String id)
	{
		data = new LevelData(id);
		world = new World();
		
		world.setManager(new TagManager());
		world.setManager(new GroupManager());
		
		world.setSystem(new AnimationSystem());
		world.setSystem(new CollisionEntitySystem());
		world.setSystem(new MovementSystem());
		world.setSystem(new PlayerInputSystem());
		world.setSystem(new EmitterSystem());
		es = new EnemySystem();
		world.setSystem(es);
		
		rs = new RenderSystem();
		world.setSystem(rs, true);
		
		Entity player = PlayerFactory.createEntity(world);
		
		BeatshotGame.world = world;
		BeatshotGame.player = player;
		
		playerHealth = (Health)player.getComponent(Health.CType);
	}
	
	/**
	 * Performs updating when running in endurance mode
	 */
	private void updateEndurance(float travel)
	{
		distance += travel;
		BeatshotGame.score += travel;
		if (distance > ENDURANCERATE)
		{
			distance -= ENDURANCERATE;
			
			//pick formation
			int num = (int)(Math.random()*data.enemyData.size);
			Formation enemyData = data.enemyData.get(num);
			
			//if midboss or boss then flash a warning
			boolean w = (num == data.midboss) || (num == data.enemyData.size-1);
			world.getSystem(RenderSystem.class).warning = w;
			warning = w?WARNING_WAIT:0;
			
			es.spawnEnemies(enemyData);
			//warningBeep.play();
		}
	}
	
	/**
	 * Performs updates of the level progression when in story/linear mode
	 */
	private void updateStory(float travel)
	{
		if (distance > 0)
		{
			distance -= travel;
		}
		else
		{
			if (es.aliveCount() == 0)
			{
				//set and spawn the next formation
				IntArray set = data.order.get(wave);
				for (int i = 0; i < set.size; i++)
				{
					int n = set.get(i);
					Formation f = data.enemyData.get(n);
					es.spawnEnemies(f);
					System.out.println();
				}
				wave++;
				
				//if midboss or boss then flash a warning
				boolean w = (wave == data.midboss) || (wave == data.order.size);
				world.getSystem(RenderSystem.class).warning = w;
				warning = w?2.0f:0;
			}
		}
	}
	
	/**
	 * Updates game logic
	 * @param delta
	 */
	public void advance(float delta)
	{
		if (BeatshotGame.GameOver)
		{
			world.setDelta(0);
			return;
		}
		
		if (warning >= 0f)
		{
			warning -= delta;
		}
		else
		{
			world.getSystem(RenderSystem.class).warning = false;
		}
		
		float travel = 10f*delta;
		if (EnduranceMode)
		{
			this.updateEndurance(travel);
		}
		else
		{
			this.updateStory(travel);
		}
		
		world.setDelta(delta);
		world.process();
		
		if (playerHealth.hp <= 0)
		{
			BeatshotGame.GameOver = true;
		}
	}
	
	/**
	 * Draws the render system
	 * @param batch
	 */
	public void draw(SpriteBatch batch)
	{
		world.getSystem(RenderSystem.class).draw(batch);
	}

	/**
	 * sets all the level's data into its starting positions
	 */
	public void start()
	{
		GroupManager gm = this.world.getManager(GroupManager.class);
		TagManager tm = this.world.getManager(TagManager.class);
		
		//create layered background
		for (int i = 0; i < data.background.stack.size; i++)
		{
			
			Entity layer = this.world.createEntity();
			
			FieldData f = data.background.stack.get(i);
			Texture t = Tools.assets.get(f.image, Texture.class);
			Sprite s = new Sprite(t);
			
			if (f instanceof LayerData)
			{
				LayerData d = (LayerData)data.background.stack.get(i);
				t.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
				s.setSize(Math.max(t.getWidth(), FOV[2]), Math.max(t.getHeight(), FOV[3]));
				layer.addComponent(new Position(), Position.CType);
					
				layer.addComponent(new Scrollable(0, d.rate, Math.max(FOV[2]/(float)t.getWidth(), 1.0f), Math.max(FOV[3]/(float)t.getHeight(), 1.0f)), Scrollable.CType);
			}
			else if (f instanceof StaticData)
			{
				StaticData d = (StaticData)data.background.stack.get(i);
				layer.addComponent(new Position(d.x, d.y), Position.CType);
				layer.addComponent(new Angle(0), Angle.CType);
				layer.addComponent(new Rotation(d.dps), Rotation.CType);
			}
			
			layer.addComponent(new Renderable(s), Renderable.CType);
			
			gm.add(layer, "Field");
			this.world.addEntity(layer);
		}
		
		//load banners
		{
			Texture t = Tools.assets.get(DataDir.Ui+"banners.png", Texture.class);
			t.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
			SpriteSheet bannerTex = new SpriteSheet(t, 1, 3);
			for (int i = 0; i < 3; i++)
			{
				Entity e = this.world.createEntity();
				TextureRegion r = bannerTex.getFrame(i);
				Sprite s = new Sprite(r);
				s.setSize(FOV[3], 12);
				s.setRotation(90);
				s.setOrigin(0, 0);
				
				e.addComponent(new Position(0, 0, 12, 0));
				e.addComponent(new Scrollable(.35f, 0f, FOV[3]/bannerTex.getFrameWidth(), 1f, r));
				e.addComponent(new Renderable(s));
				
				gm.add(e, "Banner");
				gm.add(e, "Banner"+(char)(i+65));
				
				e.addToWorld();
				
				e = this.world.createEntity();
				s = new Sprite(r);
				
				s.setSize(FOV[3], 12);
				s.setRotation(-90);
				s.setOrigin(0, 0);
				s.flip(false, true);
				
				e.addComponent(new Position(FOV[2], 0, -12, FOV[3]));
				e.addComponent(new Scrollable(-.35f, 0f, FOV[3]/bannerTex.getFrameWidth(), 1f, r));
				e.addComponent(new Renderable(s));
				
				gm.add(e, "Banner");
				gm.add(e, "Banner"+(char)(i+65));
				e.addToWorld();
			}
		}
		
		//game over banner
		{
			Entity gameOverBanner;

			gameOverBanner = world.createEntity();
			Sprite s = new Sprite(Tools.assets.get(DataDir.Ui+"gameover.png", Texture.class));
			s.setPosition(0, 80);
			gameOverBanner.addComponent(new Renderable(s));
			tm.register("GameOver", gameOverBanner);
			gameOverBanner.addToWorld();
		}
		
		//warning banner
		{
			Entity warningBanner;

			warningBanner = world.createEntity();
			Sprite s = new Sprite(Tools.assets.get(DataDir.Ui+"enemywarning.png", Texture.class));
			s.setPosition(0, 140);
			warningBanner.addComponent(new Renderable(s));
			tm.register("Warning", warningBanner);
			warningBanner.addToWorld();
		}
		
		Position p = (Position)BeatshotGame.player.getComponent(Position.CType);
		p.location.x = FOV[2]/2.0f;
		
		this.world.initialize();
		
		BeatshotGame.score = 0f;
		BeatshotGame.GameOver = false;
		
		warningBeep = Tools.assets.get(DataDir.SFX + "warning.wav", Sound.class);
		
	}

	/**
	 * Loads the assets necessary for the level to be displayed
	 */
	public void loadAssets() {
		for (int i = 0; i < data.background.stack.size; i++)
		{
			FieldData f = data.background.stack.get(i);
			Tools.assets.load(f.image, Texture.class);
		}
		//Engine.assets.load(data.bgm, Music.class);
		Tools.assets.load(DataDir.Ui + "banners.png", Texture.class);
		
		Tools.assets.load(DataDir.SFX + "warning.wav", Sound.class);
		Tools.assets.load(DataDir.Ui + "gameover.png", Texture.class);
		Tools.assets.load(DataDir.Ui + "enemywarning.png", Texture.class);
	}

	public void unloadAssets() {
		for (int i = 0; i < data.background.stack.size; i++)
		{
			FieldData f = data.background.stack.get(i);
			Tools.assets.unload(f.image);
		}
		//Engine.assets.load(data.bgm, Music.class);
		Tools.assets.unload(DataDir.Ui + "banners.png");
		Tools.assets.unload(DataDir.Ui + "gameover.png");
		Tools.assets.unload(DataDir.SFX + "warning.wav");
	}
}
