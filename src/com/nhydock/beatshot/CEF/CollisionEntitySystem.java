package com.nhydock.beatshot.CEF;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.artemis.components.*;
import com.badlogic.gdx.math.Vector2;
import com.nhydock.beatshot.CEF.Components.Bound;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Bullet;
import com.nhydock.beatshot.CEF.Groups.Enemy;
import com.nhydock.beatshot.CEF.Groups.Player;
import com.nhydock.beatshot.logic.Engine;
import com.nhydock.beatshot.logic.Bullet.BulletEmitter;

import static com.nhydock.beatshot.logic.level.Level.FOV;

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
	
	
    private ImmutableBag<Entity> enemyEntities;

    @Override
    public void initialize()
    {
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
				if (!enemy.active || enemy.dead)
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
			Entity collider = Engine.player;
			Bound target = physics.get(collider);
			Position bpos = posm.get(collider);
			if (doesCollide(pos, bound, bpos, target))
			{
				handleCollision(e, collider);
				return;
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

	//reserved memory for doing collision math so we don't have to create new vectors every time
	private static Vector2 bulletLoc = new Vector2();
	private static Vector2 targetLoc = new Vector2();
	
	private boolean doesCollide(Position apos, Bound bullet, Position bpos, Bound target) {
		bulletLoc.x = apos.location.x + apos.offset.x + bullet.center.x;
		bulletLoc.y = apos.location.y + apos.offset.y + bullet.center.y;
		
		targetLoc.x = bpos.location.x + bpos.offset.x + target.center.x;
		targetLoc.y = bpos.location.y + bpos.offset.y + target.center.y;
		
		float dst = Math.abs(bulletLoc.dst(targetLoc));
		float radius = bullet.radius;
		
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
		
		if (health.hp <= 0)
		{
			Enemy e = em.getSafe(target);
			if (e != null)
			{
				e.dead = true;
				Engine.score += 50;
			}
			Entity explosion = BulletEmitter.explode(target);
			world.getManager(GroupManager.class).add(explosion, "Dead");
		}
		bullet.deleteFromWorld();
	}
}
