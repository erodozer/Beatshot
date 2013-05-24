package scenes.Main.ui;

import logic.Consts.DataDir;
import logic.Engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shipvgdc.sugdk.graphics.SpriteSheet;

public class ScoreField extends Sprite {

	Sprite back;
	Sprite tab;
	BitmapFont scoreFont;
	String score;
	
	private static final String format = "%010d";
	
	public static void loadAssets()
	{
		Engine.assets.load(DataDir.Ui + "tabbar.png", Texture.class);
		Engine.assets.load(DataDir.Ui + "tabs.png", Texture.class);
		Engine.assets.load(DataDir.Fonts + "score2.fnt", BitmapFont.class);
	}
	
	public ScoreField()
	{
		back = new Sprite(Engine.assets.get(DataDir.Ui + "tabbar.png", Texture.class));
		back.setSize(25, 190);
		back.setV2(190);
		back.rotate(-90);
		
		scoreFont = Engine.assets.get(DataDir.Fonts + "score2.fnt", BitmapFont.class);
		
		SpriteSheet s = new SpriteSheet(Engine.assets.get(DataDir.Ui + "tabs.png", Texture.class), 4, 1);
		tab = new Sprite(s.getFrame(2));
		tab.rotate(-90);
		tab.setX(190-tab.getHeight());
		
		this.setScore(0);
	}
	
	public void setPosition(float x, float y)
	{
		back.setPosition(x, y);
		tab.setPosition(x+back.getHeight()-tab.getHeight(), y);
	}
	
	public void setScore(int score)
	{
		this.score = String.format(format, score);
	}
	
	public void draw(SpriteBatch batch)
	{
		back.draw(batch);
		tab.draw(batch);
		//scoreFont.draw(batch, score, this.getX(), scoreFont.getAscent());
	}
}
