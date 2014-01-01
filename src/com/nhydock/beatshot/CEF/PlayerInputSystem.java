package com.nhydock.beatshot.CEF;


import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.artemis.components.*;
import com.nhydock.beatshot.CEF.Components.Ammo;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Emitter;
import com.nhydock.beatshot.CEF.Groups.Player;
import com.nhydock.beatshot.core.Consts.PlayerInput;
import com.nhydock.beatshot.logic.Engine;

public class PlayerInputSystem extends VoidEntitySystem {

	public static final int LOWAMMOPOINT = 40;
	public static final int LOWHPPOINT = 30;
	
	private static final float DRAINRATE = 1.0f;
	private static final float CHARGERATE = 30.0f;
	private static final float HPCHARGERATE = 1.0f;

	int firing;
	
	@Override
	protected void processSystem() {
		Velocity vel = Engine.player.getComponent(Velocity.class);
		Emitter emit = Engine.player.getComponent(Emitter.class);
		
		//move left
		if (PlayerInput.LEFT.isPressed())
		{
			vel.x = -100f;
		}
		else if (PlayerInput.RIGHT.isPressed())
		{
			vel.x = 100f;
		}
		else
		{
			vel.x = 0f;
		}
		
		firing = 0;
		for (int i = 0; i < PlayerInput.Lasers.length; i++)
		{
			PlayerInput p = PlayerInput.Lasers[i];
			if (p.isPressed())
			{
				emit.enable(i);
				firing++;
			}
			else
			{
				emit.disable(i);
			}
		}
			
		Ammo a = Engine.player.getComponent(Ammo.class);
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
		}
		

		Health h = Engine.player.getComponent(Health.class);
		h.hp = Math.min(h.hp+(HPCHARGERATE*world.delta), h.maxhp);
	}

}
