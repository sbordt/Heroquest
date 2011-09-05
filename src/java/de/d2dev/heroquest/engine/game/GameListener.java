package de.d2dev.heroquest.engine.game;

import de.d2dev.heroquest.engine.game.action.AttackAction;
import de.d2dev.heroquest.engine.game.action.MoveAction;

public interface GameListener {
	
	public void onMoveAction(MoveAction action);
	public void onAttackAction(AttackAction action);
	
	public void onHeroAttacksMonster(Hero hero, Monster monster);
	public void onMonsterAttacksHero(Monster monster, Hero hero);
	
	public void onHeroKillsMonster(Hero hero, Monster monster);
	public void onMonsterKillsHero(Monster monster, Hero hero);
	
	public void onHeroHeroDies(Hero hero);
	public void onMonsterDies(Monster monster);

}
