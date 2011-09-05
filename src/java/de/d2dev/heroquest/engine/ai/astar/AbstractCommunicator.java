/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai.astar;

import de.d2dev.heroquest.engine.ai.SearchKnot;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import java.util.ArrayDeque;

/**
 *
 * @author Simon + Toni
 */
public abstract class AbstractCommunicator implements Communicator {

    private Map map;
    private int goalX, goalY;
    private AStar<SearchKnot> astar;

    public AbstractCommunicator (Map map) {
        this.map = map;
    }
//********************Public*****************************  

    public Map getMap() {
        return map;
    }

    public int getGoalX() {
        return goalX;
    }

    public int getGoalY() {
        return goalY;
    }
    
    

    public void setGoal(int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public SearchKnot getKnot(Field field) {
        return new SearchKnot(field, getHeuristic(field), this);
    }

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
        return path;

    }

//***********InterFace Communicator*******************
    @Override
    public abstract int getTransitionCosts(Knot a, Knot b);

    @Override
    public abstract ArrayDeque<Knot> getSuccessors(Knot actual);


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

    public abstract int getHeuristic(Field field);
}
