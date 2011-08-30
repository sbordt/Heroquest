package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;

/**
 * Superclass for Heroes and Monsters. Can stand on fields and move.
 * @author Sebastian Bordt
 *
 */
public class Unit {
	
	public enum Type {
		HERO,
		MONSTER,
	}
	
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
	
	private Type type;
	
	private Field field;
	
	private ViewDirection viewDir;

	public Unit(Field field, Type type) throws GameStateException {
		this.setField( field );
		
		this.type = type;
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
		
		if ( this.field != null )
			this.field.unit = null;
		
		this.field = field;
		field.unit = this;
	}
	
	public ViewDirection getViewDir() {
		return viewDir;
	}
	
	public Type getType() {
		return this.type;
	}
}
