package de.d2dev.heroquest.engine.game;

import java.util.List;
import java.util.Vector;

import nu.xom.Attribute;
import nu.xom.Element;
import de.d2dev.fourseasons.files.FileUtil;
import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.fourseasons.gamestate.Gamestate;
import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.types.TextureResource;

/**
 * Class for a field on a heroquest map. The following table shows how the different
 * values map to the different types of fields.
 * <br><br>
 * <table border="1">
 *  <tr>
 *   <th>Field Type</th><th>isWall</th><th>isDoor</th><th>isBlocked</th>
 *  </tr>
  *  <tr>
 *   <th>free field</th><th>false</th><th>false</th><th>false</th>
 *  </tr>
 *  <tr>
 *   <th>plain wall</th><th>true</th><th>false</th><th>true</th>
 *  </tr>
 *  <tr>
 *   <th>door</th><th>true</th><th>true</th><th>true</th>
 *  </tr>
 * </table>
 * @author Sebastian Bordt
 *
 */
public final class Field {
	
	private static final String FIELD = "field";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String REVEALED = "revealed";
	private static final String IS_WALL = "isWall";
	private static final String IS_DOOR = "isDoor";
	private static final String TEXTURE = "texture";
	
	/**
	 * The map the field belongs to.
	 */
	private Map map;
	
	/**
	 * The fields x-coordinate.
	 */
	private int x;
	
	/**
	 * The fields y-coordinate.
	 */
	private int y;
	
	/**
	 * Whether the field has been revealed. In game this value is initially usually
	 * false, then changes to true and can't be changed from thereon.
	 */
	private boolean revealed;
	
	/**
	 * Whether this field is a wall.
	 */
	private boolean isWall;
	
	/**
	 * A door that might be on the field. {@code null} if there is none.
	 */
	private Door door = null;
	
	/**
	 * A unit that might be standing on the field. Visible to the package because 
	 * the unit has the {@code setField} method.
	 */
	Unit unit = null;
	
	/**
	 * The fields texture. Simply the texture to be rendered when there is nothing on
	 * the field.
	 */
	private Resource texture;
	
	public Field(Map map, int x, int y) {
		this.map = map;
		
		this.x = x;
		this.y = y;
		
		this.revealed = false;
		
		this.isWall = false;
		
		this.texture = TextureResource.createTextureResource( "fields/default.jpg" );
	}
	
	/**
	 * Read from xml.
	 * @param xml
	 */
	public Field(Map map, Element xml) {
		if ( xml.getLocalName() != FIELD )	// verify parameter
			throw new IllegalArgumentException();
		
		this.map = map;
		
		this.x = FileUtil.readIntAttribute( xml, X );
		this.y = FileUtil.readIntAttribute( xml, Y );
		this.revealed = FileUtil.readBoleanAttribute( xml, REVEALED );
		this.isWall = FileUtil.readBoleanAttribute( xml, IS_WALL);
		//this.isDoor = FileUtil.readBoleanAttribute( xml, IS_DOOR);
		this.texture = TextureResource.createTextureResource( FileUtil.readStringAttribute( xml, TEXTURE ) );
	}
	
	/**************************************************************************************
	 * 
	 * 										GAME STATE
	 * 
	 **************************************************************************************/
	
	/**
	 * Getter for the map the fields belongs to.
	 * @return
	 */
	public Map getMap() {
		return map;
	}
	
	/**
	 * Getter for the fields x-coord.
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Getter for the fields y-coord.
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Get the field on top of the given field.
	 * @return {@code null} in case there is none - field is in upper row.
	 */
	public Field getUpperField() {
		// there is no upper field if we are in the upper row
		if ( this.y == 0 )
			return null;
		
		return this.map.getField( this.x, this.y -1 );
	}
	
	/**
	 * Get the field left to the given field.
	 * @return {@code null} in case there is none - field is in left column.
	 */
	public Field getLeftField() {
		// there is no left field if we are in the left column
		if ( this.x == 0 )
			return null;
		
		return this.map.getField( this.x -1, this.y );
	}
	
	/**
	 * Get the field under to the given field.
	 * @return {@code null} in case there is none - field is in lower row.
	 */
	public Field getLowerField() {
		// there is no lower field if we are in the lower row
		if ( this.y == this.map.getHeight()-1 )
			return null;
		
		return this.map.getField( this.x, this.y +1 );
	}
	
	/**
	 * Get the field right to the given field.
	 * @return {@code null} in case there is none - field is in right column.
	 */
	public Field getRightField() {
		// there is no right field if we are in the right column
		if ( this.x == this.map.getWidth()-1 )
			return null;		
		return this.map.getField( this.x +1, this.y );
	}
	
