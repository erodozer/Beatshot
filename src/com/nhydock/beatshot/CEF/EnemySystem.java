package com.nhydock.beatshot.CEF;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.artemis.components.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Bullet;
import com.nhydock.beatshot.CEF.Groups.Enemy;
import com.nhydock.beatshot.Factories.ExplosionFactory;
import com.nhydock.beatshot.logic.Engine;
import com.nhydock.beatshot.logic.level.Formation;
import com.nhydock.beatshot.logic.level.LevelData;

import static com.nhydock.beatshot.CEF.RenderSystem.FOV;

public class EnemySystem extends EntityProcessingSystem {

	public EnemySystem() {
		super(Aspect.getAspectForAll(Enemy.class).exclude(Bullet.class));
	}
	
	@Mapper ComponentMapper<Enemy> em;
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Health> hm;
	
	public void processEntities(Array<Entity> enemies)
	{
		int i = 0;
		
		while (i < enemies.size)
		{
			Entity e = enemies.get(i);
			process(e);
			
			if (e.isEnabled())
			{
				i++;
			}
			else
			{
				enemies.removeIndex(i);
				e.deleteFromWorld();
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void process(Entity e) {
		
		Health hp = hm.get(e);
		if (hp.isDead())
		{
			//award points on kill
			Engine.score += 50;
			
			//show explosion
			Position p = pm.get(e);
			Entity explosion = ExplosionFactory.create(this.world, p.location);
			explosion.addToWorld();
			world.getManager(GroupManager.class).add(explosion, "effects");
			
			e.disable();
		}
	}

	public Array<Entity> spawnEnemies(Formation f) {
		
		Array<Entity> enemies = f.spawn(this.world);
		
		for (int i = 0; i < enemies.size; i++)
		{
			Entity e = enemies.get(i);
			Enemy enemy = (Enemy)e.getComponent(Enemy.CType);
			enemy.active = true;
			world.getManager(GroupManager.class).add(e, "Enemy");
			e.addToWorld();
		}
		
		return enemies;
	}
	
}
