package com.nhydock.beatshot.Factories;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.artemis.components.Bound;
import com.badlogic.gdx.artemis.components.Path;
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
	 * @param world
	 */
	public static Entity createBullet(World world, Vector2 pos, BulletData bulletData, String group)
	{
		Entity e = world.createEntity();	
		Bound b = new Bound(3.0f, 3.0f);
		e.addComponent(b, Bound.CType);
		
		if (bulletData instanceof PathBullet)
		{
			PathBullet data = (PathBullet)bulletData;
			e.addComponent(new Path(data.path, data.duration, 0f, Path.Loop.None));
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
			e.addComponent(new Renderable(s), Renderable.CType);
		}
		else if (bulletData instanceof VelocityBullet)
		{	
			VelocityBullet data = (VelocityBullet)bulletData;
			Velocity v = new Velocity((int)data.velocity.angle(), 200f);
			e.addComponent(v, Velocity.CType);
			
			Sprite s;
			if (group == Player.TYPE)
			{
				s = new Sprite(bulletSprites.getFrame(1, 0));
			}
			else
			{
				s = new Sprite(bulletSprites.getFrame(0, 0));
			}
			Position p = new Position(pos.x, pos.y, -s.getWidth()/2f, -s.getHeight()/2f);
			e.addComponent(p, Position.CType);
			
			s.setPosition(p.location.x+p.offset.x, p.location.y+p.offset.y);
			e.addComponent(new Renderable(s), Renderable.CType);
		}
		
		world.getManager(GroupManager.class).add(e, Bullet.TYPE);
		world.getManager(GroupManager.class).add(e, group);
		
		e.addToWorld();
		
		return e;
		
	}
}
