package de.d2dev.heroquest.engine;

import de.d2dev.heroquest.engine.files.HqMapFile;

/**
 * Class for a heroquest map that holds all the objects associated with a map,
 * like monsters, heroes, fields and so on.
 * 
 * @author Sebastian Bordt
 *
 */
public class Map {
	
	private int width;
	private int height;
	
	private Field[][] fields;
	
	/**
	 * Construct a new empty map.
	 * @param width > 0
	 * @param height > 0
	 */
	public Map(int width, int height) {
		if ( width <= 0 || height <= 0 )
			throw new IllegalArgumentException();
		
		this.width = width;
		this.height = height;
		
		this.fields = new Field[width][height];
		
		for (int x=0; x<this.width; x++) {
			for (int y=0; y<this.height; y++) {
				this.fields[x][y] = new Field( x, y );
			}
		}
	}
	
	/**
	 * Load a map from file.
	 * @param file
	 */
	public Map(HqMapFile file) {
		
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Field getField(int x, int y) {
		return this.fields[x][y];
	}
	
	public Field[][] getFields() {
		return fields;
	}
	
	/**
	 * Save a map to file.
	 * (No game data will be saved, just the map as-is).
	 * @param file
	 */
	public void save(HqMapFile file) {
		
	}
}
