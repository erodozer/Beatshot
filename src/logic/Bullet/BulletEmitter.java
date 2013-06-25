package logic.Bullet;

import CEF.Components.*;
import CEF.Groups.Bullet;
import CEF.Groups.Emitter;
import CEF.Groups.Enemy;
import CEF.Groups.Player;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import core.Consts.DataDir;
import util.SpriteSheet;

/**
 * Factory for creating bullet emitters for entities
 * @author nhydock
 *
 */
public class BulletEmitter {

	private static SpriteSheet bulletSprites;
	private static SpriteSheet explosion;
	
	static
	{
		bulletSprites = new SpriteSheet(Gdx.files.internal(DataDir.Images + "bullets.png"), 2, 2);
		explosion = new SpriteSheet(Gdx.files.internal(DataDir.Images + "bang.png"), 4, 1);
	}
	
	/**
	 * Creates a new emitter for the world
	 * @param e
	 * @param bulletLimit - number of bullets that can be present from the emitter at one time
	 * @param spawnRate - rate per second at which bullets will spawn from the emitter
	 * @param parent
	 * @return Entity converted to emitter
	 */
	public static Entity createEmitter(Entity e, int bulletLimit, float spawnRate, Entity parent)
	{
		e.addComponent(new Position(), Position.CType);		//position of the entity
		e.addComponent(new Velocity(0, 200f), Velocity.CType);	//speed at which the bullets fire
		e.addComponent(new Angle(0), Angle.CType);				//angle at which the bullets fire
		e.addComponent(new Time(spawnRate), Time.CType);		//bullet fire rate
		e.addComponent(new Limiter(0, 10), Limiter.CType); 		//number of bullets that can be emitted
		e.addComponent(new Emitter(parent), Emitter.CType);		//identify as an emitter
		e.addComponent(new Anchor(parent), Anchor.CType);		//links the position of the emitter with the parent
		
		return e;
	}
	
	/*
	 * bullet data
	 */
	
	private static final PolygonShape shape;
	
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
		e.addComponent(new Bound(1.0f, 1.0f), Bound.CType);
		
		Emitter emit = (Emitter)emitter.getComponent(Emitter.CType);
		
		Sprite s;
		if (emit.parent.getComponent(Enemy.class) == null)
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
	
	public static Entity explode(Entity orig)
	{
		Entity e = orig.getWorld().createEntity();
		
		Sprite s = new Sprite(explosion.getFrame(0));
		e.addComponent(new Renderable(s));
		
		Animation a = new Animation(explosion.getTexture(), explosion.frameCount, .08f, false);
		e.addComponent(a, Animation.CType);
		
		Position p = (Position) orig.getComponent(Position.CType);
		e.addComponent(new Position(p.location, p.offset));
		
		e.addToWorld();
		orig.deleteFromWorld();
		
		return e;
	}
}
