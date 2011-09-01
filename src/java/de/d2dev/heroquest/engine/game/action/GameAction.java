/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.game.action;

import de.d2dev.fourseasons.gamestate.GameStateException;

/**
 *
 * @author Simon
 */
public interface GameAction {
    
    public void excecute() throws GameStateException ;
}
