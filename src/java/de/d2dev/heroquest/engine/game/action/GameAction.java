/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.game.action;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.game.RunningGameContext;
import de.d2dev.heroquest.engine.game.Unit;

/**
 *
 * @author Simon
 */
public interface GameAction {

	public RunningGameContext getGameContext();
	
	public Unit getActingUnit();
    
    public void excecute() throws GameStateException;
}
