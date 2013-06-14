package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Bound extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Bound.class);
	
	private final float width;
	private final float height;
	public final float radius;
	
	public Bound(float width, float height)
	{
		this.width = width;
		this.height = height;
		
		this.radius = (float)Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
	}
}
