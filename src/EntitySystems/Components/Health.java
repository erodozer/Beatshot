package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Health extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Health.class);
	
	public int hp;
	public final int maxhp;
	
	//have value caching for percent calculation
	private float perc;
	private int tempHp;
	
	public Health(int maxhp)
	{
		this.hp = this.maxhp = maxhp;
		this.tempHp = this.hp;
		this.perc = 1.0f;
	}
	
	public float getPercent()
	{
		if (this.hp != this.tempHp)
		{
			this.tempHp = this.hp;
			this.perc = this.tempHp/(float)this.maxhp;
		}
		return this.perc;
	}
	
	private float LOW = .35f;
	
	public boolean isLow()
	{
		return this.getPercent() < LOW;
	}
}
