package com.nhydock.beatshot.util;

import static com.nhydock.beatshot.core.BeatshotGame.INTERNAL_RES;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.JsonReader;

public class Tools {

	public static AssetManager assets;
	public static JsonReader json;
	public static final InputProcessor utils;
	
	static
	{
		assets = new AssetManager();
		json = new JsonReader();
		utils = new InputProcessor(){

			@Override
			public boolean keyDown(int key) {
				if (key == Input.Keys.F11)
				{
					int width, height;
					width = Gdx.graphics.getWidth();
					height = Gdx.graphics.getHeight();
					
					DisplayMode desktop = Gdx.graphics.getDesktopDisplayMode();
					if (width != desktop.width || height != desktop.height)
					{
						Gdx.graphics.setDisplayMode(desktop);
					}
					else
					{
						Gdx.graphics.setDisplayMode((int)INTERNAL_RES[0]*2, (int)INTERNAL_RES[1]*2, false);
					}
					return true;
				}
				if (key == Input.Keys.F9)
				{
					Gdx.app.exit();
					return true;
				}
				return false;
			}
			
			public boolean keyTyped(char arg0) { return false; }
			public boolean keyUp(int arg0) { return false; }
			public boolean mouseMoved(int arg0, int arg1) {	return false; }
			public boolean scrolled(int arg0) {	return false; }
			public boolean touchDown(int arg0, int arg1, int arg2, int arg3) { return false; }
			public boolean touchDragged(int arg0, int arg1, int arg2) {	return false; }
			public boolean touchUp(int arg0, int arg1, int arg2, int arg3) { return false; }
		};
	}
}
