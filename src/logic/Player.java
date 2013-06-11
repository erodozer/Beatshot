package logic;

import logic.Bullet.BulletEmitter;
import logic.Consts.DataDir;
import logic.Consts.Lasers;

import scenes.Main.PlayerNotification;

import EntitySystems.Components.*;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
	private static Texture shadow_sprite;
	
	private static PolygonShape shape;
	
	static
	{
		shape = new PolygonShape();
		shape.setAsBox(1.0f, 1.0f);
		
		sprite = new SpriteSheet(Gdx.files.internal(DataDir.Images + "player.png"), 4, 1);
		shadow_sprite = new Texture(Gdx.files.internal(DataDir.Images + "player_shadow.png"));
		
	}

	/**
	 * Formats an entity into a player
	 * @param e - entity to convert
	 * @return a formatted player entity
	 */
	public static Entity createEntity(Entity e) {
		World w = e.getWorld();
		
		e.addComponent(new Health(MAXHP));
		e.addComponent(new Ammo(MAXAMMO));
		
		e.addComponent(new Position(0, 0, 0, 5));
		e.addComponent(new Velocity(0, 0));
		
		e.addComponent(new Bound(shape));
		
		e.addComponent(new Renderable(sprite.getFrame(0)));
		e.addComponent(new Animation(sprite.getTexture(), sprite.frameCount, .1667f, true));
		e.addComponent(new EntitySystems.Components.Group.Player());
		e.addComponent(new InputHandler(new int[]{Input.Keys.LEFT, Input.Keys.RIGHT}));
		
		float x = 0;
		for (int i = 0; i < Lasers.values().length; i++, x += 5f)
		{
			Entity laser = e.getWorld().createEntity();
			laser = BulletEmitter.createEmitter(laser, 10, .1f, e);
			Position p = (Position) laser.getComponent(Position.CType);
			p.offset.x = x;
			p.offset.y = sprite.getFrameHeight();
			laser.addComponent(new InputHandler(Lasers.values()[i].keys));
			laser.addComponent(new EntitySystems.Components.Group.Player());
			laser.addToWorld();
		}
		
		Entity shadow = e.getWorld().createEntity();
		shadow.addComponent(new Position(0,0,0,-2));
		shadow.addComponent(new Anchor(e));
		shadow.addComponent(new Renderable(new Sprite(shadow_sprite)));
		e.getWorld().getManager(TagManager.class).register("PlayerShadow", shadow);
		shadow.addToWorld();
		
		return e;
	}

}
