import logic.Engine;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shipvgdc.sugdk.tween.TweenableActor;
import com.shipvgdc.sugdk.tween.TweenableSprite;

/**
 * Main application runner for the game
 * @author nhydock
 *
 */
public class runner extends Game {
	
	static
	{
		Tween.registerAccessor(Actor.class, new TweenableActor());
		Tween.registerAccessor(Sprite.class, new TweenableSprite());
		Tween.setCombinedAttributesLimit(4);
	}
	
	@Override
	public void create() {
		scenes.Main.Scene scene1 = new scenes.Main.Scene();
		
		this.setScreen(scene1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create the listener that will receive the application events
        ApplicationListener listener = new runner();
 
        // define the window's title
        final String title = "BeatShot";
 
        // define the window's size
        final int width = 480;
        final int height = 640;
        
        // use ES2 to allow for textures that are not powers of 2
        final boolean useOpenGLES2 = true;
 
        // create the game using Lwjgl starter class
        new LwjglApplication( listener, title, width, height, useOpenGLES2 );
   
	}
}
