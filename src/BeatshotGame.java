import logic.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * Main application runner for the game
 * @author nhydock
 *
 */
public class BeatshotGame extends Game {
	
	@Override
	public void create() {
		scenes.Main.Scene scene1 = new scenes.Main.Scene();
		
		this.setScreen(scene1);
		
		
	}
	
	private float timer;
	private final float debug_rate = 5.0f;
	
	public void render()
	{
		super.render();
		
		/*
		timer += Gdx.graphics.getDeltaTime();
		if (timer > debug_rate)
		{
			Gdx.app.log("Heap Mem (Mb)", ""+(Gdx.app.getJavaHeap() >> 17));
			Gdx.app.log("Nat Mem (Mb)", ""+(Gdx.app.getNativeHeap() >> 17));
			timer = 0;
		}
		*/
	}
}
