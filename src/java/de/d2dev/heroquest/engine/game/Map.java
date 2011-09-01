package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.files.FileUtil;
import de.d2dev.fourseasons.util.Observed;
import de.d2dev.fourseasons.util.ListenerUtil;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * Class for a heroquest map that holds all the objects associated with a map,
 * like monsters, heroes, fields and so on.
 * 
 * @author Sebastian Bordt
 *
 */
public final class Map implements Observed<MapListener> {
	
	private static final String HERO_QUEST_MAP = "heroquestmap";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String FIELDS = "fields";
	
	private ListenerUtil<MapListener> listeners = new ListenerUtil<MapListener>();
	
	private int width;
	private int height;
	
	private Field[][] fields;
	
	/**
	 * The upper left field of the four fields at
	 * witch the heroes start the game!
	 */
	private Field startingField;
	
	void fireOnUnitEntersField(Field field) {
		for (MapListener l : this.listeners) {
			l.onUnitEntersField(field);
		}
	}
	
	void fireOnUnitLeavesField(Field field) {
		for (MapListener l : this.listeners) {
			l.onUnitLeavesField(field);
		}
	}

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
				this.fields[x][y] = new Field( this, x, y );
			}
		}
		
		this.startingField = this.fields[0][0];
	}
	
	/**
	 * Read from xml.
	 * @param file
	 */
	public Map(Element element) {
		if ( element.getLocalName() != HERO_QUEST_MAP )	// verify parameter
			throw new IllegalArgumentException();
		
		this.width = FileUtil.readIntAttribute( element, WIDTH );
		this.height = FileUtil.readIntAttribute( element, HEIGHT );
		
		// parse the fields
		this.fields = new Field[this.width][this.height];
		
		Elements field_elements = element.getChildElements( FIELDS ).get(0).getChildElements();
		
		for (int i=0; i<field_elements.size(); i++) {
			Field field = new Field( this, field_elements.get(i) );
			
			if ( this.fields[field.getX()][field.getY()] == null ) {
				this.fields[field.getX()][field.getY()] = field;
			} else {
				throw new IllegalArgumentException("Field appears twice in xml");
			}	
		}
		
		// all fields parsed?
		for (Field[] fields : this.fields) {
			for(Field field : fields) {
				if ( field == null ) {
					throw new IllegalArgumentException("A field does not appear in xml");
				}
			}
		}
	}
	
	@Override
	public void addListener(MapListener l) {
		this.listeners.addListener(l);
	}

	@Override
	public void removeListener(MapListener l) {
		this.listeners.removeListener(l);
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
	 * Save to xml.
	 * (No game data will be saved, just the map as-is).
	 * @param file
	 */
	public Element toXML() {
		Element xml = new Element( HERO_QUEST_MAP );
		xml.addAttribute( new Attribute( WIDTH, Integer.toString( this.width ) ) );
		xml.addAttribute( new Attribute( HEIGHT, Integer.toString( this.height ) ) );
		
		// save the fields
		Element xml_fields = new Element( FIELDS );
		
		for (Field[] fields : this.fields) {
			for (Field field : fields) {
				xml_fields.appendChild( field.toXML() );
			}
		}
		
		xml.appendChild( xml_fields );
		
		return xml;
	}
}
