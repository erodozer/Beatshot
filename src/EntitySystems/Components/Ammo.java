package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Ammo extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Ammo.class);
	
	public Ammo(int ammo) {
		this.ammo = this.maxammo = ammo;
	}

	public float ammo;
	public final float maxammo;
	public boolean recharge;
}
