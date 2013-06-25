package CEF;

import static logic.level.Level.FOV;
import logic.Bullet.BulletEmitter;
import logic.level.LevelData;
import logic.level.LevelData.Spawn;
import logic.level.LevelData.SpawnSet;
import CEF.Components.Animation;
import CEF.Components.Bound;
import CEF.Components.Path;
import CEF.Components.Position;
import CEF.Components.Renderable;
import CEF.Components.Time;
import CEF.Components.Velocity;
import CEF.Groups.Bullet;
import CEF.Groups.Enemy;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class EnemySystem extends EntityProcessingSystem {

	public EnemySystem() {
		super(Aspect.getAspectForAll(Enemy.class).exclude(Bullet.class));
	}

	float distance;
	
	@Mapper ComponentMapper<Enemy> em;
	@Mapper ComponentMapper<Animation> am;
	
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Path> pathm;
	
	
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
				e.disable();
			}
		}
		else
		{
			if (p.location.y > FOV[3]-30)
			{
				v.y = -40;
			}
			else
			{
				v.y = 0;
				if (pathm.getSafe(e) == null)
				{
					if (!enemy.movementPath.equals("null"))
					{
						System.out.println("yes");
						Time t = new Time(1.5f);
						t.loop = true;
						e.addComponent(t);
						e.addComponent(new Path(LevelData.enemyPaths.get(enemy.movementPath), 1.5f));
						e.changedInWorld();
					}
				}
			}
			
			Animation a = am.getSafe(e);
			if (a != null)
			{
				if (a.done)
				{
					e.disable();
				}
			}
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
			Enemy enemy = (Enemy)e.getComponent(Enemy.CType);
			enemy.active = true;
			enemy.movementPath = s.path;
			Position p = (Position)e.getComponent(Position.CType);
			p.location.x = s.pos.x;
			p.location.y = s.pos.y + 250f;
			p.offset.x = s.offset.x;
			p.offset.y = s.offset.y;
			Sprite sprite = ((Renderable)e.getComponent(Renderable.CType)).sprite;
			sprite.setPosition(p.location.x + p.offset.x, p.location.y + p.offset.y);
			world.getManager(GroupManager.class).add(e, "Enemy");
			e.addToWorld();
			enemies.add(e);
		}
		
		return enemies;
	}
	
}
