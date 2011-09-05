package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.ai.astar.Communicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.game.Field;
import java.util.ArrayDeque;

/**
 *
 * @author Simon
 */
public class SearchKnot implements Knot {

    private Field field;
    private int heuristic;
    private Communicator communicator;
    private int costs;

    public SearchKnot(Field field, int heuristic, Communicator communicator) {
        this.field = field;
        this.heuristic = heuristic;
        this.communicator = communicator;
        this.costs = -1;

    }
    
    
    
    @Override
    public ArrayDeque<Knot> getSuccessors() {
        return communicator.getSuccessors(this);
    }

    @Override
    public int getHeuristic() {
        return heuristic;
    }

    public Field getField() {
        return field;
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
        if (this.field != other.field && (this.field == null || !this.field.equals(other.field))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.field != null ? this.field.hashCode() : 0);
        return hash;
    }

    
    @Override
    public int getTransitionCosts(Knot b) {
//        if (b == null) {
//            throw new NullPointerException("getTransitionCosts: Knot is null");
//        }
//        return communicator.getTransitionCosts(this,b);
        return 1;
    }

    @Override
    public int getCosts() {
        return costs;
    }

    @Override
    public void setCosts(int costs) {
        this.costs = costs;
    }
}
