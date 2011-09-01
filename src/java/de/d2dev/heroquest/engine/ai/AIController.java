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
     * @return A list of {@code GameAction}s to perform in this turn.
     */
    public List<GameAction> getActions();
 
}

