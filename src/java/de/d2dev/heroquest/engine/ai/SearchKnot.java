package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.ai.astar.Communicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.game.Field;
import java.util.LinkedList;

/**
 *
 * @author Simon
 */
public class SearchKnot implements Knot {

    private Field field;
    private int heuristic;
    private Communicator communicator;
    private int x;
    private int y;

    public SearchKnot(Field field, int heuristic, Communicator communicator) {
        this.field = field;
        this.heuristic = heuristic;
        this.communicator = communicator;
        this.x = field.getX();
        this.y = field.getY();
    }

    public SearchKnot(int heuristic, Communicator communicator, int x, int y) {
        this.field = null;
        this.heuristic = heuristic;
        this.communicator = communicator;
        this.x = x;
        this.y = y;
    }
    

    public Field getField() {
        return field;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
    
    
    @Override
    public LinkedList<Knot> getSuccessors() {
        return communicator.getSuccessors(this);
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
        if (b == null) {
            throw new NullPointerException("getTransitionCosts: Knot is null");
        }
        return communicator.getTransitionCosts(this,b);
    }
}
