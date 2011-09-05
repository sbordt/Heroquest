package de.d2dev.heroquest.engine.game.action;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.game.ClassicalGameUtil;
import de.d2dev.heroquest.engine.game.Hero;
import de.d2dev.heroquest.engine.game.Monster;
import de.d2dev.heroquest.engine.game.RunningGameContext;
import de.d2dev.heroquest.engine.game.Unit;

public class AttackAction extends GameActionAdapter {
	
	/**
	 * The unit to be attacked!
	 */
	private Unit unitToAttack;

	public AttackAction(RunningGameContext context, Unit unit) {
		super(context, unit);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void excecute() throws GameStateException {
		// TODO check validity... can this attack happen anyway?!
		
		if ( this.unit.isHero() )
			ClassicalGameUtil.heroAttackMonster( (Hero) unit, (Monster) unitToAttack);
		else
			ClassicalGameUtil.monsterAttackHero( (Monster) unit, (Hero)unitToAttack);
	}

}
