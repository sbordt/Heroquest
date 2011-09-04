package de.d2dev.heroquest.engine.game;

import java.util.ArrayList;
import java.util.List;

public class Door {
	
	private Field field;
	
	private boolean isOpen;
	
	Door(Field field) {
		this.field = field;
		
		this.isOpen = false;
	}
	
	/**
	 * The map the door belongs to.
	 * @return
	 */
	public Map getMap() {
		return this.field.getMap();
	}
	
	/**
	 * The field the door is standing on.
	 * @return
	 */
	public Field getField() {
		return this.field;
	}

	public boolean isOpen() {
		return this.isOpen;
	}
	
	public boolean isClosed() {
		return !this.isOpen;
	}
	
	/**
	 * Is this a vertical door?
	 * @return
	 */
	public boolean isVertical() {
		return field.getUpperField().isWall() && field.getLowerField().isWall() && !field.getLeftField().isWall() && !field.getRightField().isWall();
	}
	
	/**
	 * Is this a horizontal door?
	 * @return
	 */
	public boolean isHorizontal() {
		return !field.getUpperField().isWall() && !field.getLowerField().isWall() && field.getLeftField().isWall() && field.getRightField().isWall();
	}
	
	/**
	 * The number of rooms to be revealed when this door is opened.
	 * @return
	 */
	public List<Room> getRooms() {
		// works for vertical and horizontal doors
		List<Room> rooms = new ArrayList<Room>();
		
		// vertical doors - reveal rooms that belong to the doors left and right field
		if ( this.isVertical() ) {
			if ( field.getLeftField().belongsToRoom() ) {
				rooms.add( field.getLeftField().getRoom() );
			}
			
			if ( field.getRightField().belongsToRoom() ) {
				rooms.add( field.getRightField().getRoom() );
			}
		} 
		// horizontal doors - reveal rooms that belong to the doors upper and lower field
		else if ( this.isHorizontal() ) {
			if ( field.getUpperField().belongsToRoom() ) {
				rooms.add( field.getUpperField().getRoom() );
			}
			
			if ( field.getLowerField().belongsToRoom() ) {
				rooms.add( field.getLowerField().getRoom() );
			}
		}
		
		return rooms;
	}
	
	/**
	 * Opens the door. Doors can be opened exactly one and then they stay open.
	 */
	public void open() {
		// a door is to be opened only once!
		if ( !this.isOpen ) {
			// open the door and fire the event
			this.isOpen = true;
			
			this.field.getMap().fireOnDoorOpened( this );
			
			// reveal all rooms that belong to the door!
			for (Room room : this.getRooms()) {
				room.reveal();
			}
		}
	}
}
