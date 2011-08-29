package de.d2dev.heroquest.engine.gamestate;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import de.d2dev.heroquest.engine.files.HqMapFile;

/**
 * Class for a heroquest map that holds all the objects associated with a map,
 * like monsters, heroes, fields and so on.
 * 
 * @author Sebastian Bordt
 *
 */
public final class Map {
	
	private static final String HERO_QUEST_MAP = "heroquestmap";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String FIELDS = "fields";
	
	private int width;
	private int height;
	
	private Field[][] fields;
	
	/**
	 * The upper left field of the four fields at
	 * witch the heroes start the game!
	 */
	private Field startingField;

	/**
	 * Construct a new empty map.
	 * @param width > 2
	 * @param height > 2
	 */
	public Map(int width, int height) {
		if ( width <= 2 || height <= 2 )
			throw new IllegalArgumentException();
		
		this.width = width;
		this.height = height;
		
		this.fields = new Field[width][height];
		
		for (int x=0; x<this.width; x++) {
			for (int y=0; y<this.height; y++) {
				this.fields[x][y] = new Field( x, y );
			}
		}
		
		this.startingField = this.fields[0][0];
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
	
	public Field getStartingField() {
		return startingField;
	}
	
	/**
	 * Save a map to file.
	 * (No game data will be saved, just the map as-is).
	 * @param file
	 */
	public void save(HqMapFile file) {
		Element map = new Element( HERO_QUEST_MAP );
		map.addAttribute( new Attribute( WIDTH, Integer.toString( this.width ) ) );
		map.addAttribute( new Attribute( HEIGHT, Integer.toString( this.height ) ) );
		
		// save the fields
		Element xml_fields = new Element( FIELDS );
		
		for (Field[] fields : this.fields) {
			for (Field field : fields) {
				xml_fields.appendChild( field.toXML() );
			}
		}
		
		map.appendChild( xml_fields );
		
		// save the generated xml to the map - file
		file.map = new Document( map );
	}
}
