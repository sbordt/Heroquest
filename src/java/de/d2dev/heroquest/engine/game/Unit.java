package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.ai.AIController;

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
	
	private Type type;
	
	private Field field;
	
	private Direction2D viewDir = Direction2D.UP;
	
	/**
	 * If the unit is under ai control, this {@link AIController} determines its actions.
	 * {@code null} if the unit is not controlled by an ai.
	 */
	private AIController aiController;

	public Unit(Field field, Type type) throws GameStateException {
		this.moveTo( field );
		
		this.type = type;
	}
	
	/**************************************************************************************
	 * 
	 * 										GAME STATE
	 * 
	 **************************************************************************************/
	
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
	
	public Direction2D getViewDir() {
		return viewDir;
	}
	
	public void setViewDir(Direction2D dir) {
		this.viewDir = dir;
	}
	
	public Type getType() {
		return this.type;
	}
	
	/**************************************************************************************
	 * 
	 * 										OTHER METHODS
	 * 
	 **************************************************************************************/
	
	/**
	 * 
	 * @return {@code null} if there is none.
	 */
	public AIController getAIController() {
		return this.aiController;
	}
	
	/**
	 * 
	 * @param aiController {@code null} to remove ai control.
	 */
	public void setAiController(AIController aiController) {
		this.aiController = aiController;
	}
}
