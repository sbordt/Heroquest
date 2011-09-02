/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.ai.astar.AStar;
import de.d2dev.heroquest.engine.ai.astar.Communicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.ai.astar.Path;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Simon + Toni
 */
public class MapCommunicator implements Communicator {

    private Map map;
    private int goalX, goalY;
    private AStar<SearchKnot> astar;

//********************Public*****************************   
    public MapCommunicator(Map map) {
        this.map = map;
        this.astar = new AStar<SearchKnot>();
    }

    public void setGoal(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public SearchKnot getKnot(int x, int y) {
        return new SearchKnot(x, y, getHeuristic(x, y), this);
    }

//*****************search Methods***********************************
    public Stack<Path<SearchKnot>> search(int startX, int startY, int goalX, int goalY, int solutionCount) {

        this.goalX = goalX;
        this.goalY = goalY;
        return astar.search(getKnot(startX, startY), solutionCount);
    }

    public int getCostSearch(int startX, int startY, int goalX, int goalY) {
        Stack<Path<SearchKnot>> result = search(startX, startY, goalX, goalY, 1);
        if (result.isEmpty()) {
            return -1;
        }
        return result.pop().getCosts();
    }

    public int getCostSearch(Field start, Field goal) {
        this.goalX = goal.getX();
        this.goalY = goal.getY();
        Stack<Path<SearchKnot>> result = search(start.getX(), start.getY(), goalX, goalY, 1);
        if (result.isEmpty()) {
            return -1;
        }
        return result.pop().getCosts();
    }

    public LinkedList<SearchKnot> getPathSearch(Field start, Field goal) {
        this.goalX = goal.getX();
        this.goalY = goal.getY();
        Stack<Path<SearchKnot>> result = search(start.getX(), start.getY(), goalX, goalY, 1);
        if (result.isEmpty()) {
            return null;
        }
        return result.pop().getTrace();
    }

    public LinkedList<SearchKnot> getPathSearch(int startX, int startY, int goalX, int goalY) {
        Stack<Path<SearchKnot>> result = search(startX, startY, goalX, goalY, 1);
        if (result.isEmpty()) {
            return null;
        }

        return result.pop().getTrace();
    }

//***********InterFace Communicator*******************
    @Override
    public int getTransitionCosts(Knot a, Knot b) {
        return 1;
    }

    @Override
    public Stack<Knot> getSuccessors(int x, int y) {
        Stack<Knot> speicher = new Stack<Knot>();

        Field actual = map.getField(x, y);

        
        for (Field f : actual.getNeighbours) {
            if (!f.isBlocked()) {
                speicher.add(getKnot(f.getX(),  f.getY()));
            } else if (f.getX() == goalX && f.getY() == goalY) {
                speicher.add(getKnot(f.getX(),  f.getY()));
            }
        }


        return speicher;
    }
    //******************Private*************************   


    private int getHeuristic(int x, int y) {
        return Math.abs(goalX - x) + Math.abs(goalY - y);
    }
}
