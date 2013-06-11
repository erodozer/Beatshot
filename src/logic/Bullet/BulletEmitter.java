package logic.Bullet;

import logic.Consts.DataDir;
import EntitySystems.Components.*;
import EntitySystems.Components.Group.Bullet;
import EntitySystems.Components.Group.Emitter;
import EntitySystems.Components.Group.Enemy;
import EntitySystems.Components.Group.Player;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.shipvgdc.sugdk.graphics.SpriteSheet;

public class BulletEmitter {

	private static SpriteSheet bulletSprites;
	
	static
	{
		bulletSprites = new SpriteSheet(Gdx.files.internal(DataDir.Images + "bullets.png"), 2, 2);
	}
	
	/**
	 * Creates a new emitter for the world
	 * @param e
	 * @param bulletLimit
	 * @param lifeSpan
	 * @param parent
	 * @return
	 */
	public static Entity createEmitter(Entity e, int bulletLimit, float lifeSpan, Entity parent)
	{
		e.addComponent(new Position(0, 0));		//position of the entity
		e.addComponent(new Velocity(0, 200f));	//speed at which the bullets fire
		e.addComponent(new Angle(0));			//angle at which the bullets fire
		e.addComponent(new Time(0.1f));		//bullet fire rate
		e.addComponent(new Limiter(0, 10)); 	//number of bullets that can be emitted
		e.addComponent(new Emitter(parent));	//identify as an emitter
		e.addComponent(new Anchor(parent));		//links the position of the emitter with the parent
		
		return e;
	}
	
	/*
	 * bullet data
	 */
	
	private static PolygonShape shape;
	
	static
	{
		shape = new PolygonShape();
		shape.setAsBox(1.0f, 1.0f);
	}
	
	/**
	 * Creates a new bullet to shoot out from the emitter
	 * @param e - entity to convert
	 * @param emitter
	 * @return Entity
	 */
	public static Entity createBullet(Entity e, Entity emitter)
	{
		Position p = emitter.getComponent(Position.class);
		Velocity v = emitter.getComponent(Velocity.class);
		Angle a = emitter.getComponent(Angle.class);
		
		e.addComponent(new Position(p.location.x+p.offset.x, p.location.y+p.offset.y), Position.CType);
		e.addComponent(new Velocity(v.x, v.y), Velocity.CType);
		e.addComponent(new Angle(a.degrees), Angle.CType);
		e.addComponent(new Time(5.0f), Time.CType);
		e.addComponent(new Bullet(emitter), Bullet.CType);
		e.addComponent(new Bound(shape), Bound.CType);
		
		Sprite s;
		if (emitter.getComponent(Player.class) != null)
		{
			s = new Sprite(bulletSprites.getFrame(1, 0));
			e.addComponent(new Player(), Player.CType);
		}
		else
		{
			s = new Sprite(bulletSprites.getFrame(0, 0));
			e.addComponent(new Enemy(), Enemy.CType);	
		}
		s.setPosition(p.location.x+p.offset.x, p.location.y+p.offset.y);
		e.addComponent(new Renderable(s), Renderable.CType);
		return e;
	}
}
