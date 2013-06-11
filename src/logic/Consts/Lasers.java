package logic.Consts;

import com.badlogic.gdx.Input.Keys;

public enum Lasers
{
	ONE(Keys.A, Keys.BUTTON_L1), 
	TWO(Keys.S, Keys.BUTTON_B), 
	THREE(Keys.SPACE, Keys.BUTTON_A), 
	FOUR(Keys.D, Keys.BUTTON_C), 
	FIVE(Keys.F, Keys.BUTTON_R1);
	
	public final int[] keys;
	
	private Lasers(int... keys)
	{
		this.keys = keys;
	}
	
	public static Lasers valueOf(int key)
	{
		for (Lasers l : Lasers.values())
		{
			for (int k : l.keys)
			{
				if (k == key)
				{
					return l;
				}
			}
		}
		return null;
	}
}