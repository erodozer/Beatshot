package com.nhydock.beatshot.logic.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.JsonValue;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.logic.Engine;

public class LevelData
{	
	/**
	 * All the spawn sets
	 */
	public Array<Formation> enemyData;
	public IntArray order;
	public int midboss;
	
	/**
	 * Level file name
	 */
	public String name;
	/**
	 * Name of the background music
	 */
	public String bgm;
	
	private JsonValue levelFile;

	public Background background;
	
	public LevelData(String name)
	{
		levelFile = Engine.json.parse(Gdx.files.internal(DataDir.Levels + (name+".json")));
		this.name = name;
		loadProperties();
		loadFormations();
	}
	
	/**
	 * Parses the enemies out of the svg paths
	 */
	private void loadFormations()
	{
		JsonValue value;
		
		//load the formations
		{
			value = levelFile.get("formations");
			enemyData = new Array<Formation>();
			
			for (int i = 0; i < value.size; i++)
			{
				JsonValue set = value.get(i);
				enemyData.add(new Formation(set));
			}
		}
		
		//load the encounter order of the formations in the level
		{
			value = levelFile.get("spawns");			
			
			midboss = value.getInt("midboss");
			
			value = value.get("order");
			order = new IntArray();

			for (int i = 0; i < value.size; i++)
			{
				int n = value.getInt(i);
				order.add(n);
			}
		}
	}
	
	private void loadProperties()
	{
		bgm = DataDir.BGM + levelFile.getString("bgm");
		background = new Background(levelFile.get("field"));
	}
	
	public static class Background
	{
		/**
		 * Background stack
		 */
		private Array<LayerData> layers;
		private Array<StaticData> immovables;
		
		public Array<FieldData> stack;
		
		public Background(JsonValue e)
		{
			layers = new Array<LayerData>();
			immovables = new Array<StaticData>();
			stack = new Array<FieldData>();
			for (int i = 0; i < e.size; i++)
			{
				JsonValue child = e.get(i);
				if (child.getString("type").equals("layer"))
				{
					LayerData d = new LayerData(child);
					layers.add(d);
					stack.add(d);
				}
				else if (child.getString("type").equals("static"))
				{
					StaticData d = new StaticData(child);
					immovables.add(d);
					stack.add(d);
				}
			}
		}
		
		public static class FieldData
		{
			String image;
		}
		
		public static class LayerData extends FieldData
		{
			float rate;		//vertical scroll rate
			
			public LayerData(JsonValue e)
			{
				image = DataDir.Levels + e.getString("image");
				rate = e.getFloat("scroll", 0);
			}
		}
		
		/**
		 * Objects in the scene that stick in one place.
		 * They may spin
		 * @author nhydock
		 *
		 */
		public static class StaticData extends FieldData
		{
			float dps;  //degrees per second rotation speed
			float x;	//x position on the screen
			float y;    //y position on the screen
			
			public StaticData(JsonValue e)
			{
				image = DataDir.Levels + e.getString("image");
				dps = e.getFloat("rot", 0f);
				
				x = e.getFloat("x", 0f);
				y = e.getFloat("y", 0f);
				
				//allow using percentages for position on screen
				if (Math.abs(x) < 1f)
				{
					x *= 190;
				}
				if (Math.abs(y) < 1f)
				{
					y*= 220;
				}
				
			}
		}
	}
}