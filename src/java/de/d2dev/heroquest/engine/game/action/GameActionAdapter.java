package de.d2dev.heroquest.engine.game.action;

import de.d2dev.heroquest.engine.game.RunningGameContext;
import de.d2dev.heroquest.engine.game.Unit;

public abstract class GameActionAdapter implements GameAction {
	
	protected RunningGameContext context;
	protected Unit unit;

	public GameActionAdapter(RunningGameContext context, Unit unit) {
		super();
		this.context = context;
		this.unit = unit;
	}

	@Override
	public RunningGameContext getGameContext() {
		return this.context;
	}

	@Override
	public Unit getActingUnit() {
		return this.unit;
	}
}
