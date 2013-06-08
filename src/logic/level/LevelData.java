package logic.level;

import java.io.IOException;
import java.util.HashMap;

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
	Array<Array<String>> groupNames;
	Array<Array<Vector2>> spawns;
	
	public String name;
	public String bgm;
	public String background;
	private Element levelFile;
	public EnemyAtlas atlas;
	
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
		HashMap<Element, Path> spawnPath = SvgPathParser.getPathsMap(levelFile);
		
		groupNames = new Array<Array<String>>();
		spawns = new Array<Array<Vector2>>();
		
		for (Element e : spawnPath.keySet())
		{
			String[] names = e.getAttribute("enemies").split("[, ]+");
			Array<String> group = new Array<String>(names);
			Array<Vector2> points = new Array<Vector2>(names.length);
			for (int i = 0; i < group.size; i++)
			{
				Step pos = spawnPath.get(e).getStep(i);
				Vector2 v = new Vector2(pos.x, pos.y);
				points.add(v);
			}
			groupNames.add(group);
			spawns.add(points);
		}
	}
	
	private void loadProperties()
	{
		bgm = DataDir.BGM + levelFile.getAttribute("bgm", "silence.mp3");
		background = DataDir.Levels + levelFile.getAttribute("background", "field001.png");
		
		//allow inline atlas defining
		Element atlasData;
		if ((atlasData = levelFile.getChildByName("atlas")) != null)
		{
			atlas = new EnemyAtlas(atlasData);
		}
		else
		{
			atlas = new EnemyAtlas(levelFile.getAttribute("atlas"));
		}
	}
}