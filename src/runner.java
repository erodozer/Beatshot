import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import core.BeatshotGame;
import static core.BeatshotGame.INTERNAL_RES;

/**
 * Main application runner for the game
 * @author nhydock
 *
 */
public class runner  {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        // define the window's title
        final String title = "BeatShot";
 
        // define the window's size
        final int width = (int)(INTERNAL_RES[0]*2);
        final int height = (int)(INTERNAL_RES[1]*2);
        
        // use ES2 to allow for textures that are not powers of 2
        final boolean useOpenGLES2 = true;
 
        // create the game using Lwjgl starter class
        LwjglApplication app = new LwjglApplication(new BeatshotGame(), title, width, height, useOpenGLES2 );
	}
}
