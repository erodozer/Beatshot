package com.nhydock.beatshot.scenes.Main.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.core.Consts.PlayerInput;
import com.nhydock.beatshot.logic.Engine;

public class KeyDisplay extends Group {

	Texture KeyTex;
	Texture WheelTex;
	
	Sprite[] keys;
	Sprite disc;
	Sprite frame;
	
	public KeyListener inputListener;
	
	public static void loadAssets()
	{
		Engine.assets.load(DataDir.Ui + "frame.png", Texture.class);
		Engine.assets.load(DataDir.Ui + "keys.png", Texture.class);
		Engine.assets.load(DataDir.Ui + "disc.png", Texture.class);
	}
	
	public KeyDisplay()
	{
		super();
		
		frame = new Sprite(Engine.assets.get(DataDir.Ui + "frame.png", Texture.class));
		frame.setSize(240, 75);
		
		keys = new Sprite[PlayerInput.Lasers];
		KeyTex = Engine.assets.get(DataDir.Ui + "keys.png", Texture.class);

		for (int i = 0, x = 88, y = 12; i < keys.length; i++, x += KeyTex.getWidth() >> 2)
		{
			Sprite key = new Sprite(KeyTex);
			key.setScale(.25f, 1.0f);
			//odd should be black
			if ((i & 0x0001) == 0x0001) 
			{
				key.setU(.5f); key.setU2(.75f);
				key.setPosition(x-key.getRegionWidth(), y + 12);
			}
			//even are white (that is since we're counting from 0)
			else 
			{
				key.setU(0f); key.setU2(.25f);
				key.setPosition(x-key.getRegionWidth(), y);
			}
			keys[i] = key;
		}
		disc = new Sprite(Engine.assets.get(DataDir.Ui + "disc.png", Texture.class));
		disc.setU(0);
		disc.setU2(1/3f);
		disc.setScale(1/3f, 1.0f);
		disc.setPosition(20-disc.getRegionWidth(), 14);
		
		inputListener = new KeyListener(keys, disc);
	}
	
	public void draw(SpriteBatch batch, float alpha)
	{
		frame.draw(batch);
		for (Sprite k : keys)
		{
			k.draw(batch);
		}
		disc.draw(batch);
	}
	
	//input listener that swaps the key's images when a key is pressed
	private static class KeyListener implements InputProcessor
	{
		private Sprite[] keys;
		private Sprite disc;
		
		public KeyListener(Sprite[] keys, Sprite disc)
		{
			this.keys = keys;
			this.disc = disc;
		}

		@Override
		public boolean keyDown(int keycode)
		{
			PlayerInput input = PlayerInput.valueOf(keycode);
			if (input == PlayerInput.LEFT)
			{
				disc.setU(1/3f);
				disc.setU2(2/3f);
			}
			else if (input == PlayerInput.RIGHT)
			{
				disc.setU(2/3f);
				disc.setU2(1f);
			}
			else if (input != null)
			{
				int i = input.ordinal();
				Sprite image = this.keys[i];
				if ((i & 0x0001) == 0x0001) 
				{
					image.setU(.75f); image.setU2(1.0f);
				}
				else
				{
					image.setU(.25f); image.setU2(.5f);	
				}
			}
			
			return false;
		}
		
		@Override
		public boolean keyUp(int keycode)
		{
			PlayerInput input = PlayerInput.valueOf(keycode);
			if (input.valueOf(keycode) == null)
				return false;
			
			if (input.ordinal() < input.Lasers)
			{
				int i = input.ordinal();
				Sprite image = this.keys[i];
				if ((i & 0x0001) == 0x0001) 
				{
					image.setU(.5f); image.setU2(.75f);
				}
				else
				{
					image.setU(0f); image.setU2(.25f);	
				}
			}
			
			boolean set = false;
			for (int i = 0; i < PlayerInput.LEFT.keys.length && !set; i++)
			{
				if (Gdx.input.isKeyPressed(PlayerInput.LEFT.keys[i]))
				{
					disc.setU(1/3f);
					disc.setU2(2/3f);
					set = true;
				}
			}
			for (int i = 0; i < PlayerInput.RIGHT.keys.length && !set; i++)
			{
				if (Gdx.input.isKeyPressed(PlayerInput.RIGHT.keys[i]))
				{
					disc.setU(2/3f);
					disc.setU2(1f);
					set = true;
				}
			}
			if (!set)
			{
				disc.setU(0f);
				disc.setU2(1/3f);
			}
			return false;
		}

		@Override
		public boolean keyTyped(char arg0) {
			// TODO Auto-generated method stub
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
}
