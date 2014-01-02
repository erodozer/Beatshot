package com.nhydock.beatshot.logic.Bullet;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

public class PathBullet implements BulletData{
	//determined path to generate on
	public Path<Vector2> path;
	
	//number of bullets to generate along the path
	public int count;
	
	//time it should take to spawn all bullets
	public float duration;

	@Override
	public float getSpawnRate() {
		// TODO Auto-generated method stub
		return 0;
	}

}
