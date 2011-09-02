package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;

public class Hero extends Unit {
	
	public enum HeroType {
		BARBARIAN,
		DWARF,
		ALB,
		WIZZARD,
	}
	
	protected HeroType heroType;

	Hero(Field field, HeroType heroType) throws GameStateException {
		super(field);
		
		// This is a hero!
		this.type = Type.HERO;
		
		this.heroType = heroType;
		
		switch ( this.heroType ) {
		case BARBARIAN:
		}
	}

}
