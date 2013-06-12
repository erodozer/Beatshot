package logic;

import EntitySystems.AnimationSystem;
import EntitySystems.BulletLifeSystem;
import EntitySystems.CollisionEntitySystem;
import EntitySystems.EmitterSystem;
import EntitySystems.InputSystem;
import EntitySystems.MovementSystem;
import EntitySystems.RenderSystem;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.assets.AssetManager;

public class Engine {

	public static AssetManager assets;
	public static String level;
	public static float score;
	
	public static World world;
	public static Entity player;
	
	static
	{
		level = "level001";
		assets = new AssetManager();
		score = 0;
		
		world = new World();
		
		world.setManager(new TagManager());
		world.setManager(new GroupManager());
		
		world.setSystem(new AnimationSystem());
		world.setSystem(new BulletLifeSystem());
		world.setSystem(new EmitterSystem());
		world.setSystem(new CollisionEntitySystem());
		world.setSystem(new MovementSystem());
		
		world.setSystem(new InputSystem());
		
		world.setSystem(new RenderSystem(), true);
		
		player = Player.createEntity(world.createEntity());
		world.getManager(TagManager.class).register(EntitySystems.Components.Group.Player.TYPE, player);
		
		player.addToWorld();
	}
	
	/**
	 * Purges the world of all everything except the player
	 */
	public static void purge()
	{
		Entity player = world.getManager(TagManager.class).getEntity("Player");
		
		//delete everything
		for (int i = 0; i < world.getEntityManager().getActiveEntityCount(); i++)
		{
			Entity e = world.getEntity(i);
			world.deleteEntity(e);
		}
		//readd the player
		world.addEntity(player);
	}
}
