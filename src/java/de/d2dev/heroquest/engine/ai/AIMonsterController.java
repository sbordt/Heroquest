/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.game.Direction2D;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.game.action.ActionBuilder;
import de.d2dev.heroquest.engine.game.action.GameAction;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author Simon
 */
public class AIMonsterController implements AIController {

    private Unit unit;
    private Map map;
    private PriorityQueue<Target> targets;
    private double agression = 1;

    public AIMonsterController(Unit unit, Map map) {

        this.unit = unit;
        this.map = map;
        this.targets = new PriorityQueue<Target>();
    }

//*************************Interface AIController*******************************
    @Override
    public List<GameAction> getActions() {
        System.out.println("Monster wurde aufgerufen: "+ unit.getName());
        //Fill the priority queue with heroes
        findWay();
        //Get Path to next Target
        if (!targets.isEmpty()) {
            ArrayDeque<Field> actionPath = targets.remove().getPathToMonster();
            //Turn it into an action sequence
            return traversePath2Action(actionPath);
        }
        System.out.println("No way found: " + unit.getName());
        //If no Path found return an empty list
        return new ArrayList<GameAction>();

    }

//************************Private Methods***************************************
   /**
     * Fills the PriorityQueue with all the heroes and their shortest 
     * unblocked Path
     */
    private void findWay() {
        targets.clear();
        List<Unit> heroes = map.getHeroes();
        System.out.println("Heroes on map: "+heroes.size());
        while (!heroes.isEmpty()) {
            //Starting with first heroe
            Unit nextUnit = heroes.get(0);
            //Get shortest unblocked Path
            Target target = AIUtility.getTarget(this.map, this.unit, nextUnit, agression);
            targets.add(target);
            heroes.remove(0);
        }
        
    }


    private List<GameAction> traversePath2Action(ArrayDeque<Field> actionPath) {

        Field start = actionPath.getFirst();
        int oldX = start.getX();
        int oldY = start.getY();
        ActionBuilder actionBuilder = new ActionBuilder(unit);
        for (Field field : actionPath) {

            int x = field.getX();
            int y = field.getY();
            int xDirection = (x - oldX + 1) * 2;
            int yDirection = (y - oldY - 1) * 3;
            int action = xDirection + yDirection;
            if (!field.isBlocked()) {
                switch (action) {
                    case 1:
                        actionBuilder.addMove(Direction2D.RIGHT);
                        break;
                    case 2:
                        actionBuilder.addMove(Direction2D.DOWN);
                        break;
                    case -3:
                        actionBuilder.addMove(Direction2D.LEFT);
                        break;
                    case -4:
                        actionBuilder.addMove(Direction2D.UP);
                        break;
                }
                oldX = x;
                oldY = y;

            }
        }
        return actionBuilder.getActions();

    }

    private void testPrint() {
        Field[][] field = this.map.getFields();
        String[][] path = new String[field.length][field[0].length];
        for (int y = 0; y < field[0].length; y++) {
            for (int x = 0; x < field.length; x++) {
                System.out.print(field[x][y].isBlocked() ? "# " : ". ");
            }
            System.out.println();

        }

    }
}
