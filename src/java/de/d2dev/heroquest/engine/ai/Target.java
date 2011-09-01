/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Unit;
import java.util.Stack;

/**
 *
 * @author Simon + Toni
 */
public class Target implements Comparable<Target> {
    
    private Stack<SearchKnot> pathToMonster;
    private Unit unit;

    public Target(Stack<SearchKnot> pathToMonster, Unit unit) {
        this.pathToMonster = pathToMonster;
        this.unit = unit;
    }


    public Unit getUnit() {
        return unit;
    }

    public Stack<SearchKnot> getPathToMonster() {
        return pathToMonster;
    }
 
    
    @Override
    public int compareTo(Target o) {
        return pathToMonster.size() - o.getPathToMonster().size();
    }


    
}
