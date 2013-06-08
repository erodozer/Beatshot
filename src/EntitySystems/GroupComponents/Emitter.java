package EntitySystems.GroupComponents;

import com.artemis.Component;
import com.artemis.Entity;

public class Emitter extends Component {
	public final String TYPE = "Emitter";
	
	/**
	 * Entity link for position tracking
	 */
	public Entity parent;
	
	/**
	 * Create an emitter
	 * @param parent - actor to link the emitter to
	 */
	public Emitter(Entity parent)
	{
		this.parent = parent;
	}
}
