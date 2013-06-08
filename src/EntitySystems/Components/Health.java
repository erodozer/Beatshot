package EntitySystems.Components;

import com.artemis.Component;

public class Health extends Component {
	public int hp, maxhp;
	
	public Health(int maxhp)
	{
		this.hp = this.maxhp = maxhp;
	}
}
