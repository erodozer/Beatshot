package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;

public class Consts {

	public static enum Lasers
	{
		Laser1(Keys.A),
		Laser2(Keys.S),
		Laser3(Keys.SPACE),
		Laser4(Keys.D),
		Laser5(Keys.F);
		
		public final int key;
		
		private Lasers(final int key)
		{
			this.key = key;
		}
		
		public static Lasers valueOf(int key)
		{
			for (Lasers l : Lasers.values())
			{
				if (l.key == key)
					return l;
			}
			return null;
		}
	}
	
	public static enum DataDir
	{
		Home("data/"),
		Images("data/images/"),
		Sound("data/audio/"),
		BGM("data/audio/bgm/"),
		SFX("data/audio/sfx/"),
		Sprites("data/images/sprites/"),
		Ui("data/images/ui/"),
		Enemies("data/enemies/"),
		Levels("data/levels/");
		
		public final String path;
		
		private DataDir(final String path)
		{
			this.path = path;
		}
		
		public String toString()
		{
			return this.path;
		}
	}
	
	
}
