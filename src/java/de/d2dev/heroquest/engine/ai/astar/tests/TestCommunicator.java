package de.d2dev.heroquest.engine.ai.astar.tests;

import de.d2dev.heroquest.engine.ai.SearchKnot;
import de.d2dev.heroquest.engine.ai.astar.AStar;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.ai.astar.Path;
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
    private int goalX = 99;
    private int goalY = 99;

    public TestCommunicator() {
        field = new int[100][100];
        field[goalX][goalY] = GOAL;
    }

    //****************Public*********************
    public Stack<Knot> getSuccessors(int x, int y) {
        Stack<Knot> speicher = new Stack<Knot>();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (!((i == x - 1 && j == y - 1)
                        || (i == x + 1 && j == y + 1)
                        || (i == x - 1 && j == y + 1)
                        || (i == x + 1 && j == y - 1))) {

                    if (fieldExists(i, j)) {
                        if (!blocked(i, j)) {

                            speicher.add(getKnot(i, j));
                        }
                    }
                }
            }
        }
        return speicher;
    }

    public int[][] getField() {
        return field;
    }
    
    public int getTransitionCosts(Knot a,Knot b) {
        return 1;
    }

//*************Private********************   
    
    
    private SearchKnot getKnot(int x, int y) {
        return new SearchKnot(x, y, getHeuristic(x, y), this);
    }

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

//**************** MAIN *************************
    public static void main(String[] args) {
        TestCommunicator c = new TestCommunicator();
        AStar astar = new AStar();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i <= 1; i++) {
            Stack<Path<Knot>> result = (Knot)astar.search(c.getKnot(0, 0), 100);
        }
        System.out.println(System.currentTimeMillis() - startTime);

//        for (Path a : result) {
//            System.out.print(a);            
//        }


    }
}


//int[][] field = {
//            { 0, 0, 0, 0, 0, 0,-1},
//            { 0, 0, 0,-1, 0,-1,-1},
//            { 0,-1,-1,-1, 0,-1},
//            { 0, 0,-1, 0, 0, 0, 0, 0},
//            { 0, 0,-1,-1,-1, 0},
//            { 0, 0,-1, 0,-1, 0, 0, 0},
//            { 0, 0, 0, 0,-1, 0,-1,-1},
//            { 0,-1,-1, 0,-1, 0, 0, 0},
//            { 0, 0,-1, 0, 0, 0, 0, 0},
//            { 0, 0,-1, 0, 0,-1,-1, 0}};
//
//        for (Knot knot : trace) {
//            field[((SearchKnot) knot).getX()][((SearchKnot) knot).getY()] = 4;
//        }
//        String result = trace.size() + "\n";
//        for (int i = 0; i < field.length; i++) {
//            for (int j = 0; j < field[i].length; j++) {
//                if (field[i][j] == -1) {
//                    result += "# ";
//                } else if (field[i][j] == 0) {
//                    result += 0 + " ";
//                } else {
//                    result += (char) 186 + " ";
////                    result += field[i][j] + ((field[i][j] < 10) ? " " : "");
//                }
//            }
//            result += "\n";
//        }
//        result += "\n";
//        return result;