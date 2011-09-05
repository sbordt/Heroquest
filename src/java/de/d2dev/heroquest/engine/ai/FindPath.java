/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.ai.astar.AbstractCommunicator;
import de.d2dev.heroquest.engine.ai.astar.Communicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import java.util.ArrayDeque;

/**
 *
 * @author Simon + Toni
 */
public class FindPath extends AbstractCommunicator implements Communicator {



    public FindPath(Map map) {
        super(map);
    }



//***********InterFace Communicator*******************
    @Override
    public int getTransitionCosts(Knot a, Knot b) {
        return 1;
    }

    @Override
    public ArrayDeque<Knot> getSuccessors(Knot actual) {
        ArrayDeque<Knot> speicher = new ArrayDeque<Knot>();

        for (Field f : ((SearchKnot) actual).getField().getNeighbours()) {
            if (!f.isBlocked()) {
                speicher.add(getKnot(f));
            } else if ((f.getX() == getGoalX() && f.getY() == getGoalY())) {
                speicher.add(getKnot(f));
            }

        }

        return speicher;
    }

    @Override
    public int getHeuristic(Field field) {
        return Math.abs(getGoalX() - field.getX()) + Math.abs(getGoalY() - field.getY());
    }
}
