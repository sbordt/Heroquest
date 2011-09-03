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
import java.util.ArrayDeque;

/**
 *
 * @author Simon + Toni
 */
public class MapCommunicator implements Communicator {

    private Map map;
    private int goalX, goalY;
    private AStar<SearchKnot> astar;
    private boolean findBlocked;

    public MapCommunicator(Map map) {
        this.map = map;
        this.findBlocked = false;

    }

//********************Public*****************************  
    public Map getMap() {
        return map;
    }

    public void setGoal(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public SearchKnot getKnot(Field field) {
        return new SearchKnot(field, getHeuristic(field), this);
    }

//*****************search Methods***********************************
    public ArrayDeque<Field> getNextPath() {
        if (astar == null) {
            throw new RuntimeException("getNextPath: Astar not initialized");
        }
        Path<SearchKnot> result = astar.getNextPath();
        if (result == null) {
            return null;
        }
        return transformPathInField(result);
    }

    public ArrayDeque<Field> getPath(Field start, Field goal) {

        //Setting up the goal for heuristic
        this.goalX = goal.getX();
        this.goalY = goal.getY();
        //Resetting Astar with new Startposition
        newStart(start);
        //Find first way
        Path<SearchKnot> result = astar.getNextPath();
        if (result == null) {
            return null;
        }
        ArrayDeque<Field> path = transformPathInField(result);
        int i = 0;
        for (Field field : path) {
            if (field.hasUnit()) {
                System.out.println(i + " " + field.getUnit().getName());
            } else {
                System.out.println(i + " " + field.isBlocked());
            }
            i++;
        }
        return path;

    }

    public ArrayDeque<Field> getBlockedPath(Field start, Field goal) {
        findBlocked = true;
        ArrayDeque<Field> path = getPath(start, goal);
        findBlocked = false;
        int i = 0;
        for (Field field : path) {
            if (field.hasUnit()) {
                System.out.println(i + " " + field.getUnit().getName());
            } else {
                System.out.println(i + " " + field.isBlocked());
            }
            i++;
        }
        return path;
    }

//***********InterFace Communicator*******************
    @Override
    public int getTransitionCosts(Knot a, Knot b) {
        return 1;
    }

    @Override
    public ArrayDeque<Knot> getSuccessors(Knot actual) {
        ArrayDeque<Knot> speicher = new ArrayDeque<Knot>();

        for (Field f : ((SearchKnot) actual).getField().getNeighbours()) {
            if (!f.isBlocked()) {
                speicher.add(getKnot(f));
            } else if ((f.getX() == goalX && f.getY() == goalY) || (f.hasUnit() && findBlocked) ) {
                speicher.add(getKnot(f));
            }

        }

        return speicher;
    }

    //******************Private*************************   
    private void newStart(Field field) {
        this.astar = new AStar<SearchKnot>(getKnot(field));
    }

    private ArrayDeque<Field> transformPathInField(Path<SearchKnot> path) {

        ArrayDeque<Field> result = new ArrayDeque<Field>();
        for (SearchKnot knot : path.getTrace()) {
            result.addLast(knot.getField());
        }
        return result;
    }

    private int getHeuristic(Field field) {
        return Math.abs(goalX - field.getX()) + Math.abs(goalY - field.getY());
    }
}
