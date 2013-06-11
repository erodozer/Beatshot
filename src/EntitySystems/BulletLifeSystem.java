package EntitySystems;

import logic.Engine;
import logic.Bullet.BulletEmitter;

import EntitySystems.Components.*;
import EntitySystems.Components.Group.*;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;

/**
 * This entity system is solely for deleting bullets that have reached the end of their time
 * and have not hit a desired targeted.  It is also used for spawning new bullets from bullet
 * emitters and sending them on their way.
 * @author nhydock
 */
public class BulletLifeSystem extends EntityProcessingSystem {

	/**
	 * Creates a BulletLifeSystem
	 */
	public BulletLifeSystem() {
		super(Aspect.getAspectForAll(Bullet.class));
	}

	@Mapper ComponentMapper<Time> tmap;
	@Mapper ComponentMapper<Bullet> bmap;
	
	//emitter handling
	@Mapper ComponentMapper<Limiter> lmap;
	@Mapper ComponentMapper<Player> playerMap;

	protected void process(Entity e) {
		Time t = tmap.get(e);
		
		//update bullet
		t.update(world.delta);
		
		if (t.curr < 0)
		{
			processExpired(e);
		}
	}

	protected void processExpired(Entity e) {
		//handle normal bullets
		Bullet b = bmap.get(e);
		Limiter l = lmap.get(b.emitter);
		l.current--;
		
		//remove from world when life has reached the end
		world.deleteEntity(e);
	}
}
