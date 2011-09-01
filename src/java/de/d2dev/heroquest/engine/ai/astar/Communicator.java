/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai.astar;

import de.d2dev.heroquest.engine.ai.SearchKnot;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Simon
 */
public interface Communicator {

    public Stack<Knot> getSuccessors(int x, int y);

    public int getTransitionCosts(Knot a, Knot b);

    
    
}
