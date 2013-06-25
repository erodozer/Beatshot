package logic.Enemy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import logic.Bullet.BulletEmitter;

import CEF.Components.Bound;
import CEF.Components.Health;
import CEF.Components.Position;
import CEF.Components.Renderable;
import CEF.Components.Velocity;
import CEF.Groups.Emitter;
import CEF.Groups.Enemy;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import core.Consts.DataDir;

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
		if (enemy == null)
		{
			return null;
		}
		
		
		Sprite s = textureAtlas.createSprite(name);
		
		e.addComponent(new Renderable(s), Renderable.CType);
		e.addComponent(new Health(enemy.maxhp), Health.CType);
		e.addComponent(new Bound(s.getWidth(), s.getHeight()), Bound.CType);
		e.addComponent(new Position(), Position.CType);
		e.addComponent(new Velocity(), Velocity.CType);
		e.addComponent(new Enemy(), Enemy.CType);
		
		Entity emitter = BulletEmitter.createEmitter(e.getWorld().createEntity(), 5, (float)(Math.random()*2.0f)+.5f, e);
		Velocity v = (Velocity)emitter.getComponent(Velocity.CType);
		v.y = -((float)(Math.random()*80f) + 100f);
		emitter.addToWorld();
		
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
		 * @param dataSrc - an xml element
		 */
		private EnemyData(Element dataSrc)
		{
			name = dataSrc.getAttribute("name");
			region = new TextureRegion(atlasImage,
					dataSrc.getIntAttribute("x", 0),
					dataSrc.getIntAttribute("y", 0),
					dataSrc.getIntAttribute("width", 1),
					dataSrc.getIntAttribute("height", 1));
			maxhp = dataSrc.getIntAttribute("hp", 10);
		}
	}
}
