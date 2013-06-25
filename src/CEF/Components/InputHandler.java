package CEF.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class InputHandler extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(InputHandler.class);
	
	public final int[] keys;
	
	public InputHandler(int[] keys)
	{
		this.keys = keys;
	}
	
}
