package de.d2dev.heroquest.engine.game.action;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.game.Hero;
import de.d2dev.heroquest.engine.game.Monster;
import de.d2dev.heroquest.engine.game.Unit;

public class AttackAction extends GameActionAdapter {
	
	/**
	 * The unit to be attacked!
	 */
	private Unit unitToAttack;

	public AttackAction(Unit unit, Unit unitToAttack) {
		super(unit);
		
		this.unitToAttack = unitToAttack;
	}

	@Override
	public void excecute() throws GameStateException {
		// TODO check validity... can this attack happen anyway?!
		
		if ( this.unit.isHero() )
			context.heroAttackMonster( (Hero) unit, (Monster) unitToAttack);
		else
			context.monsterAttackHero( (Monster) unit, (Hero) unitToAttack);
	}

}
