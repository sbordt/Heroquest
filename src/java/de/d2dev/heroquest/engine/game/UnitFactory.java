package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.ai.AISystem;
import de.d2dev.heroquest.engine.game.Hero.HeroType;
import de.d2dev.heroquest.engine.game.Monster.MonsterType;

/**
 * Utility class for unit creation.
 * @author Sebastian Bordt
 *
 */
public class UnitFactory {
	
	public Hero createBarbarian(Field field) throws GameStateException {
		return new Hero( field, HeroType.BARBARIAN );
	}
	
	public Hero createDwarf(Field field) throws GameStateException {
		return new Hero( field, HeroType.DWARF );
	}
	
	public Hero createAlb(Field field) throws GameStateException {
		return new Hero( field, HeroType.ALB );
	}
	
	public Hero createWizard(Field field) throws GameStateException {
		return new Hero( field, HeroType.WIZARD );
	}
	
	public Monster createMonster(Field field, MonsterType monsterType) throws GameStateException {
		return new Monster( field, monsterType );
	}
	
	/**
	 * Create a monster under control by a given {@link AISystem}.
	 * @param field
	 * @param monsterType
	 * @param system
	 * @return
	 * @throws GameStateException
	 */
	public Monster createMonster(Field field, MonsterType monsterType, AISystem system) throws GameStateException {
		Monster monster = new Monster( field, monsterType );
		monster.setAiController( system.creatAIController(monster) );
		
		return monster;
	}
	
	public Monster createGoblin(Field field) throws GameStateException {
		return new Monster( field, MonsterType.GOBLIN );
	}
	
	public Monster createOrc(Field field) throws GameStateException {
		return new Monster( field, MonsterType.ORC );
	}
	
	public Monster createFimir(Field field) throws GameStateException {
		return new Monster( field, MonsterType.FIMIR );
	}
	
	public Monster createSkeleton(Field field) throws GameStateException {
		return new Monster( field, MonsterType.SKELETON );
	}
	
	public Monster createZombie(Field field) throws GameStateException {
		return new Monster( field, MonsterType.ZOMBIE );
	}
	
	public Monster createMummy(Field field) throws GameStateException {
		return new Monster( field, MonsterType.MUMMY );
	}
	
	public Monster createChaosWarrier(Field field) throws GameStateException {
		return new Monster( field, MonsterType.CHAOS_WARRIOR );
	}

	public Monster createGargoyle(Field field) throws GameStateException {
		return new Monster( field, MonsterType.GARGOYLE );
	}

}
