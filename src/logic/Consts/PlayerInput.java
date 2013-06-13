package logic.Consts;

import com.badlogic.gdx.Input.Keys;

public enum PlayerInput
{
	ONE(Keys.A, Keys.BUTTON_L1), 
	TWO(Keys.S, Keys.BUTTON_B), 
	THREE(Keys.SPACE, Keys.BUTTON_A), 
	FOUR(Keys.D, Keys.BUTTON_C), 
	FIVE(Keys.F, Keys.BUTTON_R1),
	LEFT(Keys.LEFT, Keys.DPAD_LEFT, Keys.DPAD_UP),
	RIGHT(Keys.RIGHT, Keys.DPAD_RIGHT, Keys.DPAD_DOWN);
	
	public static final int Lasers = 5;
	
	public final int[] keys;
	
	private PlayerInput(int... keys)
	{
		this.keys = keys;
	}
	
	public static PlayerInput valueOf(int key)
	{
		for (PlayerInput l : PlayerInput.values())
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