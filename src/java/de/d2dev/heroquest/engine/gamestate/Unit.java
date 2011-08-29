package de.d2dev.heroquest.engine.gamestate;

import de.d2dev.fourseasons.gamestate.GameStateException;

/**
 * Superclass for Heroes and Monsters. Can stand on fields and move.
 * @author Sebastian Bordt
 *
 */
public abstract class Unit {
	
	/**
	 * The direction in witch a unit views. Rather for the rendering
	 * than for our game logic. {@code UP} is the default viewing direction
	 * for units :-)
	 * @author Sebastian Bordt
	 *
	 */
	public enum ViewDirection {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	private Field field;
	
	private ViewDirection viewDir;

	Unit(Field field) throws GameStateException {
		this.setField( field );
		
		this.viewDir = ViewDirection.UP;
	}
	
	/**
	 * The field the unit is standing on.
	 * @return
	 */
	public Field getField() {
		return this.field;
	}
	
	/**
	 * Set the unit to stand on a specific field.
	 */
	public void setField(Field field) throws GameStateException {
		if ( field.isBlocked() )	// validity
			throw new GameStateException( "Attempt to place a unit on a blocked field." );
		
		this.field = field;
		field.unit = this;
	}
	
	public ViewDirection getViewDir() {
		return viewDir;
	}
}
