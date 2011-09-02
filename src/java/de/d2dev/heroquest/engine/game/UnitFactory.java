package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.game.Hero.HeroType;
import de.d2dev.heroquest.engine.game.Monster.MonsterType;

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
	
	public Monster createOrc(Field field) throws GameStateException {
		return new Monster( field, MonsterType.ORC );
	}

}