	/**
	 * Get a {@code List} containing the fields direct neighbours,
	 * that are the (at most) 4 fields directly next to the field.
	 * There might be fewer than 4 direct neighbours, e.g. in case of the 
	 * maps upper left corner.
	 * @return
	 */
	public List<Field> getNeighbours() {
		List<Field> neighbours = new Vector<Field>();
		
		Field field;
		
		if ( ( field = this.getUpperField() ) != null )
			neighbours.add( field );
		if ( ( field = this.getLeftField() ) != null )
			neighbours.add( field );
		if ( ( field = this.getLowerField() ) != null )
			neighbours.add( field );
		if ( ( field = this.getRightField() ) != null )
			neighbours.add( field );
		
		return neighbours;
	}
	
	/**
	 * Is the field revealed?
	 * @return
	 */
	public boolean isRevealed() {
		return revealed;
	}
	
	/**
	 * Is this field a wall?
	 * @return
	 */
	public boolean isWall() {
		return isWall;
	}
	
	/**
	 * Make this field a wall or remove a wall.
	 * @param wall
	 * @throws GameStateException
	 */
	public void setWall(boolean wall) throws GameStateException {
		if ( wall ) {	// attempt to create a wall
			Gamestate.checkState( !this.hasUnit(), "Attempt to set a wall on a unit field." );
			
			this.isWall = true;
		} else {	// attempt to remove a wall
			Gamestate.checkState( this.door != null, "Attempt to make a door-field a non-wall field." );
				
			this.isWall = false;
		}
	}
	
	/**
	 * Is this field a door?
	 * @return
	 */
	public boolean isDoor() {
		return door != null;
	}
	
	/**
	 * Getter for the fields door.
	 * @return
	 */
	public Door getDoor() {
		return door;
	}
	
	/**
	 * Make this field a door or remove a door.
	 * @param door
	 * @throws GameStateException
	 */
	public void setDoor(boolean door) throws GameStateException {
		// nothing to do
		if ( door == this.isDoor() )
			return;
		
		// attempt to create a door
		if ( door ) {
			Gamestate.checkState( this.isWall, "Attempt to set a door on a non-wall field." );
			
			this.door = new Door( this );
		} else {	// attempt to remove a door
			this.door = null;
		}
	}

	/**
	 * Whether the field is blocked. Units can't move to
	 * blocked fields, but to free ones.
	 * @return
	 */
	public boolean isBlocked() {
		if ( this.unit != null )	// units block the field
			return true;
		
		// walls block fields - but OPEN door let u pass!
		if ( this.isWall ) {
			if ( !this.isDoor() || this.door.isClosed() ) {
				return true;
			}
		}
		
		// finally a free field!
		return false;
	}
	
	/**
	 * Is some unit standing on the field? Maximum of one unit
	 * standing on a field. 
	 * @return
	 */
	public boolean hasUnit() {
		return this.unit != null;
	}
	
	/**
	 * Getter for the unit standing on the field.
	 * @return {@code null} in case there is no unit standing on the field.
	 */
	public Unit getUnit() {
		return this.unit;
	}
	
	/**
	 * Getter for 'the' fields texture. Each field must have at least one
	 * texture. (How should it otherwise be rendered with no objects on it?)
	 * @return
	 */
	public Resource getTexture() {
		return texture;
	}
	
	public void setTexture(Resource texture) {
		this.texture = texture;
		
		this.map.fireOnFieldTextureChanges( this );	// fire event
	}
	
	/**************************************************************************************
	 * 
	 * 										OTHER METHODS
	 * 
	 **************************************************************************************/
	
	public Element toXML() {
		Element xml = new Element( FIELD );
		
		xml.addAttribute( new Attribute( X, Integer.toString( this.x ) ) );
		xml.addAttribute( new Attribute( Y, Integer.toString( this.y ) ) );
		xml.addAttribute( new Attribute( REVEALED, Boolean.toString( this.revealed ) ) );
		xml.addAttribute( new Attribute( IS_WALL, Boolean.toString( this.isWall ) ) );
		xml.addAttribute( new Attribute( IS_DOOR, Boolean.toString( this.door != null ) ) );
		xml.addAttribute( new Attribute( TEXTURE, this.texture.getName() ) );
		
		return xml;
	}

	@Override
	public String toString() {
		return "Field [map=" + map + ", x=" + x + ", y=" + y + ", revealed="
				+ revealed + ", isWall=" + isWall + ", door=" + door
				+ ", unit=" + unit + ", texture=" + texture + "]";
	}
	
}
