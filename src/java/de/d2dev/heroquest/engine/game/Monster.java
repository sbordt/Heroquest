package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;

public class Monster extends Unit {

	protected Monster(Field field) throws GameStateException {
		super(field);
		
		// This is a monster!
		this.type = Type.MONSTER;
	}

}
