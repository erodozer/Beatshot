package EntitySystems.Components.Group;

import EntitySystems.Components.Ammo;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Enemy extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Enemy.class);
	
	
	public static final String TYPE = "Enemy";

	public boolean active;
	
}
