package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;

public class Monster extends Unit {
	
	public enum MonsterType {
		ORC,
	}
	
	MonsterType monsterType;
	
	protected Monster(Field field, MonsterType monsterType) throws GameStateException {
		super(field);
		
		// This is a monster!
		this.type = Type.MONSTER;
		
		// Set the monsters type and name
		this.monsterType = monsterType;
		
		switch ( this.monsterType ) {
		case ORC:
			this.name = "Orc";
			break;
		}
	}

}
