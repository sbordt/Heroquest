/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Unit;
import java.util.ArrayDeque;

/**
 *
 * @author Simon + Toni
 */
public class Target implements Comparable<Target> {

    private ArrayDeque<Field> pathToMonster;
    private Unit unit;
    private int estimateCost;

    public Target(Unit unit, ArrayDeque<Field> pathToMonster, int estimateCost) {
        if (pathToMonster == null) {
            throw new RuntimeException("TargetConstructor: pathToMonster == Null");
        }
        if (unit == null) {
            throw new RuntimeException("TargetConstructor: unit == Null");
        }
        this.pathToMonster = pathToMonster;
        this.unit = unit;
        this.estimateCost = pathToMonster.size() + estimateCost;
    }

    public int getEstimateCost() {
        return estimateCost;
    }

    
    
    public Unit getUnit() {
        return unit;
    }

    public ArrayDeque<Field> getPathToMonster() {
        return pathToMonster;
    }

    @Override
    public int compareTo(Target o) {
        return estimateCost - o.getEstimateCost();
    }
}
