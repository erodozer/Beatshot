package logic;

import CEF.AnimationSystem;
import CEF.BulletLifeSystem;
import CEF.CollisionEntitySystem;
import CEF.EmitterSystem;
import CEF.InputSystem;
import CEF.MovementSystem;
import CEF.RenderSystem;

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
