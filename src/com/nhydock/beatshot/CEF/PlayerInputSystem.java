package com.nhydock.beatshot.CEF;


import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.artemis.components.*;
import com.nhydock.beatshot.CEF.Components.Ammo;
import com.nhydock.beatshot.CEF.Components.Emitter;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.core.Consts.PlayerInput;
import static com.nhydock.beatshot.CEF.RenderSystem.FOV;

public class PlayerInputSystem extends VoidEntitySystem implements InputProcessor{

	public static final int LOWAMMOPOINT = 40;
	public static final int LOWHPPOINT = 30;
	
	private static final float DRAINRATE = 5.0f;
	private static final float CHARGERATE = 30.0f;
	private static final float HPCHARGERATE = 1.0f;

	private int firing;
	private boolean[] lasers = new boolean[PlayerInput.Lasers.length];
	
	private int move;
	
	@Override
	protected void processSystem() {
		Emitter emit = BeatshotGame.player.getComponent(Emitter.class);
		Velocity vel = BeatshotGame.player.getComponent(Velocity.class);
		
		//move left
		if (move == -1)
		{
			Position pos = BeatshotGame.player.getComponent(Position.class);
			if (pos.location.x + pos.offset.x > 0)
				vel.x = -100f;
			else
				move = 0;
		}
		else if (move == 1)
		{
			Position pos = BeatshotGame.player.getComponent(Position.class);
			if (pos.location.x - pos.offset.x < FOV[2])
				vel.x = 100f;
			else
				move = 0;
		}
		else
		{
			vel.x = 0f;
		}
		
		Ammo a = BeatshotGame.player.getComponent(Ammo.class);
		
			
		if (firing == 0 || a.recharge)
		{
			a.ammo = Math.min(a.ammo+(CHARGERATE*world.delta), a.maxammo);
			if (a.ammo > a.maxammo/2.0f)
			{
				a.recharge = false;
				for (int i = 0; i < PlayerInput.Lasers.length; i++)
				{
					if (lasers[i]){
						emit.enable(i);
						firing++;
					}
				}
			}
		}
		else
		{
			a.ammo = Math.max(a.ammo - (firing * DRAINRATE * world.delta), 0);
			if (a.ammo <= 0)
			{
				a.recharge = true;
				for (int i = 0; i < PlayerInput.Lasers.length; i++)
				{
					emit.disable(i);
					firing = 0;
				}
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
		
		Ammo a = BeatshotGame.player.getComponent(Ammo.class);
		
		Emitter emit = BeatshotGame.player.getComponent(Emitter.class);
		for (int i = 0; i < PlayerInput.Lasers.length; i++)
		{
			PlayerInput p = PlayerInput.Lasers[i];
			if (PlayerInput.valueOf(key) == p)
			{
				lasers[i] = true;
				if (!a.recharge){
					emit.enable(i);
					a.ammo -= DRAINRATE;
					firing++;
				}
				break;
			}
		}
		if (a.ammo <= 0) {
			a.recharge = true;
			firing = 0;
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
				lasers[i] = false;
				firing = Math.max(0, firing - 1);
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
