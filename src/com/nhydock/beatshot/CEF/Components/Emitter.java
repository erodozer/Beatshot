package com.nhydock.beatshot.CEF.Components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BooleanArray;
import com.badlogic.gdx.utils.FloatArray;
import com.nhydock.beatshot.logic.Bullet.BulletData;

public class Emitter extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Emitter.class);
	
	public final String TYPE = "Emitter";
	
	/**
	 * Entity link for bullet typing
	 */
	public Entity parent;

	/**
	 * Says if the emitter should be firing
	 */
	Array<BulletData> emitterData;
	BooleanArray active;
	
	//built in timer for determining when
	FloatArray timers;
	
	private Array<BulletData> fire;
	
	/**
	 * Create an emitter
	 * @param parent - actor to link the emitter to
	 */
	public Emitter()
	{
		emitterData = new Array<BulletData>();
		timers = new FloatArray();
		active = new BooleanArray();
		fire = new Array<BulletData>();
	}
	
	public void register(BulletData d)
	{
		emitterData.add(d);
		active.add(true);
		timers.add(0f);
	}
	
	public void disable(int i)
	{
		active.set(i, false);
		timers.set(i, 0f);
	}
	
	public void enable(int i)
	{
		active.set(i, true);
		timers.set(i, emitterData.get(i).getSpawnRate());
	}

	public Array<BulletData> update(float delta) {
		fire.clear();
		
		for (int i = 0; i < active.size; i++)
		{
			if (active.get(i))
			{
				BulletData data = emitterData.get(i);
				float timer = timers.get(i);
				timer += delta;
				if (timer > data.getSpawnRate())
				{
					fire.add(data);
					timer = 0f;
				}
				timers.set(i, timer);
			}
		}
		
		return fire;
	}
}
