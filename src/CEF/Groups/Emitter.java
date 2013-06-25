package CEF.Groups;

import CEF.Components.Ammo;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public class Emitter extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Emitter.class);
	
	public final String TYPE = "Emitter";
	
	/**
	 * Entity link for bullet typing
	 */
	public Entity parent;

	/**
	 * Says if the emitter should be firing
	 */
	public boolean active;
	
	/**
	 * Create an emitter
	 * @param parent - actor to link the emitter to
	 */
	public Emitter(Entity parent)
	{
		this.parent = parent;
	}
}
