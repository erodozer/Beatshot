package com.nhydock.beatshot.CEF;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.artemis.components.*;
import com.badlogic.gdx.utils.Array;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Enemy;
import com.nhydock.beatshot.Factories.ExplosionFactory;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.logic.level.Formation;

public class EnemySystem extends VoidEntitySystem {

	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Health> hm;
	
	protected Array<Entity> enemies = new Array<Entity>();
	
	public int aliveCount() {
		return enemies.size;
	}
	
	@Override
	public void processSystem()
	{
		int i = 0;
		
		while (i < enemies.size)
		{
			Entity e = enemies.get(i);
			
			if (e.isEnabled())
			{
				i++;
				process(e); 
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

	protected void process(Entity e) {
		
		Health hp = hm.getSafe(e);
		if (hp == null)
			return;
		
		if (hp.isDead())
		{
			//award points on kill
			BeatshotGame.score += 50;
			
			//show explosion
			Position p = pm.get(e);
			Entity explosion = ExplosionFactory.create(this.world, p.location, false);
			explosion.addToWorld();
			world.getManager(GroupManager.class).add(explosion, "effects");
			
			e.disable();
		}
		
	}
	
	public void clear() {
		
	}

	public void spawnEnemies(Formation f) {
		
		System.out.println(enemies);
		Array<Entity> spawned = f.spawn(this.world);
		
		for (int i = 0; i < spawned.size; i++)
		{
			Entity e = spawned.get(i);
			world.getManager(GroupManager.class).add(e, Enemy.TYPE);
			e.addToWorld();
		}
		
		enemies.addAll(spawned);
	}
	
}
