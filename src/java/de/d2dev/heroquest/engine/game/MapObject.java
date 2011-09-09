package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.resource.Resource;

/**
 * {@code MapObject} is the abstract superclass for all kinds of objects on a {@link Map} 
 * (Note: Some build in objects like {@link Unit}s or {@link Door}s are currently no {@code MapObject}s). 
 * A {@code MapObject} has a rectangular bounding (a given number of {@link Field}s) and can be positioned on the map. Within its
 * bounding, the map object assigns properties to each field it lies on. The simplest property
 * a {@code MapObject} can assign to a field is to block it. The properties a {@code MapObject} assigns to its fields
 * also determine whether it can be placed at a given position, lying on a given number of fields. For example, a {@code MapObject}
 * that blocks a given field can't be placed if the field is already blocked!<br>
 * <br>
 * {@code MapObject}s specify the properties they assign to fields in object space. In object space, the upper left field is the origin (0/0).
 * Coordinates increase along the positive x- and the negative y-axis. Position and rotation determine the objects positioning on the map ('world space').
 * Translations/rotations and the order in which they are performed do NOT effect each other (or rotation before translation).
 * Translation moves the objects origin (the upper left field). Rotation does not rotate the object around the origin 
 * but around its center. After that, the object is realigned to the x- and y-axis, 
 * to make the origin its upper left field again.
 * @author Sebastian Bordt
 *
 */
public abstract class MapObject {
	
	protected Map map;
	
	/**
	 * The x-coordinate of the objects upper left field.
	 */
	protected int x;
	
	/**
	 * The y-coordinate of the objects upper left field.
	 */
	protected int y;
	
	/**
	 * The objects rotation.
	 */
	protected Rotation2D rotation;
	
	/**
	 * Bounding width in fields.
	 */
	protected int width;
	
	/**
	 * Bounding height in fields.
	 */
	protected int height;
	
	/**
	 * A texture for the objects bounding. May be {@code null}.
	 */
	protected Resource texture;
	
	
	
	public MapObject(Map map, int x, int y, Rotation2D rotation, int width,
			int height, Resource texture) {
		super();
		this.map = map;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.width = width;
		this.height = height;
		this.texture = texture;
	}

	/**************************************************************************************
	 * 
	 * 								 		GAME STATE
	 * 
	 **************************************************************************************/	
	
	public Map getMap() {
		return map;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Rotation2D getRotation() {
		return rotation;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Resource getTexture() {
		return texture;
	}	
	
	public boolean blocksField(int x, int y) {
		return this.blocksFieldObjectSpace( this.xCoordToObjectSpace(x), this.yCoordToObjectSpace(y) );
	}

	protected abstract boolean blocksFieldObjectSpace(int x, int y);
	
	/**************************************************************************************
	 * 
	 * 							GAME EVENTS THE OBJECT CAN REACT ON
	 * 
	 **************************************************************************************/
	
	public void onUnitEntersField(int x, int y) {};
	public void onUnitLeavesField(int x, int y) {};
	
	/**************************************************************************************
	 * 
	 * 										OTHER METHODS
	 * 
	 **************************************************************************************/
	
	protected int xCoordToObjectSpace(int x) {
		x -= this.x;
		
		return x;
	}
	
	protected int yCoordToObjectSpace(int y) {
		y -= this.y;
		
		return y;
	}
	
}
