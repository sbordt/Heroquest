/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai.astar;

import de.d2dev.heroquest.engine.game.Field;
import java.util.LinkedList;

/**
 *
 * @author Simon
 */
public interface Communicator {

    public LinkedList<Knot> getSuccessors(Field field);

    public int getTransitionCosts(Knot a, Knot b);

    
    
}
