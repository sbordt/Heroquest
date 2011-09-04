package de.d2dev.heroquest.engine.game;

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
	 * The number of rooms to be revealed when this door is opened.
	 * @return
	 */
	public List<Room> getRooms() {
		return null;	// TODO
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
		}
	}
}
