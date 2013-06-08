package EntitySystems.Components;

import com.artemis.Component;

/**
 * Allows a number limit to be set on something, and to keep it's current value
 * @author nhydock
 *
 */
public class Limiter extends Component {
	public float current;
	public final float max;
	public final float min;
	
	public Limiter(float min, float max)
	{
		this.min = min;
		this.max = max;
		this.current = Math.max(0, min);
	}
	
}
