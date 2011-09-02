package de.d2dev.heroquest.engine.ai.astar;
import java.util.LinkedList;


/**
 * Pfad Object for AStar search
 */
public interface Knot{

    /**
     * Gives back the Successors of a Knot
     * @return a Stack with the Successors
     */
    public LinkedList<Knot> getSuccessors();
    
    /**
     * Gives the estimated distance to the goal from this knot
     * @return heuristic
     */
    public int getHeuristic();
    
    
    /**
     * Returns the costs to get from this knot to a knot b
     * @return 
     */
    public int getTransitionCosts(Knot b);
    
    
}