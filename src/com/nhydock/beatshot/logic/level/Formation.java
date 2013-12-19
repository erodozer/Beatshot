package com.nhydock.beatshot.logic.level;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.PolyPath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.nhydock.beatshot.logic.Enemy.EnemyAtlas;

/**
 * Holds data on how a formation should be parsed out from a level
 * @author nhydock
 *
 */
public class Formation implements Cloneable {
	public Path<Vector2> path;
	public float duration;
	public int id;
	public EnemyAtlas atlas;
	public Array<Entity> enemies;
	
	public Formation load(JsonValue json, World world)
	{
		Formation f = new Formation();
		
		JsonValue value;
		
		//set atlas
		{
			value = json.get("atlas");
			atlas = EnemyAtlas.register(value.asString());
		}
		//path parsing
		{
			value = json.get("path");
			if (value.getString("type").equals("bezier"))
			{
				Array<Vector2> points = new Array<Vector2>();
				value = value.get("points");
				for (int i = 0; i < value.size; i++)
				{
					Vector2 v = new Vector2();
					JsonValue p = value.get(i);
					v.x = p.getFloat(0);
					v.y = p.getFloat(1);
					points.add(v);
				}
				BSpline<Vector2> path = new BSpline<Vector2>();
				path.set(points.toArray(), 2, false);
				f.path = path;
			}
		}
		//set enemies
		{
			enemies = new Array<Entity>();
			value = json.get("enemies");
			for (JsonValue v : value)
			{
				Entity e = world.createEntity();
				int type = v.getInt("type");
				float delay = v.getFloat("delay");
				
				atlas.createEnemy(type, e);
				e.addComponent(new com.badlogic.gdx.artemis.components.Path(path, duration, delay));
				
				enemies.add(e);
			}
		}
	
		return f;
	}
	
	/**
	 * Creates a deep-copy of this formation
	 */
	public Formation clone()
	{
		Formation f = new Formation();
		
		//set atlas
		f.atlas = EnemyAtlas.register(this.atlas.name);
		
		//path parsing
		{
			if (this.path instanceof BSpline)
			{
				f.path = new BSpline<Vector2>();
			}
			else
			{
				f.path = new PolyPath<Vector2>();
				
				f.path = ((PolyPath<Vector2>)this.path).clone();
			}
			value = json.get("path");
			
		}
		//set enemies
		{
			enemies = new Array<Entity>();
			value = json.get("enemies");
			for (JsonValue v : value)
			{
				Entity e = world.createEntity();
				int type = v.getInt("type");
				float delay = v.getFloat("delay");
				
				atlas.createEnemy(type, e);
				e.addComponent(new com.badlogic.gdx.artemis.components.Path(path, duration, delay));
				
				enemies.add(e);
			}
		}
	}
}