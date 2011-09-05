package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.fourseasons.util.ListenerUtil;
import de.d2dev.fourseasons.util.Observed;
import de.d2dev.heroquest.engine.game.action.GameAction;
import de.d2dev.heroquest.engine.game.action.MoveAction;

public class GameContext implements Observed<GameListener> {
	
	private ListenerUtil<GameListener> listeners = new ListenerUtil<GameListener>();
	
	public final Map map;
	
	public GameContext(Map map) {
		super();
		this.map = map;
	}
	
	
	public void execute(GameAction action) throws GameStateException {
		action.excecute();
		
		if ( action instanceof MoveAction ) {
			this.fireOnMoveAction( (MoveAction) action );
		}
	}
	
	
	void fireOnMoveAction(MoveAction action) {
		for (GameListener l : listeners) {
			l.onMoveAction( action );
		}
	}
	
	void fireOnMonsterDies(Monster monster) {
		for (GameListener l : listeners) {
			l.onMonsterDies( monster );
		}
	}

	@Override
	public void addListener(GameListener l) {
		listeners.addListener(l);
	}

	@Override
	public void removeListener(GameListener l) {
		listeners.addListener(l);
	}

}
