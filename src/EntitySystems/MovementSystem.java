package EntitySystems;

import logic.Bullet.BulletEmitter;

import EntitySystems.Components.Bound;
import EntitySystems.Components.Path;
import EntitySystems.Components.Position;
import EntitySystems.Components.Time;
import EntitySystems.Components.Velocity;
import EntitySystems.GroupComponents.Emitter;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class MovementSystem extends EntityProcessingSystem {

	public MovementSystem() {
		/**
		 * Select out all entities with positions
		 */
		super(Aspect.getAspectForAll(Position.class));
	}

	@Mapper ComponentMapper<Position> posmap;
	@Mapper ComponentMapper<Path> pathmap;
	@Mapper ComponentMapper<Time> timemap;
	@Mapper ComponentMapper<Velocity> velmap;
	@Mapper ComponentMapper<Emitter> emitmap;
	
	@Override
	protected void process(Entity e) {
		Position p = posmap.get(e);
		Path path = pathmap.get(e);
		Time t = timemap.get(e);
		Velocity v = velmap.get(e);
		
		//if entity is an emitter
		Emitter emit = emitmap.get(e);
		if (emit != null)
		{
			Position p1 = posmap.get(e);
			Position p2 = posmap.get(emit.parent);
			
			p1.location.x = p2.location.x;
			p1.location.y = p2.location.y;
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
			p.location.x = vec.x;
			p.location.y = vec.y;
		}
		//entity has velocity
		else if (v != null)
		{
			p.location.x += v.x * world.delta;
			p.location.y += v.y * world.delta;
		}
	}

}
