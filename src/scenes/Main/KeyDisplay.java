package scenes.Main;

import org.lwjgl.opengl.Display;

import logic.Consts;
import logic.Consts.Lasers;
import logic.Engine;
import logic.Consts.DataDir;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.shipvgdc.sugdk.graphics.SpriteSheet;

public class KeyDisplay extends Group {

	SpriteSheet KeyTex;
	Texture WheelTex;
	
	ImageButton[] keys;
	
	public static void loadAssets()
	{
		Engine.assets.load(DataDir.Ui + "frame.png", Texture.class);
	}
	
	public KeyDisplay()
	{
		super();
		
		Image frame = new Image(Engine.assets.get(DataDir.Ui + "frame.png", Texture.class));
		frame.setSize(240, 75);
		this.addActor(frame);
		
		keys = new ImageButton[Consts.Lasers.values().length];
		KeyTex = new SpriteSheet(new Texture(DataDir.Ui + "keys.png"), 4, 1);
		Drawable[] states = {(new Image(KeyTex.getFrame(0))).getDrawable(),
		                     (new Image(KeyTex.getFrame(1))).getDrawable(),
		                     (new Image(KeyTex.getFrame(2))).getDrawable(),
		                     (new Image(KeyTex.getFrame(3))).getDrawable()};

		for (int i = 0, x = 100, y = 8; i < keys.length; i++, x += KeyTex.getFrameWidth())
		{
			ImageButton key;
			//odd should be black
			if ((i & 0x0001) == 0x0001) 
			{
				key = new ImageButton(states[2], states[3], states[3]);
				key.setPosition(x, y + KeyTex.getFrameHeight()*.33f);
			}
			//even are white (that is since we're counting from 0)
			else 
			{
				key = new ImageButton(states[0], states[1], states[1]);
				key.setPosition(x, y);
			}
			key.setTouchable(Touchable.disabled);
			keys[i] = key;
			this.addActor(key);
		}
		
		this.addListener(new KeyListener(keys));
	}
	
	//input listener that swaps the key's images when a key is pressed
	private static class KeyListener extends InputListener
	{
		ImageButton[] keys;
		
		public KeyListener(ImageButton[] keys)
		{
			this.keys = keys;
		}

		@Override
		public boolean keyDown(InputEvent event, int keycode)
		{
			System.out.println(keycode);
			Lasers laser = Lasers.valueOf(keycode);
			if (laser != null)
			{
				ImageButton image = this.keys[laser.ordinal()];
				image.setChecked(true);
				System.out.println(laser + " pressed");
				return true;
			}
			return false;
		}
		
		@Override
		public boolean keyUp(InputEvent event, int keycode)
		{
			Lasers laser = Lasers.valueOf(keycode);
			if (laser != null)
			{
				ImageButton image = this.keys[laser.ordinal()];
				image.setChecked(false);
				return true;
			}
			return false;
		}
		
	}
}
