package de.d2dev.heroquest.engine.ai.astar;

import de.d2dev.heroquest.engine.ai.astar.tests.SearchKnot;
import java.util.Stack;



/**
 * Path class for Astar search
 * 
 * @author Simon
 */
public class Path implements Comparable {

    /**Transition from one to the other Knot*/
    private final int TRANSITIONCOSTS = 1;
    /** Storage for the path knots */
    private Stack<Knot> trace;
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
    public Path(Stack<Knot> trace, int costs, int estimateCosts, int totalCosts) {
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
    public Path(Path copy) {

        this(new Stack<Knot>(),
                copy.getCosts(),
                copy.getEstimateCosts(),
                copy.getTotalCosts());
        trace.addAll(copy.getTrace());
    }

    /**
     * Path with initial start knot
     * @param start 
     */
    public Path(Knot start) {

        this(new Stack<Knot>(),
                0,
                start.getHeuristic(),
                start.getHeuristic());

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

    public Stack getTrace() {
        return trace;
    }

    public Knot getTop() {
        return trace.peek();
    }

//****************Public Methods************************ 
    /**
     * Adds a knot to the Path 
     * @param a Knot to add
     */
    public void addKnot(Knot a) {
            costs += a.getTransitionCosts(getTop());
            estimateCosts = a.getHeuristic();
            totalCosts = costs + estimateCosts;
            trace.add(a);

    }

    //*************Override*******************   
    @Override
    public int compareTo(Object o) {
        if (o instanceof Path) {
            return totalCosts - ((Path) o).getTotalCosts();
        }
        throw new RuntimeException("Not Comparable");
    }

    @Override
    public String toString() {

        int[][] field = {
            { 0, 0, 0, 0, 0, 0,-1},
            { 0, 0, 0,-1, 0,-1,-1},
            { 0,-1,-1,-1, 0,-1},
            { 0, 0,-1, 0, 0, 0, 0, 0},
            { 0, 0,-1,-1,-1, 0},
            { 0, 0,-1, 0,-1, 0, 0, 0},
            { 0, 0, 0, 0,-1, 0,-1,-1},
            { 0,-1,-1, 0,-1, 0, 0, 0},
            { 0, 0,-1, 0, 0, 0, 0, 0},
            { 0, 0,-1, 0, 0,-1,-1, 0}};

        for (Knot knot : trace) {
            field[((SearchKnot) knot).getX()][((SearchKnot) knot).getY()] = 4;
        }
        String result = trace.size() + "\n";
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == -1) {
                    result += "# ";
                } else if (field[i][j] == 0) {
                    result += 0 + " ";
                } else {
                    result += (char) 186 + " ";
//                    result += field[i][j] + ((field[i][j] < 10) ? " " : "");
                }
            }
            result += "\n";
        }
        result += "\n";
        return result;
    }
}
