package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

/**
 * Allows an entity to move in a direction at a constant rate
 * @author nhydock
 *
 */
public class Velocity extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Velocity.class);
	
	public float x;
	public float y;
	
	public Velocity(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Velocity() {
		this(0, 0);
	}
}
