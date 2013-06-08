package EntitySystems.Components;

import com.artemis.Component;

public class Path extends Component {
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
