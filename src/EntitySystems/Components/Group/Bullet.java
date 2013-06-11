package EntitySystems.Components.Group;

import logic.Bullet.BulletEmitter;

import EntitySystems.Components.Ammo;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class Bullet extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Bullet.class);
	
	public Entity emitter;
	
	public Bullet(Entity from)
	{
		this.emitter = from;
	}
}
