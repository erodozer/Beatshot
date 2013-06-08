package EntitySystems;

import EntitySystems.Components.Animation;
import EntitySystems.Components.Position;
import EntitySystems.Components.Renderable;
import EntitySystems.Components.Scrollable;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * System that renders the field
 * @author nhydock
 */
public class RenderSystem extends EntitySystem {

	public RenderSystem() {
		super(Aspect.getAspectForAll(Position.class, Renderable.class));
	}

	@Mapper ComponentMapper<Position> pmap;
	@Mapper ComponentMapper<Renderable> rmap;
	@Mapper ComponentMapper<Scrollable> smap;
	@Mapper ComponentMapper<Animation> amap;

	//system has its own drawing components
	OrthographicCamera camera;
	
	float[] fieldOfView = {0, 0, 240, 320};

	@Override
	protected void initialize()
	{
		camera = new OrthographicCamera(240, 320);
		camera.position.set(70, 85, 0);
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			Renderable r = rmap.get(e);
			Position p = pmap.get(e);
			r.sprite.setPosition(p.location.x+p.offset.x, p.location.y+p.offset.y);
			
			//scroll sprite's texture if renderable has scrollable as well
			Scrollable s = smap.get(e);
			if (s != null)
			{
				s.update(world.delta);
				r.sprite.setRegion(s.u1, s.v1, s.u2, s.v2);
			}
		}
	}
	
	public boolean inView(Vector2 loc)
	{
		return loc.x >= fieldOfView[0] && loc.x <= fieldOfView[2] && loc.y >= fieldOfView[1] && loc.y <= fieldOfView[3];
	}

	
	public void draw(SpriteBatch batch)
	{
		this.process();
		
		//temp variable name holders
		Renderable r;
		Entity e;
		
		//we group the different parts to maximize efficiency
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		//get and render the field
		batch.begin();
		e = this.world.getManager(TagManager.class).getEntity("Field");
		r = rmap.get(e);
		r.sprite.draw(batch);
		batch.end();
		
		//render the player
		batch.begin();
		e = this.world.getManager(TagManager.class).getEntity("Player");
		r = rmap.get(e);
		r.sprite.draw(batch);
		batch.end();

		batch.begin();
		//render enemy bullets
		batch.end();
		
		batch.begin();
		//render enemies
		batch.end();
		
		batch.begin();
		//render player bullets
		batch.end();
	}
	
}
