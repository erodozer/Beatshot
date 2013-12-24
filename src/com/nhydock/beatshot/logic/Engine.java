package com.nhydock.beatshot.logic;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.JsonReader;

public class Engine {

	public static AssetManager assets;
	public static JsonReader json;
	public static String level;
	public static float score;
	
	public static World world;
	public static Entity player;
	public static boolean GameOver;
	
	public static Music bgm;
	
	static
	{
		level = "level001";
		assets = new AssetManager();
		score = 0;
		json = new JsonReader();
	}
}
