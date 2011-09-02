package de.d2dev.heroquest.engine.game.action;

import java.util.List;
import java.util.Vector;

import de.d2dev.heroquest.engine.game.Direction2D;
import de.d2dev.heroquest.engine.game.Unit;

/**
 * Utility class to build a list of actions for a given unit using helper methods.
 * @author Sebastian Bordt
 *
 */
public class ActionBuilder {
	
	private Unit unit;
	private List<GameAction> actions = new Vector<GameAction>();
	
	public ActionBuilder(Unit unit) {
		super();
		this.unit = unit;
	}

	public void addAction(GameAction action) {
		this.actions.add(action);
	}
	
	public void addMove(Direction2D direction) {
		this.actions.add( new MoveAction( unit, direction ) );
	}
	
	public void addMoves(Direction2D direction, int numMoves) {
		for (int i=0; i<numMoves; i++) {
			this.addMove(direction);
		}
	}
	
	public List<GameAction> getActions() {
		return this.actions;
	}

}
