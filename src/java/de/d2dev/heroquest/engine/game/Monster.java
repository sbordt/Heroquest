package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;

public class Monster extends Unit {
	
	public enum MonsterType {
		GOBLIN,
		ORC,
		FIMIR,
		
		SKELETON,
		ZOMBIE,
		MUMMY,
		
		CHAOS_WARRIOR,
		GARGOYLE,
	}
	
	MonsterType monsterType;
	
	/*
	 * Classical fight system
	 */
	protected int fieldTempo;
	
	protected Monster(Field field, MonsterType monsterType) throws GameStateException {
		super(field);
		
		// This is a monster!
		this.type = Type.MONSTER;
		
		// Set the monsters type, name and fight values!
		this.monsterType = monsterType;
		
		switch ( this.monsterType ) {
		case CHAOS_WARRIOR:
			this.name = "Chaos Warrior";
			
			this.fieldTempo = 6;
			this.numAttackDices = 3;
			this.numDefenseDices = 4;
			this.bodyForce = 1;
			this.intelligence = 3;
			break;
		case FIMIR:
			this.name = "Fimir";
			
			this.fieldTempo = 6;
			this.numAttackDices = 3;
			this.numDefenseDices = 3;
			this.bodyForce = 1;
			this.intelligence = 3;
			break;
		case GARGOYLE:
			this.name = "Gargoyle";
			
			this.fieldTempo = 6;
			this.numAttackDices = 4;
			this.numDefenseDices = 4;
			this.bodyForce = 1;
			this.intelligence = 4;
			break;
		case GOBLIN:
			this.name = "Goblin";
			
			this.fieldTempo = 10;
			this.numAttackDices = 2;
			this.numDefenseDices = 1;
			this.bodyForce = 1;
			this.intelligence = 1;
			break;
		case MUMMY:
			this.name = "Mummy";
			
			this.fieldTempo = 4;
			this.numAttackDices = 3;
			this.numDefenseDices = 4;
			this.bodyForce = 1;
			this.intelligence = 0;
			break;
		case ORC:
			this.name = "Orc";
			
			this.fieldTempo = 8;
			this.numAttackDices = 3;
			this.numDefenseDices = 2;
			this.bodyForce = 1;
			this.intelligence = 2;
			break;
		case SKELETON:
			this.name = "Skeleton";
			
			this.fieldTempo = 6;
			this.numAttackDices = 2;
			this.numDefenseDices = 2;
			this.bodyForce = 1;
			this.intelligence = 0;
			break;
		case ZOMBIE:
			this.name = "Zombie";
			
			this.fieldTempo = 4;
			this.numAttackDices = 2;
			this.numDefenseDices = 3;
			this.bodyForce = 1;
			this.intelligence = 0;
			break;
		}
	}

	public MonsterType getMonsterType() {
		return monsterType;
	}
	
	/**************************************************************************************
	 * 
	 * 						      CLASSICAL FIGHT SYSTEM
	 * 
	 **************************************************************************************/
	
	public int getFieldTempo() {
		return fieldTempo;
	}
	
}
