package EntitySystems.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Ammo extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Ammo.class);
	
	public Ammo(int ammo) {
		this.ammo = this.maxammo = ammo;
	}

	public float ammo;
	public final float maxammo;
	
	public boolean recharge;
	
	//cache values for percentage calculation
	private float perc;
	private float tempAmmo;
	
	public float getPercent()
	{
		if (this.ammo != this.tempAmmo)
		{
			this.tempAmmo = this.ammo;
			this.perc = this.tempAmmo/this.maxammo;
		}
		return this.perc;
	}
	
	private float LOW = .25f;
	
	public boolean isLow()
	{
		return this.getPercent() < LOW;
	}
}
