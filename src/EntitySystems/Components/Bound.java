package EntitySystems.Components;

import java.util.List;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Bound extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Bound.class);
	
	public Shape shape;
	
	public Bound(Shape s)
	{
		this.shape = s;
	}
}
