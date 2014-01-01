package com.nhydock.beatshot.logic.Bullet;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author nhydock
 */
public class VelocityBullet implements BulletData {

	//velocity at which bullets should fire
	public Vector2 velocity; 
	public float rate;
	
	public VelocityBullet(Vector2 velocity, float rate)
	{
		this.velocity = velocity;
		this.rate = rate;
	}
	
	@Override
	public float getSpawnRate() {
		return rate;
	}

}
