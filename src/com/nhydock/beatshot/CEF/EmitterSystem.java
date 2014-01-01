package com.nhydock.beatshot.CEF;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.artemis.components.*;
import com.badlogic.gdx.utils.Array;
import com.nhydock.beatshot.CEF.Groups.Bullet;
import com.nhydock.beatshot.CEF.Groups.Emitter;
import com.nhydock.beatshot.CEF.Groups.Enemy;
import com.nhydock.beatshot.CEF.Groups.Player;
import com.nhydock.beatshot.Factories.BulletFactory;
import com.nhydock.beatshot.logic.Engine;
import com.nhydock.beatshot.logic.Bullet.BulletData;

public class EmitterSystem extends EntityProcessingSystem {

	/**
	 * Creates a BulletLifeSystem
	 */
	public EmitterSystem() {
		super(Aspect.getAspectForAll(Emitter.class));
	}

	@Mapper ComponentMapper<Emitter> emap;
	@Mapper ComponentMapper<Position> pmap;

	@Override
	protected void process(Entity e) {
		Emitter emit = emap.get(e);
		Position pos = pmap.get(e);
		 
		Array<BulletData> fire = emit.update(world.delta);
		
		String group = (e == Engine.player) ? Player.TYPE : Enemy.TYPE; 
		
		for (int i = 0; i < fire.size; i++)
		{
			Entity bullet = BulletFactory.createBullet(pos.location, fire.get(i), world);

			world.getManager(GroupManager.class).add(bullet, Bullet.TYPE);
			world.getManager(GroupManager.class).add(bullet, group);
			
			bullet.addToWorld();
		}
	}
}
