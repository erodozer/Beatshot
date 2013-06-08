package EntitySystems;

import EntitySystems.Components.Bound;
import EntitySystems.Components.Health;
import EntitySystems.Components.Position;
import EntitySystems.GroupComponents.Bullet;
import EntitySystems.GroupComponents.Player;
import EntitySystems.GroupComponents.Enemy;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

/**
 * @author nhydock
 *
 */
public class CollisionEntitySystem extends EntityProcessingSystem {

	public CollisionEntitySystem() {
		super(Aspect.getAspectForAll(Bound.class, Bullet.class));
	}

	@Mapper ComponentMapper<Bound> physics;
	
	@Mapper ComponentMapper<Enemy> em;
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<Position> posm;
	
	@Mapper ComponentMapper<Health> hm;
	
    private ImmutableBag<Entity> playerEntities;
    private ImmutableBag<Entity> enemyEntities;

    @Override
    public void initialize()
    {
    	playerEntities = world.getManager(GroupManager.class).getEntities("Player");
    	enemyEntities = world.getManager(GroupManager.class).getEntities("Enemy");
    }
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void process(Entity e) {
		Player player = pm.get(e);
		Enemy enemy = em.get(e);
		Position pos = posm.get(e);
		
		Bound bullet = physics.get(e);
		
		if (player != null)
		{
			for (int i = 0; i < enemyEntities.size(); i++)
			{
				Entity collider = enemyEntities.get(i);
				Bound target = physics.get(collider);
				Position bpos = posm.get(collider);
				if (target != null)
				{
					if (doesCollide(pos, bullet, bpos, target));
					{
						handleCollision(e, collider);
					}
				}
			}
		}
		else if (enemy != null)
		{
			for (int i = 0; i < playerEntities.size(); i++)
			{
				Entity collider = playerEntities.get(i);
				Bound target = physics.get(collider);
				Position bpos = posm.get(collider);
				if (target != null)
				{
					if (doesCollide(pos, bullet, bpos, target));
					{
						handleCollision(e, collider);
					}
				}
			}
		}
	}

	private boolean doesCollide(Position apos, Bound bullet, Position bpos, Bound target) {
		Vector2 a = new Vector2(apos.location.x + apos.offset.x, apos.location.y + apos.offset.y);
		Vector2 b = new Vector2(bpos.location.x + bpos.offset.x, bpos.location.y + bpos.offset.y);
		
		float dst = a.dst(b);
		float radius = bullet.shape.getRadius();
		
		return dst-radius < target.shape.getRadius();
	}

	/**
	 * 
	 * @param bullet - entity that caused the collision
	 * @param target - entity hit
	 */
	private void handleCollision(Entity bullet, Entity target)
	{
		Health health = hm.get(target);
		
		health.hp -= 10;
		
		if (health.hp < 0)
		{
			target.deleteFromWorld();
		}
	}
}
