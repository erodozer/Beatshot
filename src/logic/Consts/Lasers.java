package logic.Consts;

import com.badlogic.gdx.Input.Keys;

public enum Lasers
{
	Laser1(Keys.A),
	Laser2(Keys.S),
	Laser3(Keys.SPACE),
	Laser4(Keys.D),
	Laser5(Keys.F);
	
	public final int key;
	
	private Lasers(final int key)
	{
		this.key = key;
	}
	
	public static Lasers valueOf(int key)
	{
		for (Lasers l : Lasers.values())
		{
			if (l.key == key)
				return l;
		}
		return null;
	}
}