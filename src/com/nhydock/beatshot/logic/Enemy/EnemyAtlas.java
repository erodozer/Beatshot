package com.nhydock.beatshot.logic.Enemy;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.artemis.components.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.nhydock.beatshot.CEF.Components.Bound;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Enemy;
import com.nhydock.beatshot.core.Consts.DataDir;

/**
 * 
 * @author nhydock
 *
 */
public class EnemyAtlas{

	private static ObjectMap<String, EnemyAtlas> cache;
	
	static
	{
		cache = new ObjectMap<String, EnemyAtlas>();
	}
	
	/**
	 * Adds an enemy atlas to the cache
	 * @param fileName
	 * @return
	 */
	public static EnemyAtlas register(String fileName)
	{
		if (!cache.containsKey(fileName))
		{
			EnemyAtlas e = new EnemyAtlas(fileName);
			cache.put(fileName, e);
			return e;
		}
		else
		{
			return fetch(fileName);
		}
	}
	
	public static EnemyAtlas fetch(String fileName)
	{
		return cache.get(fileName);
	}
	
	public static void clearCache() {
		cache.clear();
	}
	
	private TextureAtlas textureAtlas;
	private Array<EnemyData> enemyAtlas;
	private Texture atlasImage;
	
	public String name;
	
	/**
	 * Load a file that is dedicated to being an enemy atlas
	 * @param fileName
	 */
	protected EnemyAtlas(String fileName)
	{
		JsonReader reader = new JsonReader();
		JsonValue json;
		json = reader.parse(Gdx.files.internal(DataDir.Enemies + fileName + ".json"));
		
		enemyAtlas = new Array<EnemyData>();
		atlasImage = new Texture(Gdx.files.internal(DataDir.Enemies + json.getString("texture", "enemyatlas.png")));
		textureAtlas = new TextureAtlas();
		JsonValue enemies = json.get("enemies");
		for (int i = 0; i < enemies.size; i++)
		{
			JsonValue value = enemies.get(i);
			EnemyData e = new EnemyData(value); 
			enemyAtlas.add(e);
			textureAtlas.addRegion(e.name, e.region);
		}
		
		name = fileName;
	}
	
	/**
	 * Generates an enemy entity to be placed in a world
	 * @param name - enemy's name
	 * @param e - Entity to modify into an enemy
	 * @return Entity - the generated enemy
	 */
	public Entity createEnemy(int id, Entity e)
	{
		EnemyData enemy = enemyAtlas.get(id);
		if (enemy == null)
		{
			return null;
		}
		
		Sprite s = textureAtlas.createSprite(enemy.name);
		
		e.addComponent(new Renderable(s), Renderable.CType);
		e.addComponent(new Health(enemy.maxhp), Health.CType);
		e.addComponent(new Bound(s.getWidth(), s.getHeight()), Bound.CType);
		e.addComponent(new Position(), Position.CType);
		e.addComponent(new Velocity(), Velocity.CType);
		e.addComponent(new Enemy(), Enemy.CType);
		
		return e;
	}
	
	/**
	 * Loads enemy data from an xml element
	 */
	private class EnemyData
	{
		String name;
		int maxhp;
		TextureRegion region;
		EnemyAtlas atlas;
		
		/**
		 * @param dataSrc - a json node
		 */
		private EnemyData(JsonValue dataSrc)
		{
			name = dataSrc.name;
			maxhp = dataSrc.getInt("hp", 10);
			JsonValue box = dataSrc.get("box");
			region = new TextureRegion(atlasImage,
					box.getInt(0),
					box.getInt(1),
					box.getInt(2),
					box.getInt(3));
			
		}
	}
}
