package com.nhydock.beatshot.scenes.Main.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nhydock.beatshot.CEF.Components.Ammo;
import com.nhydock.beatshot.CEF.Components.Health;
import com.nhydock.beatshot.core.BeatshotGame;
import com.nhydock.beatshot.core.Consts.DataDir;
import com.nhydock.beatshot.util.Tools;
import com.nhydock.beatshot.util.SpriteSheet;

import static com.nhydock.beatshot.CEF.RenderSystem.FOV;

public class StatBars extends Sprite{
	
	private static Texture frameTex;
	private static SpriteSheet tabsTex;
	private static SpriteSheet barsTex;
	
	
	/**
	 * Specify all assets that need to be loaded first before this class may be instantiated
	 */
	public static void loadAssets()
	{
		Tools.assets.load(DataDir.Ui + "barframe.png", Texture.class);
		Tools.assets.load(DataDir.Ui + "tabs.png", Texture.class);
		Tools.assets.load(DataDir.Ui + "statbar.png", Texture.class);
	}
	
	private Bar hpBar;
	private Bar ammoBar;
	
	public StatBars()
	{
		super();
		frameTex = Tools.assets.get(DataDir.Ui + "barframe.png", Texture.class);
		frameTex.setWrap(TextureWrap.ClampToEdge, TextureWrap.Repeat);
		tabsTex = new SpriteSheet(Tools.assets.get(DataDir.Ui + "tabs.png", Texture.class), 4, 1);
		Texture t = Tools.assets.get(DataDir.Ui + "statbar.png", Texture.class);
		t.setWrap(TextureWrap.ClampToEdge, TextureWrap.Repeat);
		barsTex = new SpriteSheet(t, 3, 1);
		hpBar = new Bar(0);
		ammoBar = new Bar(1);
		ammoBar.setX(25);
	}
	
	public void clear()
	{
		frameTex = null;
		tabsTex = null;
		
		Tools.assets.unload(DataDir.Ui + "barframe.png");
		Tools.assets.unload(DataDir.Ui + "tabs.png");
		Tools.assets.unload(DataDir.Ui + "bars/ammo.png");
		Tools.assets.unload(DataDir.Ui + "bars/hp.png");
		Tools.assets.unload(DataDir.Ui + "bars/empty.png");
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		hpBar.setPosition(x, y);
		ammoBar.setPosition(x+FOV[2]+25, y);
	}
	
	@Override
	public void draw(SpriteBatch batch, float alpha)
	{
		Health h = BeatshotGame.player.getComponent(Health.class);
		hpBar.setFill(h.getPercent());
		hpBar.draw(batch, alpha);
		
		Ammo a = BeatshotGame.player.getComponent(Ammo.class);
		ammoBar.setFill(a.getPercent());
		ammoBar.draw(batch, alpha);
	}
	
	@Override
	public void draw(SpriteBatch batch)
	{
		this.draw(batch, 1.0f);
	}
	
	private static class Bar extends Actor
	{
		TextureRegion bar;
		TextureRegion back;
		TextureRegion frame;
		TextureRegion typetab;
		TextureRegion decotab;
		
		float fill;
		
		static final int BARHEIGHT = 210;
		static final int REPEAT = 105;
		
		public Bar(int type)
		{
			super();
			typetab = tabsTex.getFrame(type);
			decotab = tabsTex.getFrame(3);
			
			frame = new TextureRegion(frameTex);
			frame.setV2(224);
			
			bar = barsTex.getFrame(type);
			back = barsTex.getFrame(2);
			back.setV2(REPEAT);
			fill = 1.0f;
		}
		
		@Override
		public void draw(SpriteBatch batch, float alpha)
		{
			batch.draw(typetab, this.getX(), this.getY()+222);
			batch.draw(frame, this.getX(), this.getY(), frame.getRegionWidth(), 224);
			batch.draw(decotab, this.getX(), this.getY()+224-decotab.getRegionHeight());
			batch.draw(back, this.getX() + 6, this.getY() + 5, 12, BARHEIGHT);
			bar.setV2((int)(REPEAT*fill));
			batch.draw(bar, this.getX() + 6, this.getY() + 5, 12, (int)(REPEAT*fill) << 1);
		}
		public void setFill(float amount)
		{
			fill = Math.min(1.0f, Math.max(0, amount));
		}
	}
}
