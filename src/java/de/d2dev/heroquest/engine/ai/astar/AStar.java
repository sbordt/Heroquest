package de.d2dev.heroquest.engine.ai.astar;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;



/**
 *
 * @author Simon
 */
public class AStar {

    private PriorityQueue<Path> agenda;
    private Map<Knot, Integer> closedList;

    /**
     * Constructs the Data with a start Knot
     * @param start Knot where the Algorithm starts searching
     */
    public AStar() {
        this.closedList = new HashMap<Knot, Integer>();
        this.agenda = new PriorityQueue<Path>();
    }

    /**
     * Tries if Knot is a Goal
     * @param a Knot to be tested
     * @return true if goal, else false
     */
    private boolean isGoal(Knot a) {
        if (a == null) {
            throw new NullPointerException("isGoal: Knot is null");
        }
        return a.getHeuristic() == 0;
    }

    /**
     * Expands the end Knot of a path and puts the new Paths in the agenda
     * @param a Path to expand
     */
    private void expand(Path a) {
        if (a == null) {
            throw new NullPointerException("Expand: Path is null");
        }
        Knot top = a.getTop();
        closedList.put(top, a.getCosts());
        for (Knot successor : top.getSuccessors()) {
            if (closedList.get(successor) == null || closedList.get(successor) > a.getCosts()) {
                Path tmp = new Path(a);
                tmp.addKnot(successor);
                agenda.add(tmp);
            }

        }
    }
    
    /**
     * Core Algorithm
     * @param start Start Point
     * @param solutionCount Number of solutions you want to get
     * @return Stack with optimal solutions
     */
    public Stack<Path> search(Knot start, int solutionCount) {
        
        
        Stack<Path> result = new Stack<Path>();
        Path next = new Path(start);
        agenda.add(next);
        
        while (!agenda.isEmpty()) {
            Knot top = next.getTop();
            if (isGoal(top)) {
                result.add(next);
                next = agenda.remove();
                solutionCount--;
                if (solutionCount == 0) {
                    break;
                }
            } else {
                next = agenda.remove();

            }
            expand(next);
        }
      
        return result;
    }
}
