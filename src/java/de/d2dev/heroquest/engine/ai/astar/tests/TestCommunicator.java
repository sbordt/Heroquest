package de.d2dev.heroquest.engine.ai.astar.tests;

import de.d2dev.heroquest.engine.ai.SearchKnot;
import de.d2dev.heroquest.engine.ai.astar.AStar;
import de.d2dev.heroquest.engine.ai.astar.Communicator;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.ai.astar.Path;
import java.util.LinkedList;
import java.util.Stack;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Simon + Toni
 */
public class TestCommunicator {

    private int[][] field;
    private final int WALL = -1;
    private final int MONSTER = -2;
    private final int FREE = 0;
    private final int GOAL = -3;
    private int goalX;
    private int goalY;
    private AStar<SearchKnot> astar;

    public TestCommunicator() {
        this.astar = new AStar<SearchKnot>();
    }

    //****************Public*********************
    public int[][] getField() {
        return field;
    }

    public SearchKnot getKnot(int x, int y) {
        return new SearchKnot(x, y, getHeuristic(x, y), this);
    }

    @Override
    public LinkedList<Knot> getSuccessors(int x, int y) {
        LinkedList<Knot> speicher = new LinkedList<Knot>();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (!((i == x - 1 && j == y - 1)
                        || (i == x + 1 && j == y + 1)
                        || (i == x - 1 && j == y + 1)
                        || (i == x + 1 && j == y - 1))) {

                    if (fieldExists(i, j)) {
                        if (!blocked(i, j)) {

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

    @Override
    public int getTransitionCosts(Knot a, Knot b) {
        return 1;
    }

    public Stack<Path<SearchKnot>> search(
            int[][] field,
            int startX, int startY,
            int goalX, int goalY,
            int solutionCount) {

        this.field = field;
        this.goalX = goalX;
        this.goalY = goalY;
        return astar.search(getKnot(startX, startY), solutionCount);

    }

    //*************Private********************   
    private boolean blocked(int x, int y) {
        int value = field[x][y];
        return value == WALL;
    }

    private boolean fieldExists(int x, int y) {
        return x >= 0 && x < field.length && y >= 0 && y < field[x].length;
    }

    private int getHeuristic(int x, int y) {
        return Math.abs(goalX - x) + Math.abs(goalY - y);
    }
}
