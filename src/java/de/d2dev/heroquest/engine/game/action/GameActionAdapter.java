package de.d2dev.heroquest.engine.game.action;

import de.d2dev.heroquest.engine.game.GameContext;
import de.d2dev.heroquest.engine.game.Unit;

public abstract class GameActionAdapter implements GameAction {
	
	protected GameContext context;
	protected Unit unit;

	public GameActionAdapter(Unit unit) {
		super();
		this.context = unit.getGameContext();
		this.unit = unit;
	}

	@Override
	public GameContext getGameContext() {
		return this.context;
	}

	@Override
	public Unit getActingUnit() {
		return this.unit;
	}
}
