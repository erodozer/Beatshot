package com.nhydock.beatshot.CEF;


import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.artemis.components.*;
import com.nhydock.beatshot.CEF.Components.Ammo;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Emitter;
import com.nhydock.beatshot.CEF.Groups.Player;
import com.nhydock.beatshot.core.Consts.PlayerInput;

public class PlayerInputSystem extends com.badlogic.gdx.artemis.systems.InputSystem {

	public static final int LOWAMMOPOINT = 40;
	public static final int LOWHPPOINT = 30;
	
	private static final float DRAINRATE = 1.0f;
	private static final float CHARGERATE = 30.0f;
	private static final float HPCHARGERATE = 1.0f;

	boolean[] shoot;
	int firing;
	
	boolean left, right;
	
    @Mapper ComponentMapper<Position> pm;
    @Mapper ComponentMapper<Velocity> vm;
    @Mapper ComponentMapper<Ammo> am;
    @Mapper ComponentMapper<Health> hm;
    
    @Mapper ComponentMapper<Emitter> em;
    @Mapper ComponentMapper<Time> tm;
    
	public PlayerInputSystem() {
		super(Aspect.getAspectForAll(Player.class));
		
		shoot = new boolean[PlayerInput.Lasers];	
	}
	
	@Override
	protected void process(Entity e) {
		Velocity vel = vm.get(e);
		Emitter emit = em.getSafe(e);
		
		if (emit == null)
		{
			if (left)
			{
				vel.x = -100.0f;
			}
			else if (right)
			{
				vel.x = 100.0f;
			}
			else
			{
				vel.x = 0;
			}
			
			Ammo a = am.getSafe(e);
			if (firing == 0 || a.recharge)
			{
				a.ammo = Math.min(a.ammo+(CHARGERATE*world.delta), a.maxammo);
				if (a.ammo > a.maxammo/2.0f)
				{
					a.recharge = false;
				}
			}
			
			Health h = hm.get(e);
			h.hp = Math.min(h.hp+(HPCHARGERATE*world.delta), h.maxhp);
		}
		else
		{
			for (int i = 0; i < shoot.length; i++)
			{
				if (PlayerInput.values()[i].keys == inputMap.get(e).keys)
				{
					if (shoot[i])
					{
						emit.active = true;
					}
					else
					{
						emit.active = false;
						Time t = tm.get(e);
						t.curr = 0;
					}
					break;	
				}
			}
		}
		
		Position p = pm.get(e);
		if (p.location.x < 0)
		{
			left = false;
			p.location.x = 0;
		}
		if (p.location.x > 176)
		{
			right = false;
			p.location.x = 176;
		}
	}

	@Override
	public boolean keyDown(int key) {
		PlayerInput l = PlayerInput.valueOf(key);
		if (l == null)
			return false;
		
		if (l == PlayerInput.LEFT)
		{
			left = true;
		}
		
		if (l == PlayerInput.RIGHT)
		{
			right = true;
		}
		
		//laser input
		if (l.ordinal() < PlayerInput.Lasers)
		{
			shoot[l.ordinal()] = true;
			firing++;
		}
		return true;
	}

	@Override
	public boolean keyUp(int key) {
		PlayerInput l = PlayerInput.valueOf(key);
		if (l == null)
			return false;
		
		if (l == PlayerInput.LEFT)
		{
			left = false;
		}
		
		if (l == PlayerInput.RIGHT)
		{
			right = false;
		}
		
		//laser input
		if (l.ordinal() < PlayerInput.Lasers)
		{
			shoot[l.ordinal()] = false;
			firing--;
		}
		return true;
	}

}
