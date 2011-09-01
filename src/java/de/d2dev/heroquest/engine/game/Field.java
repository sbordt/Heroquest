package de.d2dev.heroquest.engine.game;

import nu.xom.Attribute;
import nu.xom.Element;
import de.d2dev.fourseasons.files.FileUtil;
import de.d2dev.fourseasons.gamestate.GameStateException;
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
	 * Whether this field is a door. A door is required to be a wall.
	 */
	private boolean isDoor;
	
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
		this.isDoor = false;
		
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
		this.isDoor = FileUtil.readBoleanAttribute( xml, IS_DOOR);
		this.texture = TextureResource.createTextureResource( FileUtil.readStringAttribute( xml, TEXTURE ) );
	}
	
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
	
	public void setWall(boolean wall) throws GameStateException {
		if ( this.isDoor && !wall )	// validity
			throw new GameStateException( "Attempt to make a door-field a non-wall field." );
		
		this.isWall = wall;
	}
	
	/**
	 * Is this field a door?
	 * @return
	 */
	public boolean isDoor() {
		return isDoor;
	}
	
	public void setDoor(boolean door) throws GameStateException {
		if ( !this.isWall && door )	// validity
			throw new GameStateException( "Attempt to set a door on a non-wall field." );
		
		this.isDoor = door;
	}
	
	public boolean isBlocked() {
		if ( this.unit != null )	// actors block the field
			return true;
		
		//TODO doors
		if ( this.isWall )
			return true;
		
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
	}
	
	public Element toXML() {
		Element xml = new Element( FIELD );
		
		xml.addAttribute( new Attribute( X, Integer.toString( this.x ) ) );
		xml.addAttribute( new Attribute( Y, Integer.toString( this.y ) ) );
		xml.addAttribute( new Attribute( REVEALED, Boolean.toString( this.revealed ) ) );
		xml.addAttribute( new Attribute( IS_WALL, Boolean.toString( this.isWall ) ) );
		xml.addAttribute( new Attribute( IS_DOOR, Boolean.toString( this.isDoor ) ) );
		xml.addAttribute( new Attribute( TEXTURE, this.texture.getName() ) );
		
		return xml;
	}
}
