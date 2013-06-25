package CEF.Groups;

import logic.Bullet.BulletEmitter;

import CEF.Components.Ammo;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class Bullet extends Component {
	public static final String TYPE = "Bullet";

	public static ComponentType CType = ComponentType.getTypeFor(Bullet.class);
	
	public Entity emitter;
	
	public Bullet(Entity from)
	{
		this.emitter = from;
	}
}
