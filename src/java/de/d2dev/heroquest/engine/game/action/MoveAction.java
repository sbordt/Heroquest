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
public class MoveAction implements GameAction {

    Unit unit;
    Direction2D direction;

    public MoveAction(Unit unit, Direction2D direction) {
        this.unit = unit;
        this.direction = direction;
    }
    
    @Override
    public void excecute() throws GameStateException {
    	switch ( this.direction ) {
    	case UP:
    		this.unit.moveUp();
    		break;
    	case LEFT:
    		this.unit.moveLeft();
    		break;
    	case RIGHT:
    		this.unit.moveRight();
    		break;
    	case DOWN:
    		this.unit.moveDown();
    		break;
    	}
    }
}
