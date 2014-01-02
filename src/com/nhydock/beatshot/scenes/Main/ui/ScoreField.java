package com.nhydock.beatshot.scenes.Main.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.util.Tools;
import com.nhydock.beatshot.util.SpriteSheet;

public class ScoreField extends Sprite {

	Sprite back;
	Sprite tab;
	BitmapFont scoreFont;
	
	private static final String format = "%010d";
	
	public static void loadAssets()
	{
		Tools.assets.load(DataDir.Ui + "tabbar.png", Texture.class);
		Tools.assets.load(DataDir.Ui + "tabs.png", Texture.class);
	}
	
	public ScoreField()
	{
		back = new Sprite(Tools.assets.get(DataDir.Ui + "tabbar.png", Texture.class));
		back.setSize(25, 190);
		back.setV2(190);
		back.rotate(-90);
		
		//scoreFont = Engine.assets.get(DataDir.Fonts + "myfont.fnt", BitmapFont.class);
		scoreFont = new BitmapFont(Gdx.files.internal(DataDir.Fonts+"score.fnt"), Gdx.files.internal(DataDir.Fonts+"score.png"), false);
		//scoreFont.setScale(16.0f);
		SpriteSheet s = new SpriteSheet(Tools.assets.get(DataDir.Ui + "tabs.png", Texture.class), 4, 1);
		tab = new Sprite(s.getFrame(2));
		tab.rotate(-90);
		tab.setX(190-tab.getHeight());
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		back.setPosition(x-5, y);
		tab.setPosition(x+back.getHeight()-tab.getHeight(), y);
	}
	
	@Override
	public void draw(SpriteBatch batch)
	{
		back.draw(batch);
		tab.draw(batch);
		scoreFont.setColor(Color.WHITE);
		scoreFont.drawMultiLine(batch, String.format(format, (int)BeatshotGame.score), 
				back.getX()+10, back.getY() + 20, 0, HAlignment.LEFT);
	}
}
