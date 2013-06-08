package EntitySystems;

import logic.Bullet.BulletEmitter;

import EntitySystems.Components.*;
import EntitySystems.GroupComponents.*;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.DelayedEntityProcessingSystem;
import com.artemis.utils.Bag;

/**
 * This entity system is solely for deleting bullets that have reached the end of their time
 * and have not hit a desired targeted.  It is also used for spawning new bullets from bullet
 * emitters and sending them on their way.
 * @author nhydock
 */
public class BulletLifeSystem extends DelayedEntityProcessingSystem {

	/**
	 * Creates a BulletLifeSystem
	 */
	public BulletLifeSystem() {
		super(Aspect.getAspectForAll(Time.class).one(Emitter.class, Bullet.class));
	}

	@Mapper ComponentMapper<Time> tmap;
	@Mapper ComponentMapper<Bullet> bmap;
	@Mapper ComponentMapper<Emitter> emap;
	
	//used for generating bullets
	@Mapper ComponentMapper<Position> pmap;
	@Mapper ComponentMapper<Velocity> vmap;
	@Mapper ComponentMapper<Angle> amap;
	
	//emitter handling
	@Mapper ComponentMapper<Limiter> lmap;
	
	@Override
	protected float getRemainingDelay(Entity e) {
		Time t = tmap.get(e);
		return t.curr;
	}

	@Override
	protected void processDelta(Entity e, float delta) {
		Time t = tmap.get(e);
		t.update(delta);
	}

	@Override
	protected void processExpired(Entity e) {
		//handle emitters
		if (emap.get(e) != null)
		{
			Time t = tmap.get(e);
			t.curr = t.start;
		
			//spawn a new bullet
			Entity bullet = world.createEntity();
			bullet = BulletEmitter.createBullet(bullet, e);
			
			bullet.addToWorld();
			
			Limiter l = lmap.get(e);
			l.current++;
		}
		//handle normal bullets
		else
		{
			Bullet b = bmap.get(e);
			Limiter l = lmap.get(b.emitter);
			l.current--;
			
			//remove from world when life has reached the end
			world.deleteEntity(e);
		}
	}

}
