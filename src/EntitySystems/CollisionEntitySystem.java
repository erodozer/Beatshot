package EntitySystems;

import logic.Bullet.BulletEmitter;
import EntitySystems.Components.Bound;
import EntitySystems.Components.Health;
import EntitySystems.Components.Position;
import EntitySystems.Components.Group.Bullet;
import EntitySystems.Components.Group.Enemy;
import EntitySystems.Components.Group.Player;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;

import static logic.level.Level.FOV;

/**
 * @author nhydock
 *
 */
public class CollisionEntitySystem extends EntityProcessingSystem {

	public CollisionEntitySystem() {
		super(Aspect.getAspectForAll(Position.class, Bound.class, Bullet.class));
	}

	@Mapper ComponentMapper<Bound> physics;
	@Mapper ComponentMapper<Position> posm;
	
	@Mapper ComponentMapper<Enemy> em;
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<Bullet> bm;
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
		Player player = pm.getSafe(e);
		Enemy enemy = em.getSafe(e);

		Position pos = posm.get(e);
		Bound bound = physics.getSafe(e);
			

		if (handleOutOfBounds(e))
			return;
		
		if (player != null)
		{
			for (int i = 0; i < enemyEntities.size(); i++)
			{
				Entity collider = enemyEntities.get(i);
				enemy = (Enemy)collider.getComponent(Enemy.CType);
				if (!enemy.active)
					continue;
				
				Bound target = (Bound)collider.getComponent(Bound.CType);
				Position bpos = (Position)collider.getComponent(Position.CType);
				
				if (target != null)
				{
					if (doesCollide(pos, bound, bpos, target))
					{
						handleCollision(e, collider);
						return;
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
					if (doesCollide(pos, bound, bpos, target))
					{
						handleCollision(e, collider);
						return;
					}
				}
			}
		}
		
	}

	private boolean handleOutOfBounds(Entity e) {
		Position p = (Position)e.getComponent(Position.CType);
		
		if (p.location.x < 0 || p.location.x > FOV[2] || p.location.y < 0 || p.location.y > FOV[3])
		{
			e.deleteFromWorld();
			return true;
		}
		return false;
	}

	private boolean doesCollide(Position apos, Bound bullet, Position bpos, Bound target) {
		Vector2 a = new Vector2(apos.location.x + apos.offset.x, apos.location.y + apos.offset.y);
		Vector2 b = new Vector2(bpos.location.x + bpos.offset.x, bpos.location.y + bpos.offset.y);
		
		float dst = a.dst(b);
		float radius = bullet.radius;
		
		System.out.println(radius + " : " + dst + " : " + target.radius);
		System.out.println(dst-radius < target.radius);
		
		return dst-radius < target.radius;
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
			BulletEmitter.explode(target);
		}
		bullet.deleteFromWorld();
	}
}
