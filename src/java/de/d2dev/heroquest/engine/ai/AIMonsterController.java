/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Direction2D;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.game.action.ActionBuilder;
import de.d2dev.heroquest.engine.game.action.GameAction;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
            this.communicator.getPathSearch(unit.getField(), heroe.getField());
            targets.add(
                    new Target(
                    this.communicator.getPathSearch(unit.getField(), heroe.getField()),
                    heroe));
        }

        LinkedList<SearchKnot> actionPath = targets.poll().getPathToMonster();
        ActionBuilder actionBuilder = new ActionBuilder(unit);
        SearchKnot start = actionPath.pop();
        int oldX = start.getX();
        int oldY = start.getY();
        for (SearchKnot knot : actionPath) {
            int x = knot.getX();
            int y = knot.getY();
            int xDirection = (x - oldX + 1) * 2;
            int yDirection = (y - oldY - 1) * 3;
            int action = xDirection + yDirection;
            switch (action) {
                case 1:
                    System.out.println("AIController: Down");
                    actionBuilder.addMove(Direction2D.RIGHT);
                    break;
                case 2:
                    System.out.println("AIController: Right");
                    actionBuilder.addMove(Direction2D.DOWN);
                    break;
                case -3:
                    System.out.println("AIController: Up");
                    actionBuilder.addMove(Direction2D.LEFT);
                    break;
                case -4:
                    System.out.println("AIController: Left");
                    actionBuilder.addMove(Direction2D.UP);
                    break;
            }
            oldX = x;
            oldY = y;

        }

        return actionBuilder.getActions();
    }
}
