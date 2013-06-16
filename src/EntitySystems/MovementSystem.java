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
		
		//if entity is anchored
		if (anchor != null)
		{
			Position p1 = posmap.get(e);
			Position p2 = posmap.getSafe(anchor.link);
			//if the object is anchored to nother then it should be removed from the world
			// ex. an enemy dies an its emitter still exists for a moment
			if (p2 != null)
			{
				p1.location.x = p2.location.x+p2.offset.x;
				p1.location.y = p2.location.y+p2.offset.y;
			}
			else
			{
				world.deleteEntity(e);
			}
			return;
		}
		//entity has a path
		else if (path != null)
		{
			t.update(world.delta);
			Vector2 vec = path.path.getContinuousLocation((path.duration - t.curr)/path.duration);
			System.out.println(vec);
			p.offset.x = vec.x;
			p.offset.y = vec.y;
		}
		//entity has velocity
		else if (v != null)
		{
			p.location.x += v.x * world.delta;
			p.location.y += v.y * world.delta;
		}
	}

}
