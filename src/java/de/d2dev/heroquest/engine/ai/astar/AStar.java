package de.d2dev.heroquest.engine.ai.astar;

import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 *
 * @author Simon
 */
public class AStar<T extends Knot> {

    private PriorityQueue<Path<T>> agenda;
//    private Map<T, Integer> closedList;
    private Path<T> next;

    /**
     * Constructs the Data with a start T
     * @param start T where the Algorithm starts searching
     */
    public AStar(T start) {
//        this.closedList = new HashMap<T, Integer>();
        this.agenda = new PriorityQueue<Path<T>>();
        this.next = new Path<T>(start);
        agenda.add(next);
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
        for (T successor : (ArrayDeque<T>) top.getSuccessors()) {
            if ((a.getCosts()+1 < successor.getCosts()) || (successor.getCosts() == -1)) {  
                Path<T> tmp = new Path<T>(a);
                tmp.addKnot(successor);
                successor.setCosts(a.getCosts()+1);
                agenda.add(tmp);               
            }
        }
    }

//    /**
//     * Expands the end T of a path and puts the new Paths in the agenda
//     * @param a Path to expand
//     */
//    private void expand(Path<T> a) {
//        if (a == null) {
//            throw new NullPointerException("Expand: Path is null");
//        }
//        T top = a.getTop();
//        closedList.put(top, a.getCosts());
//        for (T successor : (ArrayDeque<T>) top.getSuccessors()) {
//            if (closedList.get(successor) == null || closedList.get(successor) > a.getCosts()) {
//                Path<T> tmp = new Path<T>(a);
//                tmp.addKnot(successor);
//                agenda.add(tmp);
//
//            }
//
//        }
//    }
    public Path<T> getNextPath() {


        while (!agenda.isEmpty()) {
            T top = next.getTop();
            if (isGoal(top)) {
                Path<T> goal = next;
                next = agenda.remove();
                expand(next);
                return goal;
            } else {
                next = agenda.remove();
            }
            expand(next);
        }
        return null;
    }
//    /**
//     * Core Algorithm
//     * @param start Start Point
//     * @param solutionCount Number of solutions you want to get
//     * @return Stack with optimal solutions
//     */
//    public ArrayDeque<Path<T>> search(T start, int solutionCount) {
//
//
//        ArrayDeque<Path<T>> result = new ArrayDeque<Path<T>>();
//
//
//        while (!agenda.isEmpty()) {
//            T top = next.getTop();
////            System.out.println("Agenda: " + agenda.size());
//            if (isGoal(top)) {
//                result.add(next);
//                next = agenda.remove();
//                solutionCount--;
//                if (solutionCount == 0) {
//                    break;
//                }
//            } else {
//                next = agenda.remove();
//
//            }
//            expand(next);
//        }
//
//
//        return result;
//    }
}