package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.ai.astar.Communicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import java.util.Stack;

/**
 *
 * @author Simon
 */
public class SearchKnot implements Knot {

    private int x;
    private int y;
    private int heuristic;
    private Communicator communicator;

    public SearchKnot(int x, int y, int heuristic, Communicator communicator) {
        this.x = x;
        this.y = y;
        this.heuristic = heuristic;
        this.communicator = communicator;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Stack<Knot> getSuccessors() {
        return communicator.getSuccessors(x, y);
    }

    @Override
    public int getHeuristic() {
        return heuristic;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SearchKnot other = (SearchKnot) obj;
        if (this.x != other.getX()) {
            return false;
        }
        if (this.y != other.getY()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }

    @Override
    public int getTransitionCosts(Knot b) {
        if (b == null) {
            throw new NullPointerException("getTransitionCosts: Knot is null");
        }
        return communicator.getTransitionCosts(this,b);
    }
}
