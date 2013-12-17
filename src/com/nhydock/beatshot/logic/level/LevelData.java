package com.nhydock.beatshot.logic.level;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.logic.Enemy.EnemyAtlas;
import com.nhydock.beatshot.util.SvgPathParser;

public class LevelData
{
	public static HashMap<String, Path<Vector2>> enemyPaths;
	
	static
	{
		XmlReader xml = new XmlReader();
		Element e;
		try {
			e = xml.parse(Gdx.files.internal(DataDir.Paths + "movement.svg"));
			enemyPaths = SvgPathParser.getPathsNameMap(e);
			enemyPaths.put("null", null);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * All the spawn sets
	 */
	public Array<SpawnSet> enemyData;
	
	/**
	 * Level file name
	 */
	public String name;
	/**
	 * Name of the background music
	 */
	public String bgm;
	
	private Element levelFile;

	public Background background;
	
	public LevelData(String name)
	{
		XmlReader xmlParser = new XmlReader();
		try {
			levelFile = xmlParser.parse(Gdx.files.internal(DataDir.Levels + (name+".svg")));
			this.name = name;
			loadProperties();
			loadEnemies();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses the enemies out of the svg paths
	 */
	private void loadEnemies()
	{
		//spawns = new Array<Array<Vector2>>();
		
		Array<Element> spawns = levelFile.getChildrenByName("spawn");
		enemyData = new Array<SpawnSet>();
		
		for (int i = 0; i < spawns.size; i++)
		{
			Element element = spawns.get(i);
			EnemyAtlas atlas = new EnemyAtlas(element.getAttribute("atlas"));
			
			HashMap<Element, Path<Vector2>> spawnPath = SvgPathParser.getPathsMap(element);
			
			for (Element e : spawnPath.keySet())
			{
				SpawnSet spawnSet = new SpawnSet(atlas, e, spawnPath.get(e));

				enemyData.add(spawnSet);
			}
		}
	}
	
	private void loadProperties()
	{
		bgm = DataDir.BGM + levelFile.getAttribute("bgm", "silence.mp3");
		
		Array<Element> layerElements = levelFile.getChildrenByName("layer");
		
		background = new Background(levelFile.getChildByName("field"));
	}
	
	public static class Spawn
	{
		public Vector2 pos;
		public Vector2 offset;
		public String name;
		public String path;
	}
	
	public static class SpawnSet
	{
		public EnemyAtlas atlas;
		public Array<Spawn> spawns;
		private float location;
		private float high;
		public final boolean warning;
		
		public SpawnSet(EnemyAtlas atlas, Element e, Path<Vector2> points)
		{
			this.atlas = atlas;
			spawns = new Array<Spawn>();
			warning = e.getBooleanAttribute("warning", false);
			String[] names = e.getAttribute("enemies").split("[, ]+");
			String[] patterns = e.getAttribute("moves", "null").split("[, ]+");
			for (int j = 0; j < names.length; j++)
			{
				Spawn spawn = new Spawn();
				
				Step pos = points;
				spawn.pos = new Vector2(pos.x, pos.y);
				spawn.name = names[j];
				spawn.offset = new Vector2(0, 0);
				try
				{
					spawn.path = patterns[j];
				}
				catch (IndexOutOfBoundsException exception)
				{
					spawn.path = "null";
				}
				spawns.add(spawn);
			}
			
			findLocation();
		}
		
		/**
		 * Readjusts the spawn data's location according to the spawn points
		 * Only call this after all spawn data has been loaded
		 */
		private void findLocation()
		{
			
			//Finds the lowest point of the spawns
			this.location = spawns.first().pos.y;
			for (int i = 1; i < spawns.size; i++)
			{
				float y = spawns.get(i).pos.y;
				if (y < this.location)
				{
					this.location = y;
				}
			}
			
			//finds the highest point of the spawns
			high = spawns.first().pos.y;
			for (int i = 1; i < spawns.size; i++)
			{
				float y = spawns.get(i).pos.y;
				if (y > high)
				{
					high = y;
				}
			}
			
			//adjust all spawns to be relative to that low point
			for (int i = 0; i < spawns.size; i++)
			{
				spawns.get(i).pos.y -= this.location;
				//spawns.get(i).offset.y = this.high ;
			}
			
		}
	}
	
	public static class Background
	{
		/**
		 * Background stack
		 */
		private Array<LayerData> layers;
		private Array<StaticData> immovables;
		
		public Array<FieldData> stack;
		
		public Background(Element e)
		{
			layers = new Array<LayerData>();
			immovables = new Array<StaticData>();
			stack = new Array<FieldData>();
			for (int i = 0; i < e.getChildCount(); i++)
			{
				Element child = e.getChild(i);
				if (child.getName().equals("layer"))
				{
					LayerData d = new LayerData(child);
					layers.add(d);
					stack.add(d);
				}
				else if (child.getName().equals("static"))
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
			
			public LayerData(Element e)
			{
				image = DataDir.Levels + e.getAttribute("image");
				rate = e.getFloatAttribute("scroll", 0);
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
			
			public StaticData(Element e)
			{
				image = DataDir.Levels + e.getAttribute("image");
				dps = e.getFloatAttribute("rot", 0f);
				
				x = e.getFloatAttribute("x", 0f);
				y = e.getFloatAttribute("y", 0f);
				
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