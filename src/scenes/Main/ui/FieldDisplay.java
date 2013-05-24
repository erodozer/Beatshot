package scenes.Main.ui;

import scenes.Main.PlayerNotification;
import logic.Consts.DataDir;
import logic.Engine;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.shipvgdc.sugdk.graphics.Animation;
import com.shipvgdc.sugdk.graphics.SpriteSheet;
import com.shipvgdc.sugdk.tween.Tweens;
import com.shipvgdc.sugdk.util.Observer;

public class FieldDisplay{

	Player player;
	Sprite map;
	Sprite banners;
	TweenManager tm;
	public KeyListener inputListener;
	
	Timeline scroll;
	
	public static void loadAssets()
	{
		Engine.assets.load(DataDir.Images + "player.png", Texture.class);
		Engine.assets.load(DataDir.Images + "player_shadow.png", Texture.class);
		Engine.assets.load(DataDir.Levels + "field001.png", Texture.class);
		Engine.assets.load(DataDir.Ui + "banners.png", Texture.class);
		
	}
	
	public FieldDisplay()
	{
		
		Texture t = Engine.assets.get(DataDir.Levels + "field001.png", Texture.class);
		t.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		map = new Sprite(t);
		
		player = new Player();
		
		tm = new TweenManager();
		
		scroll = Timeline.createSequence()
			.push(Tween.set(map, Tweens.V).target(1.0f, 2.0f))
			.push(Tween.to(map, Tweens.V, 5.0f).target(0.0f, 1.0f).ease(TweenEquations.easeNone))
			.repeat(-1, 0f)
		.start(tm);
		
		banners = new Banners(tm);
		
		inputListener = new KeyListener(player);
	}
	
	public void act(float delta)
	{
		tm.update(delta);
		player.act(delta);
	}
	
	public void setPosition(int x, int y)
	{
		player.setPosition(x+95, y);
		banners.setPosition(x, y);
		map.setPosition(x, y);
	}
	
	public void draw(SpriteBatch batch)
	{
		map.draw(batch);
		player.draw(batch);
		banners.draw(batch);
	}
	
	private class Player extends Sprite
	{
		private Animation player;
		private Texture playerShadow;
		
		int moving;
		
		int bound = 72;
		
		public Player()
		{
			super();
			SpriteSheet pTex = new SpriteSheet(Engine.assets.get(DataDir.Images + "player.png", Texture.class), 4, 1);
			player = new Animation(20, pTex.getRow(0));
			player.loop();
			player.setOriginX(player.getWidth()/2f);
			playerShadow = Engine.assets.get(DataDir.Images + "player_shadow.png", Texture.class);
		}
		
		public void act(float delta)
		{
			player.act(delta);
			if (moving == -1)
				player.setX(Math.max(this.getX() - bound, player.getX() - (bound*delta)));
			if (moving == 1)
				player.setX(Math.min(this.getX() + bound, player.getX() + (bound*delta)));
		}
		
		@Override
		public void setX(float x)
		{
			player.setX(x);
		}
		
		@Override
		public void setPosition(float x, float y)
		{
			super.setPosition(x-player.getWidth()/2, y); //sets the center point of movement
			/*
			 * we can use timelines to ensure that the player moves to the proper edges in a proper amount of time
			 * since the player is at the middle, that means the values are initally calculated as if it should
			 * take a set amount of time to move from the middle to an edge.
			 * 
			 * the best part of this is movement is entirely time dependent instead of fps dependent
		 	 */ 
			System.out.println("making new points");
			player.setPosition(x-player.getWidth()/2, y);
		}
		
		public void draw(SpriteBatch batch)
		{
			batch.draw(playerShadow, player.getX(), player.getY()-10);
			player.draw(batch, 1.0f);
		}
	}
	
	private class KeyListener extends InputListener
	{
		
		private Player player;
		
		public KeyListener(Player player)
		{
			this.player = player;
		}
		
		@Override
		public boolean keyDown(InputEvent e, int key)
		{
			if (key == Keys.LEFT)
			{
				player.moving = -1;
				return true;
			}
			else if (key == Keys.RIGHT)
			{
				player.moving = 1;
				return true;
			}
			return false;
		}
		
		public boolean keyUp(InputEvent e, int key)
		{
			if (key == Keys.LEFT || key == Keys.RIGHT)
			{
				if (Gdx.input.isKeyPressed(Keys.RIGHT))
					player.moving = 1;
				else if (Gdx.input.isKeyPressed(Keys.LEFT))
					player.moving = -1;
				else
					player.moving = 0;
				return true;
			}
			return false;
		}
	}
	
