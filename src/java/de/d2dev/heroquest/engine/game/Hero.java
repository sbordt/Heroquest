package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;

public class Hero extends Unit {
	
	public enum HeroType {
		BARBARIAN,
		DWARF,
		ALB,
		WIZARD,
	}
	
	protected HeroType heroType;

	Hero(Field field, HeroType heroType) throws GameStateException {
		super(field);
		
		// This is a hero!
		this.type = Type.HERO;
		
		// Set the heroes type and name
		this.heroType = heroType;
		
		switch ( this.heroType ) {
		case BARBARIAN:
			this.name = "Barbarian";
			break;
		case DWARF:
			this.name = "Dwarf";
			break;
		case ALB:
			this.name = "Alb";
			break;
		case WIZARD:
			this.name = "Wizard";
			break;
		}
	}

	public HeroType getHeroType() {
		return heroType;
	}
	
}
