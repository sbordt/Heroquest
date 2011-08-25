package de.d2dev.heroquest.engine;

import nu.xom.Attribute;
import nu.xom.Element;
import de.d2dev.fourseasons.files.FileUtil;
import de.d2dev.fourseasons.game.GameObjectAdapter;
import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.types.TextureResource;

/**
 * Class for a field on a heroquest map. The following table shows how the different
 * values map to the different types of fields.
 * <br><br>
 * <table border="1">
 *  <tr>
 *   <th>Field Type</th><th>isWall</th><th>isDoor</th>
 *  </tr>
 *  <tr>
 *   <th>plain wall</th><th>true</th><th>false</th>
 *  </tr>
 *  <tr>
 *   <th>door</th><th>true</th><th>true</th>
 *  </tr>
 * </table>
 * @author Sebastian Bordt
 *
 */
public class Field extends GameObjectAdapter {
	
	private static final String FIELD = "field";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String REVEALED = "revealed";
	private static final String IS_WALL = "isWall";
	private static final String IS_DOOR = "isDoor";
	private static final String TEXTURE = "texture";
	
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
	
	private boolean isWall;
	private boolean isDoor;
	
	private Resource texture;
	
	public Field(int x, int y) {
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
	public Field(Element xml) {
		if ( xml.getLocalName() != FIELD )	// verify parameter
			throw new IllegalArgumentException();
		
		this.x = FileUtil.readIntAttribute( xml, X );
		this.y = FileUtil.readIntAttribute( xml, Y );
		this.revealed = FileUtil.readBoleanAttribute( xml, REVEALED );
		this.isWall = FileUtil.readBoleanAttribute( xml, IS_WALL);
		this.isDoor = FileUtil.readBoleanAttribute( xml, IS_DOOR);
		this.texture = TextureResource.createTextureResource( FileUtil.readStringAttribute( xml, TEXTURE ) );
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isRevealed() {
		return revealed;
	}
	
	public boolean isWall() {
		return isWall;
	}
	
	public void setWall(boolean w) {
		this.isWall = w;
	}
	
	public boolean isDoor() {
		return isDoor;
	}
	
	public void setDoor() {
		// TODO
	}
	
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
