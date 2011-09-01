/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import java.util.List;

import de.d2dev.heroquest.engine.game.action.GameAction;

/**
 *
 * @author Simon
 */
public interface AIController {

    /**
     * 
     * @return A {@code GameAction} or {@code null} if there are no more
     * actions to execute in this turn!
     */
    public List<GameAction> getActions();
 
}
