package com.nhydock.beatshot.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * SVGPath is a collection of tools made specifically for parsing out paths from SVGs.
 * I found such a thing to be a helpful idea when designing bullet paths and movement
 * patterns in conjunction with the Tween Engine.  Using something like Inkscape
 * allowed me to better visualize the paths and tweek them to perfection, something
 * that's a bit hard to do direction with code.
 * <p/>
 * SVGs are also a standard, unlike probably any kind of crappy path solution I would
 * have decided on making.  The DOM format also makes it even easier to do things
 * like define multiple paths within a single file.
 * <p/>
 * Please do not think this class offers a way to render SVGs.  This is strictly for
 * parsing out paths/bezier curves from SVGs for use in pathfinding.
 * <p/>
 * 
 * @author Nicholas Hydock
 */
public class SvgPathParser {
	
	/**
	 * Take an svg path that has already been selected out and parse it
	 * @param pathElement - the svg path to parse
	 * @return a Path
	 */
	public static Path<Vector2> parsePath(Element pathElement)
	{
		//select out the svg path points
		String pathPoints = pathElement.getAttribute("d");
			
		Bezier<Vector2> p = new Bezier<Vector2>();
		Array<Vector2> path = new Array<Vector2>();
		
		String[] points = pathPoints.split("[\r\n\t ,mMcC]+");
		//we need to start 1 in advance because of how svg paths start with m or c, and they don't get parsed out
		float x = 0;
		float y = 0;
		for (int i = 1; i < points.length; i += 2)
		{
			x += Float.parseFloat(points[i]);
			y += Float.parseFloat(points[i+1]);
			path.add(new Vector2(x, y));
		}
		p.set(path, 0, path.size);
		return p;
	}
	
	/**
	 * Get a specific path by its id
	 * @param svg - an already parsed svg file
	 * @param id - name of the path
	 * @return the parsed path if it exists
	 */
	public static Path<Vector2> getPath(Element svg, String id)
	{
		//search for the specified path
		Element selectedPath = null;
		for (Element e : svg.getChildrenByNameRecursively("path"))
		{
			if (e.getAttribute("id").equals(id))
			{
				selectedPath = e;
				break;
			}
		}
		if (selectedPath != null)
		{
			return SvgPathParser.parsePath(selectedPath);
		}
		else
		{
			System.err.print("Svg does not contain path with name " + id);
			return null;
		}
	}
	
	/**
	 * Parses all paths in the svg file
	 * @param svg - file to parse
	 * @return list of all the paths parsed
	 */
	public static Array<Path<Vector2>> getAllPaths(Element svg)
	{
		Array<Element> elements = svg.getChildrenByNameRecursively("path");
		Array<Path<Vector2>> paths = new Array<Path<Vector2>>();
		
		for (Element e : elements)
		{
			paths.add(SvgPathParser.parsePath(e));
		}
		return paths;
	}
	
	/**
	 * Gets a map of all path elements and their parsed versions.
	 * Useful if there's more data you wish to extract from the element than just points
	 * @param svg - element within the svg file to parse
	 * @return a hashmap of paths keyed by their element
	 */
	public static HashMap<Element, Path<Vector2>> getPathsMap(Element svg)
	{
		HashMap<Element, Path<Vector2>> map = new HashMap<Element, Path<Vector2>>();
		
		Array<Element> elements = svg.getChildrenByNameRecursively("path");
		
		for (Element e : elements)
		{
			map.put(e, SvgPathParser.parsePath(e));
		}
		return map;
	}
	
	/**
	 * Gets a map of all parsed paths and the name by which they are associated
	 * Useful if you want to get a specific path by its id multiple times
	 * @param svg - element within the svg file to parse
	 * @return a hashmap of paths keyed by their element
	 */
	public static HashMap<String, Path<Vector2>> getPathsNameMap(Element svg)
	{
		HashMap<String, Path<Vector2>> map = new HashMap<String, Path<Vector2>>();
		
		Array<Element> elements = svg.getChildrenByNameRecursively("path");
		
		for (Element e : elements)
		{
			map.put(e.getAttribute("id"), SvgPathParser.parsePath(e));
		}
		return map;
	}
	
	/**
	 * Get all parsed paths with a certain pattern within its id
	 * @param svg - the svg file to search
	 * @param regexId - pattern of a regular expression used to search ids
	 * @return a list of parsed paths
	 */
	public static ArrayList<Path<Vector2>> getAllPaths(Element svg, String regexId)
	{
		Array<Element> elements = svg.getChildrenByNameRecursively("path");
		ArrayList<Path<Vector2>> paths = new ArrayList<Path<Vector2>>();
		
		for (Element e : elements)
		{
			if (e.getAttribute("id").matches(regexId))
			{
				paths.add(SvgPathParser.parsePath(e));
			}
		}
		return paths;
	}
}
