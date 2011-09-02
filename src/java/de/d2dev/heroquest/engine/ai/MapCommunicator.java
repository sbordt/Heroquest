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


//********************Public*****************************   
    public MapCommunicator(Map map) {
        this.map = map;
        
    }

    public void setGoal(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public SearchKnot getKnot(Field field) {
        return new SearchKnot(field, getHeuristic(field), this);
    }

//*****************search Methods***********************************
    public LinkedList<Path<SearchKnot>> search(int startX, int startY, int goalX, int goalY, int solutionCount) {
        AStar<SearchKnot> astar = new AStar<SearchKnot>();
        this.goalX = goalX;
        this.goalY = goalY;
        return astar.search(getKnot(map.getField(startX, startY)), solutionCount);
    }

    public int getCostSearch(int startX, int startY, int goalX, int goalY) {
        LinkedList<Path<SearchKnot>> result = search(startX, startY, goalX, goalY, 1);
        if (result.isEmpty()) {
            return -1;
        }
        return result.pop().getCosts();
    }

    public int getCostSearch(Field start, Field goal) {
        this.goalX = goal.getX();
        this.goalY = goal.getY();
        LinkedList<Path<SearchKnot>> result = search(start.getX(), start.getY(), goalX, goalY, 1);
        if (result.isEmpty()) {
            return -1;
        }
        return result.pop().getCosts();
    }

    public LinkedList<SearchKnot> getPathSearch(Field start, Field goal) {
        this.goalX = goal.getX();
        this.goalY = goal.getY();
        LinkedList<Path<SearchKnot>> result = search(start.getX(), start.getY(), goalX, goalY, 1);
        if (result.isEmpty()) {
            return null;
        }
        return result.pop().getTrace();
    }

    public LinkedList<SearchKnot> getPathSearch(int startX, int startY, int goalX, int goalY) {
        LinkedList<Path<SearchKnot>> result = search(startX, startY, goalX, goalY, 1);
        if (result.isEmpty()) {
            return null;
        }
        System.out.println("Weg vielleicht gefunden");
        return result.pop().getTrace();
    }

//***********InterFace Communicator*******************
    @Override
    public int getTransitionCosts(Knot a, Knot b) {
        return 1;
    }

    @Override
    public LinkedList<Knot> getSuccessors(Knot actual) {
        LinkedList<Knot> speicher = new LinkedList<Knot>();

        for (Field f : ((SearchKnot)actual).getField().getNeighbours()) {
            if (!f.isBlocked()) {
                speicher.add(getKnot(f));
            } else if (f.getX() == goalX && f.getY() == goalY) {
                speicher.add(getKnot(f));
            }
        }


        return speicher;
    }
    //******************Private*************************   


    private int getHeuristic(Field field) {
        return Math.abs(goalX - field.getX()) + Math.abs(goalY - field.getY());
    }
}
