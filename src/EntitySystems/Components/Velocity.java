package EntitySystems.Components;

import com.artemis.Component;

/**
 * Allows an entity to move in a direction at a constant rate
 * @author nhydock
 *
 */
public class Velocity extends Component {
	public float x;
	public float y;
	
	public Velocity(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
}
