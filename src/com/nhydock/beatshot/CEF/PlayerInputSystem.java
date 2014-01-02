package com.nhydock.beatshot.CEF;


import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.artemis.components.*;
import com.nhydock.beatshot.CEF.Components.Ammo;
import com.nhydock.beatshot.CEF.Components.Emitter;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.core.Consts.PlayerInput;

public class PlayerInputSystem extends VoidEntitySystem implements InputProcessor{

	public static final int LOWAMMOPOINT = 40;
	public static final int LOWHPPOINT = 30;
	
	private static final float DRAINRATE = 5.0f;
	private static final float CHARGERATE = 30.0f;
	private static final float HPCHARGERATE = 1.0f;

	int firing;
	
	int move;
	
	@Override
	protected void processSystem() {
		Emitter emit = BeatshotGame.player.getComponent(Emitter.class);
		Velocity vel = BeatshotGame.player.getComponent(Velocity.class);
		
		//move left
		if (move == -1)
		{
			vel.x = -100f;
		}
		else if (move == 1)
		{
			vel.x = 100f;
		}
		else
		{
			vel.x = 0f;
		}
		
		Ammo a = BeatshotGame.player.getComponent(Ammo.class);
		if (a.recharge){
			for (int i = 0; i < PlayerInput.Lasers.length; i++)
			{
				emit.disable(i);
			}
		}
			
		if (firing == 0 || a.recharge)
		{
			a.ammo = Math.min(a.ammo+(CHARGERATE*world.delta), a.maxammo);
			if (a.ammo > a.maxammo/2.0f)
			{
				a.recharge = false;
			}
		}
		else
		{
			a.ammo = Math.max(a.ammo - (firing * DRAINRATE * world.delta), 0);
			if (a.ammo <= 0)
			{
				a.recharge = true;
			}
		}
		

		Health h = BeatshotGame.player.getComponent(Health.class);
		h.hp = Math.min(h.hp+(HPCHARGERATE*world.delta), h.maxhp);
	}

	@Override
	public boolean keyDown(int key) {
		if (PlayerInput.valueOf(key) == PlayerInput.LEFT)
		{
			move = -1;
		}
		else if (PlayerInput.valueOf(key) == PlayerInput.RIGHT)
		{
			move = 1;
		}
		
		Emitter emit = BeatshotGame.player.getComponent(Emitter.class);
		for (int i = 0; i < PlayerInput.Lasers.length; i++)
		{
			PlayerInput p = PlayerInput.Lasers[i];
			if (PlayerInput.valueOf(key) == p)
			{
				emit.enable(i);
				firing++;
			}
		}
		return false;
	}


	@Override
	public boolean keyUp(int key) {
		if (move == -1) {
			if (PlayerInput.valueOf(key) == PlayerInput.LEFT)
			{
				move = 0;
			}
		}
		else if (move == 1){
			if (PlayerInput.valueOf(key) == PlayerInput.RIGHT)
			{
				move = 0;
			}
		}
		
		Emitter emit = BeatshotGame.player.getComponent(Emitter.class);
		for (int i = 0; i < PlayerInput.Lasers.length; i++)
		{
			PlayerInput p = PlayerInput.Lasers[i];
			if (PlayerInput.valueOf(key) == p)
			{
				emit.disable(i);
				firing--;
			}
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) { return false; }

	@Override
	public boolean mouseMoved(int arg0, int arg1) { return false; }

	@Override
	public boolean scrolled(int arg0) { return false; }

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) { return false; }

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) { return false; }

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) { return false; }

}
