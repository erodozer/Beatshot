package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Path extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Path.class);
	
	public com.shipvgdc.sugdk.util.pathfinding.Path path;
	public float duration;	

	/**
	 * @param path - path for the entity to follow
	 * @param duration - time it should take to traverse the path
	 */
	public Path(com.shipvgdc.sugdk.util.pathfinding.Path path, float duration) {
		this.path = path;
	}
}
