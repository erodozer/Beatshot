package com.nhydock.beatshot.CEF;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.artemis.components.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nhydock.beatshot.CEF.Components.Emitter;
import com.nhydock.beatshot.CEF.Groups.Bullet;
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

	private Vector2 emitV = new Vector2();
	
	@Override
	protected void process(Entity e) {
		Emitter emit = emap.get(e);
		Position pos = pmap.get(e);
		 
		Array<BulletData> fire = emit.update(world.delta);
		
		String group = (e == Engine.player) ? Player.TYPE : Enemy.TYPE; 
		
		for (int i = 0; i < fire.size; i++)
		{
			emitV.set(pos.location);
			BulletFactory.createBullet(world, emitV, fire.get(i), group);
		}
	}
}
