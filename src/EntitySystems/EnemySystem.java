package EntitySystems;

import logic.level.Level;
import logic.level.LevelData.Spawn;
import logic.level.LevelData.SpawnSet;
import EntitySystems.Components.Position;
import EntitySystems.Components.Velocity;
import EntitySystems.Components.Group.Bullet;
import EntitySystems.Components.Group.Enemy;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Array;

public class EnemySystem extends EntityProcessingSystem {

	public EnemySystem() {
		super(Aspect.getAspectForAll(Enemy.class).exclude(Bullet.class));
	}

	float distance;
	
	@Mapper ComponentMapper<Enemy> em;
	
	public void processEntities(Array<Entity> enemies)
	{
		for (int i = 0; i < enemies.size; i++)
		{
			Entity e = enemies.get(i);
			process(e);
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void process(Entity e) {
		Enemy enemy = em.get(e);
		
		if (!enemy.active)
		{
			Velocity v = (Velocity)e.getComponent(Velocity.CType);
			Position p = (Position)e.getComponent(Position.CType);
			v.y = -10;
			if (p.location.x < ((int)Level.FOV[2] >> 1))
			{
				v.x = -30;
			}
			else
			{
				v.x = 30;
			}
			
			if (p.location.x < 0 || p.location.x > Level.FOV[2])
			{
				world.deleteEntity(e);
			}
		}
		else
		{
			
		}
	}

	public Array<Entity> killEnemies(Array<Entity> enemies) {
		for (int i = 0; i < enemies.size; i++)
		{
			Entity e = enemies.get(i);
			Enemy enemy = em.get(e);
			enemy.active = false;
		}
		
		return enemies;
	}

	public Array<Entity> spawnEnemies(SpawnSet spawnSet) {
		
		Array<Entity> enemies = new Array<Entity>();
		
		for (int i = 0; i < spawnSet.spawns.size; i++)
		{
			Spawn s = spawnSet.spawns.get(i);
			Entity e = world.createEntity();
			spawnSet.atlas.createEnemy(s.name, e);
			
			
		}
		
		return null;
	}
	
}
