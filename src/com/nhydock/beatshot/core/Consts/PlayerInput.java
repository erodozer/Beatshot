package com.nhydock.beatshot.core.Consts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public enum PlayerInput
{
	ONE(Keys.A), 
	TWO(Keys.S), 
	THREE(Keys.SPACE), 
	FOUR(Keys.D), 
	FIVE(Keys.F),
	LEFT(Keys.LEFT),
	RIGHT(Keys.RIGHT);
	
	public static final PlayerInput[] Lasers = {ONE, TWO, THREE, FOUR, FIVE};
	
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
	
	public boolean isPressed()
	{
		for (int key : this.keys)
		{
			if (Gdx.input.isKeyPressed(key))
				return true;
		}
		return false;
	}
}