/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.ai.astar.Communicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.game.Map;
import java.util.Stack;

/**
 *
 * @author Simon + Toni
 */
public class MapCommunicator implements Communicator {

    private Map map;
    private int goalX, goalY;

//********************Public*****************************   
    public MapCommunicator(Map map, int goalX, int goalY) {
        this.map = map;
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public void setGoal(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public SearchKnot getKnot(int x, int y) {
        return new SearchKnot(x, y, getHeuristic(x, y), this);
    }

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
