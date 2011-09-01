package de.d2dev.heroquest.engine.game;

import com.google.common.base.Preconditions;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.fourseasons.gamestate.Gamestate;
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
	
	private Map map;
	
	private Field field;
	
	private Direction2D viewDir = Direction2D.UP;
	
	/**
	 * If the unit is under ai control, this {@link AIController} determines its actions.
	 * {@code null} if the unit is not controlled by an ai.
	 */
	private AIController aiController;

	public Unit(Field field, Type type) throws GameStateException {
		Preconditions.checkNotNull( field );	// the field must not be null
		
		this.map = field.getMap();
		this.moveTo( field );
		
		this.type = type;
	}
	
	/**************************************************************************************
	 * 
	 * 										GAME STATE
	 * 
	 **************************************************************************************/
	
	/**
	 * The map the unit belongs to.
	 * @return
	 */
	public Map getMap() {
		return this.map;
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
		Gamestate.checkState( !field.isBlocked() , "Attempt to place a unit on a blocked field." );
		
		if ( this.field != null ) {	// this is not the case when the unit is first placed on the map
			this.field.getMap().fireOnUnitLeavesField(field);
			this.field.unit = null;
		}
		
		this.field = field;
		field.unit = this ;
		
		this.field.getMap().fireOnUnitEntersField(field);
	}
	
	/**
	 * Can the unit move one field up?
	 * @return
	 */
	public boolean canMoveUp() {
		// unit can't move up if there is no upper field or if the upper field is blocked
		if ( this.field.getUpperField() == null || this.field.getUpperField().isBlocked() )
			return false;
			
		return true;
	}
	
	/**
	 * Can the unit move one field to the left?
	 * @return
	 */
	public boolean canMoveLeft() {
		// unit can't move left if there is no left field or if the left field is blocked
		if ( this.field.getLeftField() == null || this.field.getLeftField().isBlocked() )
			return false;
			
		return true;
	}
	
	/**
	 * Can the unit move one field to the right?
	 * @return
	 */
	public boolean canMoveRight() {
		// unit can't move right if there is no right field or if the right field is blocked
		if ( this.field.getRightField() == null || this.field.getRightField().isBlocked() )
			return false;
			
		return true;		
	}
	
	/**
	 * Can the unit move one field down?
	 * @return
	 */
	public boolean canMoveDown() {
		// unit can't move down if there is no lower field or if the lower field is blocked
		if ( this.field.getLowerField() == null || this.field.getLowerField().isBlocked() )
			return false;
			
		return true;
	}
	
	/**
	 * Move the unit one field up.
	 * @throws GameStateException
	 */
	public void moveUp() throws GameStateException {
		Gamestate.checkState( this.canMoveUp(), "Unit can't move up." );
		
		this.moveTo( this.field.getUpperField() );
	}
	
	/**
	 * Move the unit one field to the left.
	 * @throws GameStateException
	 */
	public void moveLeft() throws GameStateException {
		Gamestate.checkState( this.canMoveLeft(), "Unit can't move left." );
		
		this.moveTo( this.field.getLeftField() );
	}
	
	/**
	 * Move the unit one field to the right.
	 * @throws GameStateException
	 */
	public void moveRight() throws GameStateException {
		Gamestate.checkState( this.canMoveRight(), "Unit can't move right." );
		
		this.moveTo( this.field.getRightField() );
	}	
	
	/**
	 * Move the unit one field down.
	 * @throws GameStateException
	 */
	public void moveDown() throws GameStateException {
		Gamestate.checkState( this.canMoveDown(), "Unit can't move dowm." );
		
		this.moveTo( this.field.getLowerField() );
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
