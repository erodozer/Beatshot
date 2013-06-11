package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.badlogic.gdx.Gdx;

public class Angle extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Angle.class);
	
	public float degrees;
	
	public Angle(float degrees)
	{
		this.degrees = degrees;
	}
}
