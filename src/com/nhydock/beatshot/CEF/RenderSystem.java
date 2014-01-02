package com.nhydock.beatshot.CEF;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nhydock.beatshot.CEF.Components.Ammo;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Enemy;
import com.nhydock.beatshot.CEF.Groups.Player;
import com.nhydock.beatshot.CEF.Groups.Bullet;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.util.Tools;
import com.badlogic.gdx.artemis.components.*;

/**
 * System that renders the field
 * @author nhydock
 */
public class RenderSystem extends com.badlogic.gdx.artemis.systems.RenderSystem2D {

	static public float[] InternalRes = {240f, 320f};
	static public final float[] FOV = {25, 75, 190, 220};

	
	@Mapper ComponentMapper<Enemy> enemymap;

	public boolean warning;
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}
	
	public void draw(SpriteBatch batch)
	{
		process();
		
		//temp variable name holders
		Renderable r;
		Entity e;
		
		GroupManager gm = this.world.getManager(GroupManager.class);
		TagManager tm = this.world.getManager(TagManager.class);
		
		ImmutableBag<Entity> bag;
		
		//we group the different parts to maximize efficiency
		
		batch.setProjectionMatrix(camera);
		bag = this.world.getManager(GroupManager.class).getEntities("Field");
		batch.begin();
		{
			//render the field
			for (int i = 0; i < bag.size(); i++)
			{
				e = bag.get(i);
				r = rmap.get(e);
				r.sprite.draw(batch);
			}
		}
		batch.end();
		
		batch.begin();
		{
			//draw warning banners
			//Display banners;
			Ammo a = (Ammo)BeatshotGame.player.getComponent(Ammo.CType);
			Health h = (Health)BeatshotGame.player.getComponent(Health.CType);
			bag = gm.getEntities("Banner");
			ImmutableBag<Entity> bannerBag;
			Bag<Entity> bannerBag2;
			
			if (h.isLow())
			{
				bannerBag = gm.getEntities("BannerA");
				bannerBag2 = (Bag<Entity>)gm.getEntities("BannerB");
			}
			else if (a.isLow())
			{
				bannerBag = gm.getEntities("BannerB");
				bannerBag2 = (Bag<Entity>)gm.getEntities("BannerA");
			}
			else
			{
				bannerBag = gm.getEntities("BannerC");
				bannerBag2 = new Bag<Entity>();
				bannerBag2.addAll( (Bag<Entity>)gm.getEntities("BannerA") );
				bannerBag2.addAll( (Bag<Entity>)gm.getEntities("BannerB") );
			}
			
			for (int i = bag.size()-1; i >= 0; i--)
			{
				e = bag.get(i);
				Position p = pmap.getSafe(e);
				r = rmap.get(e);
				
				if (bannerBag.contains(e))
				{
					//right side banner
					if (p.offset.x < 0)
					{
						p.location.x = Math.max(190, p.location.x - 32 * world.delta);
					}
					else
					{
						p.location.x = Math.min(0, p.location.x + 32 * world.delta);
					}
				}
				else if (bannerBag2.contains(e))
				{
					//right side banner
					if (p.offset.x < 0)
					{
						p.location.x = Math.min(190-p.offset.x, p.location.x + 32 * world.delta);
					}
					else
					{
						p.location.x = Math.max(-12, p.location.x - 32 * world.delta);
					}
				}
				
				r.sprite.draw(batch);
			}
		}
		batch.end();
		
		//render the player
		batch.begin();
		{
			e = tm.getEntity("PlayerShadow");
			r = rmap.get(e);
			r.sprite.draw(batch);
			e = BeatshotGame.player;
			r = rmap.get(e);
			r.sprite.draw(batch);
		}
		batch.end();

		bag = gm.getEntities(Bullet.TYPE);
		batch.begin();
		{
			//render enemy bullets
			for (int i = 0; i < bag.size(); i++)
			{
				e = bag.get(i);
				r = rmap.get(e);
				r.sprite.draw(batch);
			}
		}
		batch.end();
		
		bag = gm.getExclusiveEntities(Enemy.TYPE);
		batch.begin();
		{
			//render enemies
			for (int i = 0; i < bag.size(); i++)
			{
				e = bag.get(i);
				r = rmap.get(e);
				r.sprite.draw(batch);
			}
		}
		batch.end();
		
		bag = gm.getEntities("effects");
		batch.begin();
		{
			//render explosions
			for (int i = 0; i < bag.size(); i++)
			{
				e = bag.get(i);
				r = rmap.get(e);
				r.sprite.draw(batch);
				Animation a = (Animation)e.getComponent(Animation.CType);
				if (a.done)
					e.deleteFromWorld();
			}
		}
		batch.end();
		
		batch.begin();
		//draw game over banner
		if (BeatshotGame.GameOver)
		{
			r = (Renderable)tm.getEntity("GameOver").getComponent(Renderable.CType);
			r.sprite.draw(batch);
		}
		else if (warning)
		{
			r = (Renderable)tm.getEntity("Warning").getComponent(Renderable.CType);
			r.sprite.draw(batch);
		}
		batch.end();
	}

	@Override
	protected float[] getFOV() {
		return FOV;
	}

	@Override
	protected float[] getInternalRes() {
		return InternalRes;
	}
	
}
