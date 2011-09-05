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
	
	/*
	 * Classical fight system
	 */
	protected int diceTempo;

	Hero(Field field, HeroType heroType) throws GameStateException {
		super(field);
		
		// This is a hero!
		this.type = Type.HERO;
		
		// Set the heroes type, name and fight values!
		this.heroType = heroType;
		
		switch ( this.heroType ) {
		case BARBARIAN:
			this.name = "Barbarian";
			
			this.diceTempo = 2;
			this.numAttackDices = 3;
			this.numDefenseDices = 2;
			this.bodyForce = 8;
			this.intelligence = 2;
			break;
		case DWARF:
			this.name = "Dwarf";
			
			this.diceTempo = 2;
			this.numAttackDices = 2;
			this.numDefenseDices = 2;
			this.bodyForce = 7;
			this.intelligence = 3;
			break;
		case ALB:
			this.name = "Alb";
			
			this.diceTempo = 2;
			this.numAttackDices = 2;
			this.numDefenseDices = 2;
			this.bodyForce = 6;
			this.intelligence = 4;
			break;
		case WIZARD:
			this.name = "Wizard";
			
			this.diceTempo = 2;
			this.numAttackDices = 1;
			this.numDefenseDices = 2;
			this.bodyForce = 4;
			this.intelligence = 6;
			break;
		}
	}

	public HeroType getHeroType() {
		return heroType;
	}
	
	/**************************************************************************************
	 * 
	 * 						      CLASSICAL FIGHT SYSTEM
	 * 
	 **************************************************************************************/
	
	public int getDiceTempo() {
		return diceTempo;
	}
	
}
