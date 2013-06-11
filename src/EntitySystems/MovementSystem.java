package EntitySystems;

import logic.Engine;
import EntitySystems.Components.*;
import EntitySystems.Components.Group.Enemy;
import EntitySystems.Components.Group.Player;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;

public class MovementSystem extends EntityProcessingSystem {

	public MovementSystem() {
		/**
		 * Select out all entities with positions
		 */
		super(Aspect.getAspectForAll(Position.class).exclude(Enemy.class).one(Velocity.class, Path.class, Anchor.class, Angle.class, Rotation.class));
	}

	@Mapper ComponentMapper<Position> posmap;
	@Mapper ComponentMapper<Angle> angmap;
	@Mapper ComponentMapper<Path> pathmap;
	@Mapper ComponentMapper<Time> timemap;
	@Mapper ComponentMapper<Velocity> velmap;
	@Mapper ComponentMapper<Rotation> rotmap;
	@Mapper ComponentMapper<Anchor> ancmap;
	
	@Mapper ComponentMapper<Player> playerMap;
	
	
	@Override
	protected void process(Entity e) {
		Position p = posmap.get(e);
		Time t = timemap.getSafe(e);
		
		Velocity v = velmap.getSafe(e);
		Path path = pathmap.getSafe(e);
		Anchor anchor = ancmap.getSafe(e);
		
		//if entity is anchored
		if (anchor != null)
		{
			Position p1 = posmap.get(e);
			Position p2 = posmap.get(anchor.link);
			
			p1.location.set(p2.location.x+p2.offset.x, p2.location.y+p2.offset.y);
		}
		//entity has a path
		else if (path != null)
		{
			if (t.curr > path.duration)
			{
				e.deleteFromWorld();
				return;
			}
			t.curr += world.delta;
			Vector2 vec = path.path.getContinuousLocation(t.curr/path.duration);
			p.location.set(vec);
		}
		//entity has velocity
		else if (v != null)
		{
			p.location.add(v.x * world.delta, v.y * world.delta);
		}
		
		Angle a = angmap.getSafe(e);
		//rotate the angle
		if (a != null)
		{
			Rotation r = rotmap.getSafe(e);
			if (r != null)
			{
				a.degrees += r.rate * world.delta;
			}
		}
	}

}
