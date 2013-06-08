package EntitySystems;

import logic.Consts.Lasers;

import EntitySystems.Components.AmmoComponent;
import EntitySystems.Components.Bound;
import EntitySystems.Components.InputHandler;
import EntitySystems.Components.Position;
import EntitySystems.Components.Velocity;
import EntitySystems.GroupComponents.Emitter;
import EntitySystems.GroupComponents.Player;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputSystem extends EntityProcessingSystem implements InputProcessor {

	public static final int LOWAMMOPOINT = 40;
	public static final int LOWHPPOINT = 30;
	
	private static final float DRAINRATE = 1.0f;
	private static final float CHARGERATE = 20.0f;

	boolean[] shoot;
	
	boolean left, right;
	
    @Mapper ComponentMapper<Position> pm;
    @Mapper ComponentMapper<Velocity> vm;
    @Mapper ComponentMapper<AmmoComponent> am;
    
	public InputSystem() {
		super(Aspect.getAspectForAll(Player.class, InputHandler.class));
		
		shoot = new boolean[Lasers.values().length];
		
	}

	@Override
	protected void process(Entity e) {
		Position pos = pm.get(e);
		Velocity vel = vm.get(e);
		
		if (left)
		{
			vel.x = -30.0f;
		}
		else if (right)
		{
			vel.x = 30.0f;
		}
		else
		{
			vel.x = 0;
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
			shoot[l.ordinal()] = false;
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
