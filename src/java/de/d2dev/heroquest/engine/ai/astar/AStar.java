package de.d2dev.heroquest.engine.ai.astar;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;



/**
 *
 * @author Simon
 */
public class AStar<T extends Knot> {

    private PriorityQueue<Path<T>> agenda;
    private Map<T, Integer> closedList;

    /**
     * Constructs the Data with a start T
     * @param start T where the Algorithm starts searching
     */
    public AStar() {
        this.closedList = new HashMap<T, Integer>();
        this.agenda = new PriorityQueue<Path<T>>();
    }

    /**
     * Tries if T is a Goal
     * @param a T to be tested
     * @return true if goal, else false
     */
    private boolean isGoal(T a) {
        if (a == null) {
            throw new NullPointerException("isGoal: T is null");
        }
        return a.getHeuristic() == 0;
    }

    /**
     * Expands the end T of a path and puts the new Paths in the agenda
     * @param a Path to expand
     */
    private void expand(Path<T> a) {
        if (a == null) {
            throw new NullPointerException("Expand: Path is null");
        }
        T top = a.getTop();
        closedList.put(top, a.getCosts());
        for (T successor : (Stack<T>)top.getSuccessors()) {
            if (closedList.get(successor) == null || closedList.get(successor) > a.getCosts()) {
                Path<T> tmp = new Path<T>(a);
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
    public Stack<Path<T>> search(T start, int solutionCount) {
        
        
        Stack<Path<T>> result = new Stack<Path<T>>();
        Path<T> next = new Path<T>(start);
        agenda.add(next);
        
        while (!agenda.isEmpty()) {
            T top = next.getTop();
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
