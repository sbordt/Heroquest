package de.d2dev.heroquest.engine.ai.astar;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Path class for Astar search
 * 
 * @author Simon
 */
public class Path<T extends Knot> implements Comparable<Path<T>> {

    /** Storage for the path knots */
    private LinkedList<T> trace;
    /**real costs from start to end*/
    private int costs;
    /**estimated costs from end to goal*/
    private int estimateCosts;
    /**costs + estimated costs*/
    private int totalCosts;

//*****************Constructor*******************************
    /**
     * Most general constructor
     * @param trace Storage for the path knots
     * @param costs real costs from start to end
     * @param estimateCosts estimated costs from end to goal
     * @param totalCosts costs + estimated costs
     * 
     */
    public Path(
            LinkedList<T> trace, 
            int costs, 
            int estimateCosts, 
            int totalCosts
            ) {
        
        
        if (trace == null) {
            throw new NullPointerException("Constructor: Stack is null");
        }
        this.trace = trace;
        this.costs = costs;
        this.estimateCosts = estimateCosts;
        this.totalCosts = totalCosts;

    }

    /**
     * Deep Copy 
     * @param copy 
     */
    public Path(Path<T> copy) {

        this(new LinkedList<T>(),
                copy.getCosts(),
                copy.getEstimateCosts(),
                copy.getTotalCosts()
                );
        
        trace.addAll(copy.getTrace());
    }

    /**
     * Path with initial start knot
     * @param start 
     */
    public Path(T start) {

        this(new LinkedList<T>(),
                0,
                start.getHeuristic(),
                start.getHeuristic()
                );

        if (start == null) {
            throw new NullPointerException("Constructor: The start of the Path is Null");
        }
        trace.add(start);
    }

//*******************************Getter/Setter********************************
    public int getCosts() {
        return costs;
    }

    public int getEstimateCosts() {
        return estimateCosts;
    }

    public int getTotalCosts() {
        return totalCosts;
    }

    public LinkedList<T> getTrace() {
//        Collections.reverse(trace);
        return trace;
    }

    public T getTop() {
        return trace.getLast();
    }

//****************Public Methods************************ 
    /**
     * Adds a knot to the Path 
     * @param a T to add
     */
    public void addKnot(T a) {
        costs += a.getTransitionCosts(getTop());
        estimateCosts = a.getHeuristic();
        totalCosts = costs + estimateCosts;
        trace.add(a);

    }

    //*************Override*******************   
    @Override
    public int compareTo(Path<T> o) {
        if (o == null) {
            throw new NullPointerException("CompareTo: Object to Compare is null");
        }

        return totalCosts - o.getTotalCosts();


    }
}
