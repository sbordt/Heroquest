/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai.astar;

import java.util.ArrayDeque;

/**
 *
 * @author Simon
 */
public interface Communicator {

    public ArrayDeque<Knot> getSuccessors(Knot knot);

    public int getTransitionCosts(Knot a, Knot b);

    
    
}
