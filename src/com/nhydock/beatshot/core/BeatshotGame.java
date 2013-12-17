package com.nhydock.beatshot.core;
import com.badlogic.gdx.Game;
import com.nhydock.beatshot.scenes.Main.Scene;
import com.nhydock.beatshot.util.SceneManager;

/**
 * Main application runner for the game
 * @author nhydock
 *
 */
public class BeatshotGame extends Game {
	
	public static final float[] INTERNAL_RES = {240, 320};
	
	static
	{
		SceneManager.register("main", com.nhydock.beatshot.scenes.Main.Scene.class);
	}
	
	@Override
	public void create() {
		this.setScreen(SceneManager.create("main"));
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
