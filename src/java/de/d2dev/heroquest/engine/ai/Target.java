/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Unit;
import java.util.ArrayDeque;


/**
 *
 * @author Simon + Toni
 */
public class Target implements Comparable<Target> {

    private ArrayDeque<SearchKnot> pathToMonster;
    private Unit unit;

    public Target(ArrayDeque<SearchKnot> pathToMonster, Unit unit) {
        if (pathToMonster == null) {
            throw new RuntimeException("TargetConstructor: pathToMonster == Null");
        }
        if (unit == null) {
            throw new RuntimeException("TargetConstructor: unit == Null");
        }
        this.pathToMonster = pathToMonster;
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public ArrayDeque<SearchKnot> getPathToMonster() {
        return pathToMonster;
    }

    @Override
    public int compareTo(Target o) {
        return pathToMonster.size() - o.getPathToMonster().size();
    }
}
