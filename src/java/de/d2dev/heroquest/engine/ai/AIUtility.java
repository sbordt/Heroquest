/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Unit;
import java.util.ArrayDeque;
import java.util.PriorityQueue;

/**
 *
 * @author Simon + Toni
 */
public class AIUtility {

    private PriorityQueue<Target> targets;
    private Map map;
    private FindPath findPath;
    private FindBlockedPath findBlockedPath;

    public AIUtility(Map map) {
        this.targets = new PriorityQueue<Target>();
        this.map = map;
        this.findPath = new FindPath(this.map);
        this.findBlockedPath = new FindBlockedPath(this.map);
    }

//******************Static Methods****************************************
    /**
     * 
     * @param map
     * @param start
     * @param goal
     * @param agression
     * @return 
     */
    public static Target getTarget(Map map, Unit start, Unit goal, double agression) {

        AIUtility util = new AIUtility(map);
        return util.findWay(start, goal, agression);
        
    }

//*****************Public Methods********************************

    public Target findWay(Unit start, Unit goal, double agression){
        ArrayDeque<Field> path = findPath.getPath(start.getField(), goal.getField());

            //If path found 
            if (path != null) {
                //Heroe as target with direct path and add it to the priorityqueue
                Target nextTarget = new Target(goal, path, 0);
                targets.add(nextTarget);
                //Look if there is a shorter blocked path
                ArrayDeque<Field> blockedPath = findBlockedPath.getPath(start.getField(), goal.getField());
                // if blockedPath + 1* agression is shorter, then try to deblock unit
                if ((blockedPath.size()+ 1 * agression) < path.size() ) {
                    Target deblocked = deblockUnit(start,goal, 1);
                    targets.add(deblocked);
                }                             
            } else {
                //If no direct way found, try to deblock unit
                Target deblocked = deblockUnit(start,goal, 1);
                if (deblocked != null) {
                    targets.add(deblocked);
                } else {
                    System.out.println(start.getName() + " Deblocking not successful");
                }
            }
            return targets.remove();
    }
    
        public Target deblockUnit(Unit from, Unit blockedUnit, int costs) {

        ArrayDeque<Field> blockedPath = findBlockedPath.getPath(from.getField(), blockedUnit.getField());
        while (!blockedPath.isEmpty()) {

            Field last = blockedPath.removeLast();
            if (last.hasUnit() && last.getUnit() != blockedUnit ) {
                Unit nextUnit = last.getUnit();
                ArrayDeque<Field> unblockedPath = findPath.getPath(from.getField(), last);
                if (unblockedPath != null) {
                    return new Target(nextUnit, unblockedPath, costs);

                } else {
                    return deblockUnit(from, nextUnit, costs + 1*3);
                }
            }
            costs++;
        }
        return null;
    }
}
