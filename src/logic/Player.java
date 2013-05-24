package logic;

import scenes.Main.PlayerNotification;

import com.shipvgdc.sugdk.util.Observable;

public class Player extends Observable<PlayerNotification> {

	public static final int MAXHP = 100;
	public static final int MAXAMMO = 100;
	public static final int LOWAMMOPOINT = 40;
	public static final int LOWHPPOINT = 30;
	
	private static final float DRAINRATE = 1.0f;
	private static final float CHARGERATE = 20.0f;
	
	int firingLasers;
	boolean[] lasers;
	
	float ammo;
	float hp;
	
	boolean ammoLow;
	boolean hpLow;
	
	private static Player instance;
	
	public static Player getPlayer()
	{
		if (instance == null)
			instance = new Player();
		return instance;
	}
	
	private Player()
	{
		lasers = new boolean[Consts.Lasers.values().length];
		firingLasers = 0;
	}
	
	public void fire(int laser)
	{
		if (!lasers[laser])
		{
			lasers[laser] = true;
			firingLasers++;
		}
	}
	
	public void update(float delta)
	{
		if (firingLasers > 0)
		{
			ammo = (float)Math.max(0, ammo-DRAINRATE*firingLasers*delta);
		}
		else
		{
			ammo = (float)Math.min(MAXAMMO, ammo+DRAINRATE*delta);
		}
		
		if (ammo < LOWAMMOPOINT && !ammoLow)
		{
			ammoLow = true;
			this.notify(PlayerNotification.AmmoLow, true);
		}
		else if (ammo > LOWAMMOPOINT)
		{
			ammoLow = false;
			this.notify(PlayerNotification.AmmoLow, false);
		}
		if (hp < LOWHPPOINT && !hpLow)
		{
			hpLow = true;
			this.notify(PlayerNotification.HPLow, true);
		}
		else if (hp > LOWHPPOINT)
		{
			hpLow = false;
			this.notify(PlayerNotification.HPLow, false);
		}
	}
}
