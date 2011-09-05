package de.d2dev.heroquest.engine.ai.astar;
import java.util.ArrayDeque;


/**
 * Pfad Object for AStar search
 */
public interface Knot{

    /**
     * Gives back the Successors of a Knot
     * @return a Stack with the Successors
     */
    public ArrayDeque<Knot> getSuccessors();
    
    /**
     * Gives the estimated distance to the goal from this knot
     * @return heuristic
     */
    public int getHeuristic();
    
    public int getCosts();
    
    public void setCosts(int costs);
    
    /**
     * Returns the costs to get from this knot to a knot b
     * @return 
     */
    public int getTransitionCosts(Knot b);
    
    
}