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
import com.badlogic.gdx.audio.Music;

public class Engine {

	public static AssetManager assets;
	public static String level;
	public static float score;
	
	public static World world;
	public static Entity player;
	public static boolean GameOver;
	
	public static Music bgm;
	
	static
	{
		level = "level001";
		assets = new AssetManager();
		score = 0;
	}
}
