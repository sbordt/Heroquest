/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Unit;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Simon + Toni
 */
public class Target implements Comparable<Target> {

    private LinkedList<SearchKnot> pathToMonster;
    private Unit unit;

    public Target(LinkedList<SearchKnot> pathToMonster, Unit unit) {
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

    public LinkedList<SearchKnot> getPathToMonster() {
        return pathToMonster;
    }

    @Override
    public int compareTo(Target o) {
        return pathToMonster.size() - o.getPathToMonster().size();
    }
}
