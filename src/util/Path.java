package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import util.Step;

/**
 * A path determined by some path finding algorithm. A series of steps from
 * the starting location to the target location. This includes a step for the
 * initial location.
 * 
 * @author Kevin Glass
 * @author Nicholas Hydock
 */
public class Path implements Serializable, Iterable<Step> {
	/** The serial identifier for this class */
	private static final long serialVersionUID = 1L;
	
	/** The list of steps building up this path */
	private ArrayList<Step> steps;
	
	/**
	 * Create an empty path
	 */
	public Path() {
		steps = new ArrayList<Step>();
	}

	/**
	 * Get the length of the path, i.e. the number of steps
	 * 
	 * @return The number of steps in this path
	 */
	public int getLength() {
		return steps.size();
	}
	
	/**
	 * Get the step at a given index in the path
	 * 
	 * @param index The index of the step to retrieve. Note this should
	 * be >= 0 and < getLength();
	 * @return The step information, the position on the map.
	 */
	public Step getStep(int index) {
		return steps.get(index);
	}
	
	/**
	 * Get the x coordinate for the step at the given index
	 * 
	 * @param index The index of the step whose x coordinate should be retrieved
	 * @return The x coordinate at the step
	 */
	public float getX(int index) {
		return getStep(index).x;
	}

	/**
	 * Get the y coordinate for the step at the given index
	 * 
	 * @param index The index of the step whose y coordinate should be retrieved
	 * @return The y coordinate at the step
	 */
	public float getY(int index) {
		return getStep(index).y;
	}
	
	/**
	 * Append a step to the path.  
	 * 
	 * @param f The x coordinate of the new step
	 * @param g The y coordinate of the new step
	 */
	public void appendStep(float f, float g) {
		steps.add(new Step(f,g));
	}

	/**
	 * Prepend a step to the path.  
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 */
	public void prependStep(float x, float y) {
		steps.add(0, new Step(x, y));
	}
	
	/**
	 * Check if this path contains the given step
	 * 
	 * @param x The x coordinate of the step to check for
	 * @param y The y coordinate of the step to check for
	 * @return True if the path contains the given step
	 */
	public boolean contains(int x, int y) {
		return steps.contains(new Step(x,y));
	}

	/**
	 * Gets an iterator that can follow the path
	 */
	@Override
	public Iterator<Step> iterator() {
		return steps.iterator();
	}
	
	/**
	 * Gets any continuous point along the curve
	 * @param time - location along the path (between 0.0 and 1.0)
	 * @return Vector2 of x, y locations
	 */
	public Vector2 getContinuousLocation(float time)
	{
		Vector2 loc;
		
		int l1, l2;
		int length = steps.size();
		
		//calculate two closest points
		float real = time*length;
		l1 = (int)(real);
		l2 = (int)Math.ceil(time*length);
		//wrap around points
		if (l2 >= steps.size())
		{
			l2 = 0;
		}
		float var = real-l1;
		
		Step s1 = steps.get(l1);
		Step s2 = steps.get(l2);
		float xDiff = (s2.x - s1.x) * var;
		float yDiff = (s2.y - s1.y) * var;
		
		loc = new Vector2(s1.x + xDiff, s1.y + yDiff);
	
		return loc;
	}
}
