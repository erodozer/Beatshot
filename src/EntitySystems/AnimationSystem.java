package EntitySystems;

import EntitySystems.Components.Animation;
import EntitySystems.Components.Renderable;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.DelayedEntityProcessingSystem;
import com.artemis.systems.EntityProcessingSystem;

/**
 * Takes care of managing and updated entities with animations
 * @author nhydock
 */
public class AnimationSystem extends EntityProcessingSystem {

	public AnimationSystem() {
		super(Aspect.getAspectForAll(Animation.class, Renderable.class));
	}

	@Mapper ComponentMapper<Animation> amap;
	@Mapper ComponentMapper<Renderable> rmap;

	@Override
	protected void process(Entity e) {
		Animation a = amap.get(e);
		
		a.update(world.delta);
		
		Renderable r = rmap.get(e);
		r.sprite.setRegion(a.u1, a.v1, a.u2, a.v2);
	}

}
