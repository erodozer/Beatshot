package com.nhydock.beatshot.logic.level;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.PolyPath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.nhydock.beatshot.logic.Enemy.EnemyAtlas;
import com.nhydock.beatshot.util.PathParser;

/**
 * Holds data on how a formation should be parsed out from a level
 * @author nhydock
 *
 */
public class Formation implements Cloneable {
	private Path<Vector2> path;
	private float duration;
	private EnemyAtlas atlas;
	private Array<EnemyData> enemies;
	
	public Formation(JsonValue json)
	{
		JsonValue value;
		
		//set atlas
		{
			value = json.get("atlas");
			atlas = EnemyAtlas.register(value.asString());
		}
		//path parsing
		{
			value = json.get("path");
			path = PathParser.parsePath(value);
		}
		
		//set enemies
		{
			enemies = new Array<EnemyData>();
			value = json.get("enemies");
			for (JsonValue v : value)
			{
				EnemyData e = new EnemyData(v.getInt("type", 0), v.getFloat("delay", 0f));
				enemies.add(e);
			}
		}
	}
	
	/**
	 * Spawns the formation into the world by creating all the enemies.
	 * Add the into the world at your will, the list is created without manipulating the world.
	 * @param world - 
	 * @return Array
	 */
	public Array<Entity> spawn(World world)
	{
		//set enemies
		Array<Entity> entities = new Array<Entity>();
		for (EnemyData v : enemies)
		{
			Entity e = world.createEntity();
			atlas.createEnemy(v.type, e);
			e.addComponent(new com.badlogic.gdx.artemis.components.Path(path, duration, v.delay));
			
			entities.add(e);
		}
				
		return entities;
	}
	
	private class EnemyData
	{
		int type;
		float delay;
		
		public EnemyData(int type, float delay)
		{
			this.type = type;
			this.delay = delay;
		}
	}
}