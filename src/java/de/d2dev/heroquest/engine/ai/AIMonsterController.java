/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.game.action.GameAction;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 *
 * @author Simon + Toni
 */
public class AIMonsterController implements AIController {

    private Unit unit;
    private Map map;
    private PriorityQueue<Target> targets;
    private MapCommunicator communicator;

    public AIMonsterController(Unit unit, Map map) {

        this.unit = unit;
        this.map = map;
        this.targets = new PriorityQueue<Target>();
        this.communicator = new MapCommunicator(this.map);
    }



    @Override
    public List<GameAction> getActions() {
        for (Unit heroe : map.getHeroes()) {
            targets.add(
                    new Target(
                    this.communicator.getPathSearch(heroe.getField(), unit.getField()),
                    heroe));
        }
        
        Stack<SearchKnot> actionPath = targets.poll().getPathToMonster();
        
        
        return null;
    }
    
}
