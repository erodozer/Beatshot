package logic.Bullet;

import EntitySystems.Components.*;
import EntitySystems.GroupComponents.Bullet;
import EntitySystems.GroupComponents.Emitter;
import EntitySystems.GroupComponents.Enemy;
import EntitySystems.GroupComponents.Player;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class BulletEmitter {

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
		e.addComponent(new Velocity(0, 0));		//speed at which the bullets fire
		e.addComponent(new Angle(0));			//angle at which the bullets fire
		e.addComponent(new Time(1.0f));			//bullet fire rate
		e.addComponent(new Limiter(0, 10)); 	//number of bullets that can be emitted
		e.addComponent(new Emitter(parent));	//identify as an emitter
		
		if (parent.getComponent(Player.class) == null)
			e.addComponent(new Enemy());
		else
			e.addComponent(new Player());
		
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
	 * @param e
	 * @param emitter
	 * @return
	 */
	public static Entity createBullet(Entity e, Entity emitter)
	{
		Position p = emitter.getComponent(Position.class);
		Velocity v = emitter.getComponent(Velocity.class);
		Angle a = emitter.getComponent(Angle.class);
		
		e.addComponent(new Position(p.location.x, p.location.y));
		e.addComponent(new Velocity(v.x, v.y));
		e.addComponent(new Angle(a.degrees));
		e.addComponent(new Time(5.0f));
		e.addComponent(new Bullet(emitter));
		e.addComponent(new Bound(shape));
		
		return e;
	}
}
