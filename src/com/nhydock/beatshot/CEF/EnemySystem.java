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
import com.nhydock.beatshot.CEF.Groups.Bullet;
import com.nhydock.beatshot.CEF.Groups.Enemy;
import com.nhydock.beatshot.logic.level.Formation;
import com.nhydock.beatshot.logic.level.LevelData;

import static com.nhydock.beatshot.CEF.RenderSystem.FOV;

public class EnemySystem extends EntityProcessingSystem {

	public EnemySystem() {
		super(Aspect.getAspectForAll(Enemy.class).exclude(Bullet.class));
	}

	float distance;
	
	@Mapper ComponentMapper<Enemy> em;
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	
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
		Enemy enemy = em.get(e);
		
		Velocity v = vm.get(e);
		Position p = pm.get(e);
		
		if (!enemy.active)
		{
			if (pathm.getSafe(e) != null)
			{
				e.removeComponent(Path.class);
				e.changedInWorld();
			}
			v.y = -10;
			if (p.location.x < ((int)FOV[2] >> 1))
			{
				v.x = -30;
			}
			else
			{
				v.x = 30;
			}
			
			if (p.location.x < 0 || p.location.x > FOV[2])
			{
				e.deleteFromWorld();
				return;
			}
		}
	}

	/**
	 * Makes all currently visible enemies fly away
	 */
	public void killEnemies() {
		for (int i = 0; i < this.getActives().size(); i++)
		{
			Entity e = this.getActives().get(i);
			Enemy enemy = em.get(e);
			enemy.active = false;
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
