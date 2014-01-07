package com.nhydock.beatshot.Factories;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.nhydock.beatshot.CEF.Components.Ammo;
import com.nhydock.beatshot.CEF.Components.Emitter;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.CEF.Groups.Player;
import com.badlogic.gdx.artemis.components.*;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.core.Consts.PlayerInput;
import com.nhydock.beatshot.logic.Bullet.BulletData;
import com.nhydock.beatshot.logic.Bullet.VelocityBullet;
import com.nhydock.beatshot.util.SpriteSheet;

/**
 * 
 * @author nhydock
 *
 */
public class PlayerFactory {

	public static final int MAXHP = 100;
	public static final int MAXAMMO = 100;
	
	private static SpriteSheet sprite;
	private static Texture shadow_sprite;
	
	static
	{
		sprite = new SpriteSheet(Gdx.files.internal(DataDir.Images + "player.png"), 4, 1);
		shadow_sprite = new Texture(Gdx.files.internal(DataDir.Images + "player_shadow.png"));
	}

	/**
	 * Formats an entity into a player
	 * @param e - entity to convert
	 * @return a formatted player entity
	 */
	public static Entity createEntity(World w) {
		Entity e = w.createEntity();
		
		e.addComponent(new Health(MAXHP), Health.CType);
		e.addComponent(new Ammo(MAXAMMO), Ammo.CType);
		
		e.addComponent(new Position(0, sprite.getFrameHeight()/2f, -sprite.getFrameWidth()/2f, -sprite.getFrameHeight()/2f), Position.CType);
		e.addComponent(new Velocity(0, 0), Velocity.CType);
		
		e.addComponent(new Bound(10f, 10f), Bound.CType);
		
		e.addComponent(new Renderable(sprite.getFrame(0)), Renderable.CType);
		e.addComponent(new Animation(sprite.getTexture(), sprite.frameCount, .1667f, true), Animation.CType);
		
		w.getManager(TagManager.class).register("Player", e);
		w.getManager(GroupManager.class).add(e, Player.TYPE);
		
		Emitter emitter = new Emitter();
		for (int i = 0, angle = 140; i < PlayerInput.Lasers.length; i++, angle -= 25)
		{
			Vector2 v = new Vector2(1, 1);
			v.setAngle(angle);
			
			BulletData laser = new VelocityBullet(v, .25f);
			emitter.register(laser);
			emitter.disable(i);
		}
		e.addComponent(emitter);
		
		e.addToWorld();
		
		Entity shadow = e.getWorld().createEntity();
		shadow.addComponent(new Position(0,0,0,-2), Position.CType);
		shadow.addComponent(new Anchor(e), Anchor.CType);
		shadow.addComponent(new Renderable(new Sprite(shadow_sprite)), Renderable.CType);
		e.getWorld().getManager(TagManager.class).register("PlayerShadow", shadow);
		w.getManager(GroupManager.class).add(e, Player.TYPE);
		shadow.addToWorld();
		
		return e;
	}

}
