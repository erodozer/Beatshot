package EntitySystems;

import logic.Consts.Lasers;

import EntitySystems.Components.Ammo;
import EntitySystems.Components.Bound;
import EntitySystems.Components.InputHandler;
import EntitySystems.Components.Position;
import EntitySystems.Components.Time;
import EntitySystems.Components.Velocity;
import EntitySystems.Components.Group.Emitter;
import EntitySystems.Components.Group.Player;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputSystem extends EntityProcessingSystem implements InputProcessor {

	public static final int LOWAMMOPOINT = 40;
	public static final int LOWHPPOINT = 30;
	
	private static final float DRAINRATE = 1.0f;
	private static final float CHARGERATE = 20.0f;

	boolean[] shoot;
	int firing;
	
	boolean left, right;
	
    @Mapper ComponentMapper<Position> pm;
    @Mapper ComponentMapper<Velocity> vm;
    @Mapper ComponentMapper<Ammo> am;
    @Mapper ComponentMapper<Emitter> em;
    @Mapper ComponentMapper<InputHandler> im;
    @Mapper ComponentMapper<Time> tm;
    
	public InputSystem() {
		super(Aspect.getAspectForAll(InputHandler.class, Player.class));
		
		shoot = new boolean[Lasers.values().length];	
	}
	
	@Override
	protected void process(Entity e) {
		Velocity vel = vm.get(e);
		Emitter emit = em.get(e);
		
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
			
			Ammo a = am.get(e);
			if (firing == 0 || a.recharge)
			{
				a.ammo = Math.min(a.ammo+(CHARGERATE*world.delta), a.maxammo);
				if (a.ammo > a.maxammo/2.0f)
				{
					a.recharge = false;
				}
			}
		}
		else
		{
			for (int i = 0; i < shoot.length; i++)
			{
				if (Lasers.values()[i].keys == im.get(e).keys)
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
		if (p.location.x <= 0)
		{
			left = false;
			p.location.x = 0;
		}
		if (p.location.x >= 176)
		{
			right = false;
			p.location.x = 176;
		}
	}

	@Override
	public boolean keyDown(int key) {
		if (key == Input.Keys.LEFT)
		{
			left = true;
			return true;
		}
		else if (key == Input.Keys.RIGHT)
		{
			right = true;
			return true;
		}
		
		Lasers l = Lasers.valueOf(key);
		if (Lasers.valueOf(key) != null)
		{
			shoot[l.ordinal()] = true;
			firing++;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int key) {
		if (key == Input.Keys.LEFT)
		{
			left = false;
			return true;
		}
		else if (key == Input.Keys.RIGHT)
		{
			right = false;
			return true;
		}
		
		Lasers l = Lasers.valueOf(key);
		if (Lasers.valueOf(key) != null)
		{
			shoot[l.ordinal()] = false;
			firing--;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
