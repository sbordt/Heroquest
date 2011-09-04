package de.d2dev.heroquest.engine.game;

import java.util.ArrayList;
import java.util.List;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.fourseasons.gamestate.Gamestate;

/**
 * A room is essentially an abstraction for a number of fields that should be revealed when a
 * door is opened. So our rooms consist of a given number of fields and doors. <br>
 * There is no such thing as 'rooms must be rectangular' even 'rooms must be surrounded by walls', 
 * but each field can only belong to at most one room. Fields that don't belong to any room are passage fields.<br>
 * A door is said to belong to a room if and only if either its field belongs to the room or one of the two fields
 * from where a door can be opened (depending on horizontal/vertical doors).
 * @author Sebastian Bordt
 *
 */
public class Room {
	
	private Map map;
	
	/**
	 * The rooms fields. Visible to the package because {@link Field}s add/remove themselves.
	 */
	List<Field> fields = new ArrayList<Field>();
	
	/**
	 * Only visible to the package as rooms are created by {@link Map#addRoom()}.
	 */
	Room(Map map) {
		this.map = map;
	}
	
	/**
	 * The map the room belongs to.
	 * @return
	 */
	public Map getMap() {
		return map;
	}
	
	/**
	 * Add a field to the room. Differently from {@link Field#setRoom(Room)} this method throws an exception 
	 * if the field already belongs to a room.
	 * @param field
	 * @throws GameStateException
	 */
	public void addField(Field field) throws GameStateException {		
		// nothing to do
		if ( field.getRoom() == this )
			return;
		
		Gamestate.checkState( !field.belongsToRoom(), "Attempt to add a field to a room that alread belongs to another room :" + field.toString() );
	
		field.setRoom( this );
	}

	/**
	 * The fields that belong to the room.
	 * @return
	 */
	public List<Field> getFields() {
		return this.fields;
	}	
	
	/**
	 * The doors that belong to the room, i.e. reveal the room.
	 * @return
	 */
	public List<Door> getDoors() {
		return null;	// TODO
	}

}
