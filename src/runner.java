import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

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
        final int width = 480;
        final int height = 640;
        
        // use ES2 to allow for textures that are not powers of 2
        final boolean useOpenGLES2 = true;
 
        // create the game using Lwjgl starter class
        new LwjglApplication(new BeatshotGame(), title, width, height, useOpenGLES2 );
   
	}
}
