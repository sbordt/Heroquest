package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.files.FileUtil;
import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.fourseasons.util.Observable;
import de.d2dev.fourseasons.util.ListenerUtil;
import de.d2dev.heroquest.engine.game.Hero.HeroType;

import java.util.*;

import com.google.common.base.Preconditions;

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
public final class Map implements Observable<MapListener> {
	
	private static final String HERO_QUEST_MAP = "heroquestmap";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String FIELDS = "fields";
	
	private ListenerUtil<MapListener> listeners = new ListenerUtil<MapListener>();
	
	private int width;
	private int height;
	
	private Field[][] fields;

//	private List<Unit> units = new ArrayList<Unit>();
//	private List<Unit> heroes = new ArrayList<Unit>();
//	private List<Unit> monsters = new ArrayList<Unit>();
	private List<Room> rooms = new ArrayList<Room>();
	
	private List<MapObject> objects = new ArrayList<MapObject>();
	
	private List<TextureOverlay> textureOverlays = new ArrayList<TextureOverlay>(); 
	
	
	
	private GameContext context;
	
	/**************************************************************************************
	 * 
	 * 										CONSTRUCTORS
	 * 
	 **************************************************************************************/

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
	
	/**************************************************************************************
	 * 
	 * 										GAME STATE
	 * 
	 **************************************************************************************/
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean fieldExists(int x, int y) {
		return x >= 0 &&  x < this.width && y >= 0 && y < this.height;
	}
	
	/**
	 * Get the field at the given position.
	 * @param x
	 * @param y
	 * @return
	 */
	public Field getField(int x, int y) {
		Preconditions.checkArgument( this.fieldExists(x, y), "Field at " + x + "/" +  y + " does not exist" );
		
		return this.fields[x][y];
	}
	
	public Field[][] getFields() {
		return fields;
	}
	
	/**
	 * Add a new room to the map.
	 * @return The newly created room.
	 */
	public Room addRoom() {
		Room room = new Room( this );
		this.rooms.add( room );
		
		return room;
	}
	
	public void addMapObject(MapObject object) throws GameStateException {
		// does this object fit on the map anyway?
		Preconditions.checkState( this.fieldExists( object.getX() + object.getWidth(), object.getY() + object.getHeight() ), "Can't place " + object.toString() + " cause it is to big for this map." );
		
		// check if we can place that object on the map! - fieldwise
		for (int x=object.getX(); x<object.getX() + object.getWidth(); x++ ){
			for ( int y=object.getY(); y<object.getY() + object.getHeight(); y++) {
				Field field = this.getField( x, y );
				
				// objects that block fields can't be placed if the field is already blocked!
				Preconditions.checkState( !field.isBlocked() || !object.blocksField(x, y), "Can't place " + object.toString() + " cause the field " + x + "/" + y + " is already blocked." );
			}
		}
		
		// Place the object on the map!
		for (int x=object.getX(); x<object.getX() + object.getWidth(); x++ ){
			for ( int y=object.getY(); y<object.getY() + object.getHeight(); y++) {
				this.getField( x, y ).objects.add( object );
			}
		}		
		
		this.objects.add( object );
	}	
	
	/**
	 * Add a new texture overlay to the map.
	 * @return The newly created texture overlay.
	 */
	public TextureOverlay addTextureOverlay() {
		TextureOverlay overlay = new TextureOverlay();
		this.textureOverlays.add( overlay );
		
		return overlay;
	}
	
	/**
	 * Get all rooms.
	 * @return
	 */
	public List<Room> getRooms() {
		return Collections.unmodifiableList( rooms );	
	}
	
	/**
	 * Get all map objects.
	 * @return
	 */
	public List<MapObject> getObjects() {
		return Collections.unmodifiableList( objects );	
	}
	
	/**
	 * Get all texture overlays.
	 * @return
	 */
	public List<TextureOverlay> getTextureOverlays() {
		return Collections.unmodifiableList( textureOverlays );
	}

	/**
	 * Get all units.
	 * @return
	 */
	public List<Unit> getUnits() {
        // TODO more efficient
    	ArrayList<Unit> units = new ArrayList<Unit>();
    	
    	for (Field[] fields : this.fields) {
			for(Field field : fields) {
				if ( field.hasUnit() ) {
					units.add( field.getUnit() );
				}
			}
		}
    	
    	return units;
	}
        
	/**
	 * Get all heroes.
	 * @return
	 */
    public List<Hero> getHeroes() {
        // TODO more efficient
    	ArrayList<Hero> heroes = new ArrayList<Hero>();
    	
    	for (Field[] fields : this.fields) {
			for(Field field : fields) {
				if ( field.hasUnit() && field.getUnit().isHero()) {
					heroes.add( (Hero) field.getUnit() );
				}
			}
		}
    	
    	return heroes;
    }
    
    /**
     * Get the first hero of the given type found.
     * @param type
     * @return {@code null} in case there is none.
     */
    public Hero getHero(HeroType type) {
    	List<Hero> heroes = this.getHeroes();
    	
    	for (Hero hero : heroes) {
    		if ( hero.getHeroType() == type ){
    			return hero;
    		}
    	}
    	
    	// not found
    	return null;
    }
    
    /**
     * Get all monsters.
     * @return
     */
    public List<Monster> getMonsters() {
        // TODO better
    	ArrayList<Monster> monsters = new ArrayList<Monster>();
    	
    	for (Field[] fields : this.fields) {
			for(Field field : fields) {
				if ( field.hasUnit() && field.getUnit().isMonster() ) {
					monsters.add( (Monster) field.getUnit() );
				}
			}
		}
    	
    	return monsters;
    }
    
    public void removeUnit(Unit unit) {
    	Preconditions.checkArgument( unit.getMap() == this, "Unit does not belong to this map!" );
    	
    	Field field = unit.getField();
    	
    	field.unit = null;
    	unit.field = null;
    	
    	this.fireOnUnitLeavesField( field );
    }
    
	/**************************************************************************************
	 * 
	 * 							      	GAME STATE - INTERNAL
	 * 
	 **************************************************************************************/
    
    
	/**************************************************************************************
	 * 
	 * 										OTHER METHODS
	 * 
	 **************************************************************************************/
    
	public GameContext getContext() {
		return context;
	}

	public void setContext(GameContext context) {
		this.context = context;
	}    
    
	@Override
	public void addListener(MapListener l) {
		this.listeners.addListener(l);
	}


	@Override
	public void removeListener(MapListener l) {
		this.listeners.removeListener(l);
	}
	
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
	
	void fireOnFieldRevealed(Field field) {
		for (MapListener l : this.listeners) {
			l.onFieldRevealed(field);
		}
	}
	
	void fireOnDoorOpened(Door door) {
		for (MapListener l : this.listeners) {
			l.onDoorOpened(door);
		}
	}
	
	void fireOnRoomRevealed(Room room) {
		for (MapListener l : this.listeners) {
			l.onRoomRevealed(room);
		}
	}
	
	void fireOnFieldTextureChanges(Field field) {
		for (MapListener l : this.listeners) {
			l.onFieldTextureChanges(field);
		}
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
