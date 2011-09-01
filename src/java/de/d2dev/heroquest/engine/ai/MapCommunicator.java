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

    public Stack<SearchKnot> getPathSearch(Field start, Field goal) {
        this.goalX = goal.getX();
        this.goalY = goal.getY();
        Stack<Path<SearchKnot>> result = search(start.getX(), start.getY(), goalX, goalY, 1);
        if (result.isEmpty()) {
            return null;
        }
        return result.pop().getTrace();
    }

    public Stack<SearchKnot> getPathSearch(int startX, int startY, int goalX, int goalY) {
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
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (!((i == x - 1 && j == y - 1)
                        || (i == x + 1 && j == y + 1)
                        || (i == x - 1 && j == y + 1)
                        || (i == x + 1 && j == y - 1))) {

                    if (fieldExists(i, j)) {
                        if (!map.getField(i, j).isBlocked()) {
                            speicher.add(getKnot(i, j));
                        } else if (i == goalX && j == goalY) {
                            speicher.add(getKnot(i, j));
                        }
                    }
                }
            }
        }
        return speicher;
    }
    //******************Private*************************   

    private boolean fieldExists(int x, int y) {
        return x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight();
    }

    private int getHeuristic(int x, int y) {
        return Math.abs(goalX - x) + Math.abs(goalY - y);
    }
}
