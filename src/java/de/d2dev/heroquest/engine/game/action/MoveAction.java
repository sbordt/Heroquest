/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.game.action;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.game.Direction2D;
import de.d2dev.heroquest.engine.game.Unit;

/**
 *
 * @author Simon
 */
public class MoveAction extends GameActionAdapter {

	/**
	 * Direction to move into.
	 */
    Direction2D direction;
    
    public MoveAction(Unit unit, Direction2D direction) {
		super(unit);
		
		this.direction = direction;
	}

	@Override
    public void excecute() throws GameStateException {
    	switch ( this.direction ) {
    	case UP:
    		this.unit.moveUp();
    		this.unit.setViewDir( Direction2D.UP );
    		break;
    	case LEFT:
    		this.unit.moveLeft();
    		this.unit.setViewDir( Direction2D.LEFT );
    		break;
    	case RIGHT:
    		this.unit.moveRight();
    		this.unit.setViewDir( Direction2D.RIGHT );
    		break;
    	case DOWN:
    		this.unit.moveDown();
    		this.unit.setViewDir( Direction2D.DOWN );
    		break;
    	}
    }
}
