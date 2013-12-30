package com.nhydock.beatshot.Factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.artemis.components.Animation;
import com.badlogic.gdx.artemis.components.Position;
import com.badlogic.gdx.artemis.components.Renderable;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.logic.Engine;
import com.nhydock.beatshot.util.SpriteSheet;

/**
 * Factory that generates explosions on hit
 * @author nhydock
 *
 */
public class ExplosionFactory {

	private static Array<SpriteSheet> explosions;
	
	/**
	 * Set up the factory's image resources to be managed
	 */
	public static void loadAssets()
	{
		TextureAtlas ta = new TextureAtlas(Gdx.files.internal(DataDir.Images + "explosions.atlas"));
		
		explosions = new Array<SpriteSheet>();
		for (AtlasRegion t : ta.getRegions())
		{
			SpriteSheet s = new SpriteSheet(t, 4, 4);
			System.out.println(s.getTexture().getRegionWidth());
			System.out.println(s.getFrame(0).getRegionWidth());
			
			explosions.add(s);
		}
	}
	
	public static Entity create(World w, Vector2 location)
	{
		Entity e = w.createEntity();
		
		SpriteSheet s = explosions.get(MathUtils.random(0, explosions.size-1));
		Renderable r = new Renderable(new Sprite(s.getFrame(0)));
		Vector2 offset = new Vector2(-r.sprite.getWidth()/2, -r.sprite.getHeight()/2);
		r.sprite.setPosition(location.x + offset.x, location.y + offset.y);
		Animation a = new Animation(s.getTexture(), s.xFrames, s.yFrames, 1f/(s.xFrames*s.yFrames), false);
		
		e.addComponent(r, Renderable.CType);
		e.addComponent(a, Animation.CType);
		
		return e;
	}
	
}
