package EntitySystems.Components;

import com.artemis.Component;

/**
 * A countdown timer component
 * @author nhydock
 *
 */
public class Time extends Component {
	/**
	 * Amount of time left
	 */
	public float curr;
	/**
	 * Amount of time when set
	 */
	public float start;
	
	public Time(float lifeLength)
	{
		this.start = this.curr = lifeLength;
	}
	
	public void update(float delta)
	{
		this.curr -= delta;
	}
}