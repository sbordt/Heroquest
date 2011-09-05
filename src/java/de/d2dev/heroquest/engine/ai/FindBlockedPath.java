/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.ai.astar.AbstractCommunicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import java.util.ArrayDeque;

/**
 *
 * @author Simon + Toni
 */
public class FindBlockedPath extends AbstractCommunicator {

    public FindBlockedPath(Map map) {
        super(map);
    }

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
            } else if ((f.getX() == getGoalX() && f.getY() == getGoalY()) || f.hasUnit()) {
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
