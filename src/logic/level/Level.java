package logic.level;

import logic.Engine;
import logic.Player;
import logic.Consts.DataDir;

import EntitySystems.*;
import EntitySystems.Components.Position;
import EntitySystems.Components.Renderable;
import EntitySystems.Components.Scrollable;

import aurelienribon.tweenengine.TweenManager;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Level{
	
	//we set a constant maximum amount of sprites we draw to keep performance at its peak
	//sprites includes both bullets and creatures, pretty much anything in the world
	private static final int MAX_SPRITES = 128;
	private int currentEnemyGroup;
	
	//game load data files
	public LevelData data;
	
	/**
	 * Container of all the entities for this level
	 */
	public World world;
	
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
		world.setSystem(new BulletLifeSystem());
		world.setSystem(new CollisionEntitySystem());
		world.setSystem(new MovementSystem());
		
		world.setSystem(new InputSystem());
		
		world.setSystem(new RenderSystem(), true);
		
		world.initialize();
	}
	
	public void advance(float delta)
	{
		world.setDelta(delta);
		world.process();
	}
	
	/**
	 * Draws the render system
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
		// place the enemies in the world
		for (int i = 0; i < data.groupNames.size; i++)
		{
			Array<String> group = data.groupNames.get(i);
			for (int j = 0; j < group.size; j++)
			{
				String name = group.get(j);
				Vector2 pos = data.spawns.get(i).get(j);
				
				Entity e = this.world.createEntity();
				e = data.atlas.createEnemy(name, e);
				e.addComponent(new Position(pos.x, pos.y));
				this.world.addEntity(e);
			}
		}
		
		Entity p = this.world.createEntity();
		p = Player.createEntity(p);
		this.world.getManager(TagManager.class).register(EntitySystems.GroupComponents.Player.TYPE, p);
		this.world.addEntity(p);
		
		Entity field = this.world.createEntity();
		field.addComponent(new Position());
		
		Texture t = Engine.assets.get(data.background, Texture.class);
		t.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		field.addComponent(new Renderable(new Sprite(t)));
		
		field.addComponent(new Scrollable(0f, -.15f, 1.0f, 1.0f));
		this.world.getManager(TagManager.class).register("Field", field);
		this.world.addEntity(field);
	}

	/**
	 * Loads the assets necessary for the level to be displayed
	 */
	public void loadAssets() {
		Engine.assets.load(data.background, Texture.class);
		Engine.assets.load(data.bgm, Music.class);
	}
}
