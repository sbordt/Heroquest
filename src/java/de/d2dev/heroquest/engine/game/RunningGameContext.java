package de.d2dev.heroquest.engine.game;

import de.d2dev.heroquest.engine.game.action.GameAction;

public class RunningGameContext {
	
	public enum State {
		HEROES_ROUND,
		MONSTER_ROUND,
	}
	
	public final Map map;
	
	public RunningGameContext(Map map) {
		super();
		this.map = map;
	}
	
	public void execute(GameAction action) {
		
	}
}
