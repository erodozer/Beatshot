package com.nhydock.beatshot.CEF.Components;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Path extends Component {
	public static ComponentType CType = ComponentType.getTypeFor(Path.class);
	
	public com.badlogic.gdx.math.Path path;
	public float duration;	

	/**
	 * @param path - path for the entity to follow
	 * @param duration - time it should take to traverse the path
	 */
	public Path(com.badlogic.gdx.math.Path path, float duration) {
		this.path = path;
		this.duration = duration;
	}
}
