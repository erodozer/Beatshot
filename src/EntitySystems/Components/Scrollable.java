package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.badlogic.gdx.math.Vector2;

/**
 * Allows a texture on an entity to repeat and even scroll over time
 * @author nhydock
 *
 */
public class Scrollable extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Scrollable.class);
	

	private final Vector2 rate;
	private final Vector2 repeat;
	
	public float u1, u2, v1, v2;
	
	public Scrollable(final float x, final float y, final float repeatX, final float repeatY)
	{
		rate = new Vector2(x, y);
		repeat = new Vector2(repeatX, repeatY);
		
		this.u1 = 0.0f;
		this.v1 = 0.0f;
		this.u2 = repeatX;
		this.v2 = repeatY;
	}
	
	public void update(float delta)
	{
		
		if (u1 > repeat.x) u1 -= repeat.x;
		else if (u1 < -repeat.x) u1 += repeat.x;
		else u1 += rate.x*delta;
		
		if (v1 > repeat.y) v1 -= repeat.y;
		else if (v1 < -repeat.y) v1 += repeat.y;
		else v1 += rate.y*delta;
		
		u2 = u1+repeat.x;	
		v2 = v1+repeat.y;
	}
}
