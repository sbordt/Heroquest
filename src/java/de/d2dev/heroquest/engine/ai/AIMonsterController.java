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
 * @author Simon + Toni
 */
public class AIMonsterController implements AIController {

    private Unit unit;
    private Map map;
    private PriorityQueue<Target> targets;
    private PriorityQueue<Field> freeFields;
    private PriorityQueue<Target> unitFields;
    private MapCommunicator communicator;

    public AIMonsterController(Unit unit, Map map) {

        this.unit = unit;
        this.map = map;
        this.targets = new PriorityQueue<Target>();
        this.freeFields = new PriorityQueue<Field>();
        this.unitFields = new PriorityQueue<Target>();
        this.communicator = new MapCommunicator(this.map);
    }

//*************************Interface AIController*******************************
    @Override
    public List<GameAction> getActions() {
        //Fill the priority queue with heroes
        findWay();
        //Get Path to next Target
        if (!targets.isEmpty()) {
            ArrayDeque<Field> actionPath = targets.remove().getPathToMonster();
            // After action, all targets in PriorityQueue become obsolete
            targets.clear();
            //Turn it into an action sequence
            return traversePath2Action(actionPath);
        }
        targets.clear();
        //If no Path found return an empty list
        return new ArrayList<GameAction>();

    }

//************************Private Methods***************************************
    private void findWay() {
        List<Unit> heroes = map.getHeroes();
        while (!heroes.isEmpty()) {
            Unit nextUnit = heroes.get(0);

            ArrayDeque<Field> path = communicator.getPath(this.unit.getField(), nextUnit.getField());
            if (path != null) {
                Target nextTarget = new Target(nextUnit, path, 0);
                targets.add(nextTarget);
            } else {
                System.out.println("findWay: trying to deblock: " + nextUnit.getName());
                Target deblocked = deblockUnit(nextUnit, 1);
                if (deblocked != null) {
                    targets.add(deblocked);
                } else {
                    System.out.println("Deblocking not successful");
                }
            }

            heroes.remove(0);
            System.out.println("Heroe wurde entfernt");
        }
    }

    private Target deblockUnit(Unit blockedUnit, int costs) {
        System.out.println("Starting deblock: " + blockedUnit.getName());
        System.out.println("Start: " + this.unit.getName());
        System.out.println("Goal: " + blockedUnit.getName());
        ArrayDeque<Field> blockedPath = communicator.getBlockedPath(this.unit.getField(), blockedUnit.getField());
        int i = 0;
        for (Field field : blockedPath) {
//            if (field.hasUnit()) {
//                System.out.println(i + " " + field.getUnit().getName());
//            } else {
//                System.out.println(i + " " + field.isBlocked());
//            }
            field.setTexture(TextureResource.createTextureResource("error.jpg"));
            i++;
        }

        while (!blockedPath.isEmpty()) {
            Field last = blockedPath.removeLast();
            if (last.hasUnit()) {
                Unit nextUnit = last.getUnit();
                System.out.println("Unit found on path " + nextUnit.getName());
                ArrayDeque<Field> unblockedPath = communicator.getPath(this.unit.getField(), last);
                if (unblockedPath != null) {
                    System.out.println("Returns the unblocked path " + costs);
                    return new Target(nextUnit, unblockedPath, 0);

                } else {
                    System.out.println("Deblock: Path is null: " + costs);
                    return deblockUnit(nextUnit, costs + 1);
                }
            }
            costs++;
        }
        return null;
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
//                        System.out.println("AIController: Right");
                        actionBuilder.addMove(Direction2D.RIGHT);
                        break;
                    case 2:
//                        System.out.println("AIController: Down");
                        actionBuilder.addMove(Direction2D.DOWN);
                        break;
                    case -3:
//                        System.out.println("AIController: Left");
                        actionBuilder.addMove(Direction2D.LEFT);
                        break;
                    case -4:
//                        System.out.println("AIController: Up");
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
