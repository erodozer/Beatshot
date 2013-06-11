package logic.level;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import logic.Consts.DataDir;
import logic.Enemy.EnemyAtlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shipvgdc.sugdk.util.pathfinding.Path;
import com.shipvgdc.sugdk.util.pathfinding.Step;
import com.shipvgdc.sugdk.util.pathfinding.SvgPathParser;

public class LevelData
{
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
			
			HashMap<Element, Path> spawnPath = SvgPathParser.getPathsMap(element);
			
			for (Element e : spawnPath.keySet())
			{
				SpawnSet spawnSet = new SpawnSet(atlas);
				String[] names = e.getAttribute("enemies").split("[, ]+");
				for (int j = 0; j < names.length; j++)
				{
					Spawn spawn = new Spawn();
					
					Step pos = spawnPath.get(e).getStep(j);
					spawn.pos = new Vector2(pos.x, pos.y);
					spawn.name = names[i];
					spawnSet.spawns.add(spawn);
				}
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
		Vector2 pos;
		String name;
	}
	
	public static class SpawnSet
	{
		EnemyAtlas atlas;
		Array<Spawn> spawns;
		
		public SpawnSet(EnemyAtlas atlas)
		{
			this.atlas = atlas;
			spawns = new Array<Spawn>();
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
				rate = Float.parseFloat(e.getAttribute("scroll", "0"));
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
				dps = Float.parseFloat(e.getAttribute("rot", "0"));
				
				x = Float.parseFloat(e.getAttribute("x", "0"));
				y = Float.parseFloat(e.getAttribute("y", "0"));
				
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