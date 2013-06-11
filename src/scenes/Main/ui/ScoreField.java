package scenes.Main.ui;

import logic.Engine;
import logic.Consts.DataDir;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.shipvgdc.sugdk.graphics.SpriteSheet;

public class ScoreField extends Sprite {

	Sprite back;
	Sprite tab;
	BitmapFont scoreFont;
	
	private static final String format = "%010d";
	
	public static void loadAssets()
	{
		Engine.assets.load(DataDir.Ui + "tabbar.png", Texture.class);
		Engine.assets.load(DataDir.Ui + "tabs.png", Texture.class);
	}
	
	public ScoreField()
	{
		back = new Sprite(Engine.assets.get(DataDir.Ui + "tabbar.png", Texture.class));
		back.setSize(25, 190);
		back.setV2(190);
		back.rotate(-90);
		
		//scoreFont = Engine.assets.get(DataDir.Fonts + "myfont.fnt", BitmapFont.class);
		scoreFont = new BitmapFont(Gdx.files.internal(DataDir.Fonts+"score.fnt"), Gdx.files.internal(DataDir.Fonts+"score.png"), false);
		//scoreFont.setScale(16.0f);
		SpriteSheet s = new SpriteSheet(Engine.assets.get(DataDir.Ui + "tabs.png", Texture.class), 4, 1);
		tab = new Sprite(s.getFrame(2));
		tab.rotate(-90);
		tab.setX(190-tab.getHeight());
	}
	
	public void setPosition(float x, float y)
	{
		back.setPosition(x-5, y);
		tab.setPosition(x+back.getHeight()-tab.getHeight(), y);
	}
	
	public void draw(SpriteBatch batch)
	{
		back.draw(batch);
		tab.draw(batch);
		scoreFont.setColor(Color.WHITE);
		scoreFont.drawMultiLine(batch, String.format(format, Engine.score), 
				back.getX()+10, back.getY() + 20, 0, HAlignment.LEFT);
	}
}
