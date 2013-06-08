package EntitySystems.Components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Renderable extends Component {
	public Sprite sprite;
	boolean visible;
	
	public Renderable(Sprite sprite)
	{
		this.sprite = sprite;
		this.visible = true;
	}
	
	public Renderable(TextureRegion tr)
	{
		this.sprite = new Sprite(tr);
		this.visible = true;
	}
}
