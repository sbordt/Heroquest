package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.fourseasons.util.ListenerUtil;
import de.d2dev.fourseasons.util.Observable;
import de.d2dev.heroquest.engine.game.action.GameAction;
import de.d2dev.heroquest.engine.game.action.MoveAction;

public abstract class GameContext implements Observable<GameListener> {
	
	protected ListenerUtil<GameListener> listeners = new ListenerUtil<GameListener>();
	
	public final Map map;
	
	protected GameContext(Map map) {
		super();
		this.map = map;
	}
	
	
	public void execute(GameAction action) throws GameStateException {
		action.excecute();
		
		if ( action instanceof MoveAction ) {
			this.fireOnMoveAction( (MoveAction) action );
		}
	}
	
	/**************************************************************************************
	 * 
	 * 						           GAME METHODS
	 * 
	 **************************************************************************************/
	
	
	public abstract void monsterAttackHero(Monster monster, Hero hero);
	
	public abstract void heroAttackMonster(Hero hero, Monster monster);
	
	

	/**************************************************************************************
	 * 
	 * 										OTHER METHODS
	 * 
	 **************************************************************************************/
	
	protected void fireOnMoveAction(MoveAction action) {
		for (GameListener l : listeners) {
			l.onMoveAction( action );
		}
	}
	
	protected void fireOnMonsterDies(Monster monster) {
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
