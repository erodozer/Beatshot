package logic.Enemy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import logic.Consts.DataDir;

import EntitySystems.Components.Bound;
import EntitySystems.Components.Health;
import EntitySystems.Components.Renderable;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 
 * @author nhydock
 *
 */
public class EnemyAtlas{

	private TextureAtlas textureAtlas;
	private Map<String, EnemyData> enemyAtlas;
	private Texture atlasImage;
	
	/**
	 * Create an enemy atlas from an already loaded Element defined inline in some xml file
	 * @param dataSrc
	 */
	public EnemyAtlas(Element dataSrc)
	{
		enemyAtlas = new HashMap<String, EnemyData>();
		
		textureAtlas = new TextureAtlas();
		atlasImage = new Texture(Gdx.files.internal(DataDir.Enemies + dataSrc.getAttribute("texture", "enemyatlas.png")));
		for (Element e : dataSrc.getChildrenByName("enemy"))
		{
			EnemyData enemy = new EnemyData(e);
			enemyAtlas.put(enemy.name, enemy);
			textureAtlas.addRegion(enemy.name, enemy.region);
		}
	}
	
	/**
	 * Load a file that is dedicated to being an enemy atlas
	 * @param fileName
	 */
	public EnemyAtlas(String fileName)
	{
		XmlReader xmlParser = new XmlReader();
		Element xmlFile = null;	
		try {
			enemyAtlas = new HashMap<String, EnemyData>();
			
			xmlFile = xmlParser.parse(Gdx.files.internal(DataDir.Enemies + fileName));
			textureAtlas = new TextureAtlas();
			atlasImage = new Texture(Gdx.files.internal(DataDir.Enemies + xmlFile.getAttribute("texture", "enemyatlas.png")));
			for (Element e : xmlFile.getChildrenByName("enemy"))
			{
				EnemyData enemy = new EnemyData(e);
				enemyAtlas.put(enemy.name, enemy);
				textureAtlas.addRegion(enemy.name, enemy.region);
			}
		}
		catch (IOException e) {
			System.err.println("Could not set enemy atlas");
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates an enemy entity to be placed in a world
	 * @param name - enemy's name
	 * @param e - Entity to modify into an enemy
	 * @return Entity - the generated enemy
	 */
	public Entity createEnemy(String name, Entity e)
	{
		EnemyData enemy = enemyAtlas.get(name);

		Sprite s = textureAtlas.createSprite(name);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(s.getWidth(), s.getHeight());
		
		e.addComponent(new Renderable(s));
		e.addComponent(new Health(enemy.maxhp));
		e.addComponent(new Bound(shape));
		
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
		
		/**
		 * @param dataSrc - an xml element
		 */
		private EnemyData(Element dataSrc)
		{
			name = dataSrc.getAttribute("name");
			region = new TextureRegion(atlasImage, 
					Integer.parseInt(dataSrc.getAttribute("x", "0")),
					Integer.parseInt(dataSrc.getAttribute("y", "0")),
					Integer.parseInt(dataSrc.getAttribute("width", "1")),
					Integer.parseInt(dataSrc.getAttribute("height", "1")));
			maxhp = Integer.parseInt(dataSrc.getAttribute("hp", "10"));
		}
	}
}
