package EntitySystems.GroupComponents;

import logic.Bullet.BulletEmitter;

import com.artemis.Component;
import com.artemis.Entity;

public class Bullet extends Component {
	public Entity emitter;
	
	public Bullet(Entity from)
	{
		this.emitter = from;
	}
}