	private static class Banner extends Sprite
	{
		Timeline scroll;
		public Banner(int type, boolean right)
		{
			super();
			Texture t = Engine.assets.get(DataDir.Ui + "banners.png", Texture.class);
			t.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
			SpriteSheet tex = new SpriteSheet(t, 1, 3);
			
			this.setTexture(tex.getTexture());
			this.setRegion(tex.getFrame(type));
			this.setSize(220, tex.getFrameHeight());
			this.setOrigin(0f, 0f);
			this.rotate(-90);
			this.flip(false, right);
			
			scroll = 
					Timeline.createSequence()
						.push(Tween.set(this, Tweens.U).target(0.0f, 220f/tex.getFrameWidth()))
						.push(Tween.to(this, Tweens.U, 1.5f).target(1.0f/(220f/tex.getFrameWidth()), 220f/tex.getFrameWidth()+tex.getFrameWidth()/220f).ease(TweenEquations.easeNone))
					.start();
		}
		
		@Override
		public void setPosition(float x, float y)
		{
			super.setPosition(x, y+220);
		}
	}
	
	private static class Banners extends Sprite implements Observer<PlayerNotification>
	{
		
		private Banner[] left;
		private Banner[] right;
		
		private Timeline scroll;
		
		private boolean lowAmmo;
		private boolean lowHP;
		
		TweenManager tm;
		
		public Banners(TweenManager tm)
		{
			this.tm = tm;
			left = new Banner[3];
			right = new Banner[3];
			
			for (int i = 0; i < 3; i++)
			{
				left[i] = new Banner(i, false);
				right[i] = new Banner(i, true);
			}
			
			scroll = 
				Timeline.createParallel()
					.push(left[0].scroll)
					.push(left[1].scroll)
					.push(left[2].scroll)
					.push(right[0].scroll)
					.push(right[1].scroll)
					.push(right[2].scroll)
					.repeat(-1, 0)
				.start(tm);
		}
		@Override
		public void setPosition(float x, float y)
		{
			left[0].setPosition(x-left[0].getHeight(), y);
			right[0].setPosition(x+190, y);
			left[1].setPosition(x-left[0].getHeight(), y);
			right[1].setPosition(x+190, y);
			left[2].setPosition(x, y);
			right[2].setPosition(x+190-right[0].getHeight(), y);
		}

		public void draw(SpriteBatch batch)
		{
			for (int i = 0; i < left.length; i++)
			{
				left[i].draw(batch);
				right[i].draw(batch);
			}
		}
		
		@Override
		public void update(PlayerNotification type, Object... values) 
		{
			/*
			 * Stacking notification banners appear at the edges 
			 */
			if (type == PlayerNotification.AmmoLow)
			{
				lowAmmo = (boolean)values[0];
				if (lowAmmo && lowHP)
				{
					Timeline.createSequence()
						.beginParallel()
							.push(Tween.to(left[0], Tweens.X, .25f).target(this.getX()+left[1].getHeight()))
							.push(Tween.to(left[1], Tweens.X, .25f).target(this.getX()))
							.push(Tween.to(right[0], Tweens.X, .25f).target(this.getX()+190-right[1].getHeight()*2))
							.push(Tween.to(right[1], Tweens.X, .25f).target(this.getX()+190-right[1].getHeight()))
						.end()
					.start(tm);
				}
				else if (lowAmmo)
				{
					Timeline.createSequence()
						.beginParallel()
							.push(Tween.to(left[1], Tweens.X, .25f).target(this.getX()))
							.push(Tween.to(right[1], Tweens.X, .25f).target(this.getX()+190-right[1].getHeight()))
						.end()
					.start(tm);	
				}
				else
				{
					Timeline.createSequence()
						.beginParallel()
							.push(Tween.to(left[1], Tweens.X, .25f).target(this.getX()-left[1].getHeight()))
							.push(Tween.to(right[1], Tweens.X, .25f).target(this.getX()+190))
						.end()
					.start(tm);		
				}
			}
			else if (type == PlayerNotification.HPLow)
			{
				lowHP = (boolean)values[0];
				if (lowAmmo && lowHP)
				{
					Timeline.createSequence()
						.beginParallel()
							.push(Tween.to(left[0], Tweens.X, .25f).target(this.getX()+left[1].getHeight()))
							.push(Tween.to(left[1], Tweens.X, .25f).target(this.getX()))
							.push(Tween.to(right[0], Tweens.X, .25f).target(this.getX()+190-right[1].getHeight()*2))
							.push(Tween.to(right[1], Tweens.X, .25f).target(this.getX()+190-right[1].getHeight()))
						.end()
					.start(tm);
				}
				else if (lowHP)
				{
					Timeline.createSequence()
						.beginParallel()
							.push(Tween.to(left[0], Tweens.X, .25f).target(this.getX()))
							.push(Tween.to(right[0], Tweens.X, .25f).target(this.getX()+190-right[1].getHeight()))
						.end()
					.start(tm);	
				}
				else
				{
					Timeline.createSequence()
						.beginParallel()
							.push(Tween.to(left[0], Tweens.X, .25f).target(this.getX()-left[0].getHeight()))
							.push(Tween.to(right[0], Tweens.X, .25f).target(this.getX()+190))
						.end()
					.start(tm);		
				}
			}
		}
	}
}
