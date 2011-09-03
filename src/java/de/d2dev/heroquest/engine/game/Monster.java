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
	
	protected Monster(Field field, MonsterType monsterType) throws GameStateException {
		super(field);
		
		// This is a monster!
		this.type = Type.MONSTER;
		
		// Set the monsters type and name
		this.monsterType = monsterType;
		
		switch ( this.monsterType ) {
		case CHAOS_WARRIOR:
			this.name = "Chaos Warrior";
			break;
		case FIMIR:
			this.name = "Fimir";
			break;
		case GARGOYLE:
			this.name = "Gargoyle";
			break;
		case GOBLIN:
			this.name = "Goblin";
			break;
		case MUMMY:
			this.name = "Mummy";
			break;
		case ORC:
			this.name = "Orc";
			break;
		case SKELETON:
			this.name = "Skeleton";
			break;
		case ZOMBIE:
			this.name = "Zombie";
			break;
		}
	}

	public MonsterType getMonsterType() {
		return monsterType;
	}
	
}
