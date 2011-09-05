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
public abstract class Unit {
	
	/**
	 * So far there are only heroes and monsters...
	 * @author Sebastian Bordt
	 *
	 */
	protected enum Type {
		HERO,
		MONSTER,
	}
	
	/*
	 * Game State Variables
	 */
	
	protected Map map;
	
	/**
	 * The units type, e.g. hero or monster!
	 */
	protected Type type;
	
	/**
	 * The units name, e.g. 'Barbarian' or 'Orc'
	 */
	protected String name;
	
	protected Field field;
	
	protected Direction2D viewDir = Direction2D.UP;
	
	/*
	 * Running Game Variables
	 */
	protected GameContext gameContext;

	protected int remainingMoves = 0;
	
	/*
	 * Classical fight system
	 */
	int bodyForce;
	int intelligence;
	int numAttackDices;
	int numDefenseDices;

	/**
	 * If the unit is under ai control, this {@link AIController} determines its actions.
	 * {@code null} if the unit is not controlled by an ai.
	 */
	protected AIController aiController;

	protected Unit(Field field) throws GameStateException {
		Preconditions.checkNotNull( field );	// the field must not be null
		
		this.map = field.getMap();
		this.moveTo( field );
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
	 * Is this unit a hero?
	 * @return
	 */
	public boolean isHero() {
		return this.type == Type.HERO;
	}
	
	/**
	 * Is this unit a monster?
	 * @return
	 */
	public boolean isMonster() {
		return this.type == Type.MONSTER;
	}
	
	/**
	 * The units name, further describing its type, e.g.
	 * 'Barbarian' or 'Orc'.
	 * @return
	 */
	public String getName() {
		return this.name;
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
		field.unit = this;
		
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
	
	/**
	 * The direction the unit views into.
	 * @return
	 */
	public Direction2D getViewDir() {
		return viewDir;
	}
	
	/**
	 * Set the direction the unit views into.
	 * @param dir
	 */
	public void setViewDir(Direction2D dir) {
		this.viewDir = dir;
	}
	
	/**
	 * Get the next field in the units viewing direction.
	 * @return {@code null} in case it does not exist - the unit views out of the map.
	 */
	public Field getViewField() {
		switch (this.viewDir) {
		case DOWN:
			return this.field.getLowerField();
		case LEFT:
			return this.field.getLeftField();
		case RIGHT:
			return this.field.getRightField();
		default:	// UP
			return this.field.getUpperField();
		}
	}
	
	/**************************************************************************************
	 * 
	 * 							      RUNNING GAMES STATE
	 * 
	 **************************************************************************************/

	public GameContext getGameContext() {
		return gameContext;
	}

	public void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
	}
	
	public int getRemainingMoves() {
		return this.remainingMoves;
	}
	
	public void setRemainingMoves(int remainingMoves) {
		this.remainingMoves = remainingMoves;
	}
	
	/**************************************************************************************
	 * 
	 * 						      CLASSICAL FIGHT SYSTEM
	 * 
	 **************************************************************************************/
	
	public int getBodyForce() {
		return bodyForce;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public int getNumAttackDices() {
		return numAttackDices;
	}

	public int getNumDefenseDices() {
		return numDefenseDices;
	}
	
	
	/**************************************************************************************
	 * 
	 * 						           GAME METHODS
	 * 
	 **************************************************************************************/

	
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
