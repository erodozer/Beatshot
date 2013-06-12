package util;

import java.io.Serializable;

/**
 * A single step within the path
 * 
 * @author Kevin Glass
 */
public class Step implements Serializable {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2158176495267123212L;
	
	/** The x coordinate at the given step */
	public final float x;
	/** The y coordinate at the given step */
	public final float y;
	
	/**
	 * Create a new step
	 * 
	 * @param x The x coordinate of the new step
	 * @param y The y coordinate of the new step
	 */
	public Step(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get the x coordinate of the new step
	 * 
	 * @return The x coodindate of the new step
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get the y coordinate of the new step
	 * 
	 * @return The y coodindate of the new step
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int)(x*y);
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Step) {
			Step o = (Step) other;
			
			return (o.x == x) && (o.y == y);
		}
		
		return false;
	}
	
	public String toString()
	{
		return String.format("Step: (x: %f, y:%f)\n", x, y);
	}
}