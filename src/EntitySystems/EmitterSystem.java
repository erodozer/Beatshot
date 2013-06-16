package EntitySystems;

import logic.Engine;
import logic.Bullet.BulletEmitter;
import EntitySystems.Components.Ammo;
import EntitySystems.Components.Limiter;
import EntitySystems.Components.Position;
import EntitySystems.Components.Time;
import EntitySystems.Components.Velocity;
import EntitySystems.Components.Group.Emitter;
import EntitySystems.Components.Group.Enemy;
import EntitySystems.Components.Group.Player;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;

public class EmitterSystem extends EntityProcessingSystem {

	/**
	 * Creates a BulletLifeSystem
	 */
	public EmitterSystem() {
		super(Aspect.getAspectForAll(Emitter.class));
	}

	@Mapper ComponentMapper<Time> tmap;
	@Mapper ComponentMapper<Position> pmap;
	@Mapper ComponentMapper<Velocity> vmap;
	@Mapper ComponentMapper<Emitter> emap;
	@Mapper ComponentMapper<Enemy> enemies;
	
	//emitter handling
	@Mapper ComponentMapper<Limiter> lmap;
	@Mapper ComponentMapper<Player> playerMap;

	@Override
	protected void process(Entity e) {
		Time t = tmap.get(e);
		Emitter emit = emap.get(e);
		
		//update bullet
		if (emit.active)
		{
			t.update(world.delta);
			if (t.curr < 0)
			{
				processExpired(e);
			}
		}
		
		Enemy enemy = enemies.getSafe(emit.parent);
		if (enemy != null)
		{
			emit.active = enemy.active;
			Position p = pmap.get(e);
			Velocity v = vmap.get(e);
			
			float diff = pmap.get(Engine.player).location.x - p.location.x;
			v.x = diff;
		}
	}

	protected void processExpired(Entity e) {
		if (playerMap.getSafe(e) != null)
		{
			Ammo a = (Ammo)Engine.player.getComponent(Ammo.CType);
			if (a.ammo <= 0 || a.recharge)
			{
				return;
			}
			a.ammo = Math.max(a.ammo-1, 0);
			a.recharge = (a.ammo <= 0);
		}
		
		Time t = tmap.get(e);
		t.curr = t.start;
	
		Entity bullet = world.createEntity();
		bullet = BulletEmitter.createBullet(bullet, e);
		world.getManager(GroupManager.class).add(bullet, "Bullet");
		//spawn a new bullet
		bullet.addToWorld();
		
		Limiter l = lmap.get(e);
		l.current++;
	}
}
