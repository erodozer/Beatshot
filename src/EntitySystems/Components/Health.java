package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Health extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Health.class);
	
	public int hp, maxhp;
	
	public Health(int maxhp)
	{
		this.hp = this.maxhp = maxhp;
	}
}
