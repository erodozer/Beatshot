package logic;

import logic.Bullet.BulletEmitter;
import logic.Consts.DataDir;
import logic.Consts.Lasers;

import scenes.Main.PlayerNotification;

import EntitySystems.Components.*;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.shipvgdc.sugdk.graphics.SpriteSheet;
import com.shipvgdc.sugdk.util.Observable;

/**
 * 
 * @author nhydock
 *
 */
public class Player extends Observable<PlayerNotification> {

	public static final int MAXHP = 100;
	public static final int MAXAMMO = 100;
	
	private static SpriteSheet sprite;
	private static PolygonShape shape;
	
	static
	{
		shape = new PolygonShape();
		shape.setAsBox(1.0f, 1.0f);
		
		sprite = new SpriteSheet(Gdx.files.internal(DataDir.Images + "player.png"), 4, 1);
	}

	/**
	 * Formats an entity into a player
	 * @param e - entity to convert
	 * @return a formatted player entity
	 */
	public static Entity createEntity(Entity e) {
		e.addComponent(new Health(MAXHP));
		
		e.addComponent(new Position(0, 0));
		e.addComponent(new Velocity(0, 0));
		
		e.addComponent(new Bound(shape));
		
		e.addComponent(new Renderable(sprite.getFrame(0)));
		e.addComponent(new Animation(sprite.getTexture(), sprite.frameCount, .1667f, true));
		
		e.addComponent(new EntitySystems.GroupComponents.Player());
		e.addComponent(new InputHandler());
		
		for (int i = 0; i < Lasers.values().length; i++)
		{
			Entity laser = e.getWorld().createEntity();
			laser = BulletEmitter.createEmitter(laser, 10, .1f, e);
			laser.addToWorld();
		}
		
		return e;
	}

}
