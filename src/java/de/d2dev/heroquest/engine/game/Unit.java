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
	
	private ViewDirection viewDir = ViewDirection.UP;

	public Unit(Field field, Type type) throws GameStateException {
		this.moveTo( field );
		
		this.type = type;
	}
	
	/**
	 * The field the unit is standing on.
	 * @return
	 */
	public Field getField() {
		return this.field;
	}
	
	/**
	 * Move the unit to another field.
	 */
	public void moveTo(Field field) throws GameStateException {
		if ( field.isBlocked() )	// validity
			throw new GameStateException( "Attempt to place a unit on a blocked field." );
		
		if ( this.field != null ) {	// this is not the case when the unit is first placed on the map
			this.field.getMap().fireOnUnitLeavesField(field);
			this.field.unit = null;
		}
		
		this.field = field;
		field.unit = this ;
		
		this.field.getMap().fireOnUnitEntersField(field);
	}
	
	public ViewDirection getViewDir() {
		return viewDir;
	}
	
	public void setViewDir(ViewDirection dir) {
		this.viewDir = dir;
	}
	
	public Type getType() {
		return this.type;
	}
}
