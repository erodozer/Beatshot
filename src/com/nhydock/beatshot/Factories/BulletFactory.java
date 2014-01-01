package com.nhydock.beatshot.Factories;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.artemis.components.Angle;
import com.badlogic.gdx.artemis.components.Bound;
import com.badlogic.gdx.artemis.components.Position;
import com.badlogic.gdx.artemis.components.Renderable;
import com.badlogic.gdx.artemis.components.Velocity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.nhydock.beatshot.CEF.Groups.Bullet;
import com.nhydock.beatshot.CEF.Groups.Player;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.logic.Bullet.*;
import com.nhydock.beatshot.util.SpriteSheet;

/**
 * Generate bullet objects
 * @author nhydock
 *
 */
public class BulletFactory {
	private static SpriteSheet bulletSprites;
	/*
	 * bullet data
	 */
	
	private static final PolygonShape shape;
	
	static
	{
		bulletSprites = new SpriteSheet(Gdx.files.internal(DataDir.Images + "bullets.png"), 2, 2);
		shape = new PolygonShape();
		shape.setAsBox(1.0f, 1.0f);
	}
	
	/**
	 * Creates a point along a path and fires it
	 * @param p
	 * @param speed
	 * @param t
	 * @param w
	 */
	public static Entity createBullet(Vector2 pos, BulletData bulletData, World w, String group)
	{
		Entity e = w.createEntity();	
		
		if (bulletData instanceof PathBullet)
			return createBullet(pos, (PathBullet)bulletData, w);
		else if (bulletData instanceof VelocityBullet)
		{	
			VelocityBullet data = (VelocityBullet)bulletData;
			Velocity v = new Velocity(data.velocity.angle(), 10f);
			Position p = new Position(pos.x, pos.y);
			Angle a = new Angle(data.velocity.angle());
			
			e.addComponent(v, Velocity.CType);
			e.addComponent(p, Position.CType);
			e.addComponent(a, Angle.CType);
			e.addComponent(new Bound(1.0f, 1.0f), Bound.CType);
			
			Sprite s;
			if (group == Player.TYPE)
			{
				s = new Sprite(bulletSprites.getFrame(1, 0));
			}
			else
			{
				s = new Sprite(bulletSprites.getFrame(0, 0));
			}
			s.setPosition(p.location.x+p.offset.x, p.location.y+p.offset.y);
			e.addComponent(new Renderable(s), Renderable.CType);
			
			return e;
		}
	}
	
	public static Entity createBullet(Vector2 pos, PathBullet bulletData, World w)
	{
		Entity e = w.createEntity();
		return e;
	}
}
