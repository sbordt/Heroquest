package de.d2dev.heroquest.engine.game;

import java.util.Random;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

/**
 * This utility class provides static methods to run a classical 
 * HeroQuest game!
 * @author Sebastian Bordt
 *
 */
public class ClassicalGameUtil {
	
	public enum HeroQuestDice {
		ATTACK,
		HERO_DEFENSE,
		MONSTER_DEFENSE,
	}
	
	public static final String GAME_LOGGER_NAME = "game";
	
	/**
	 * Roll a dice with 6 sites.
	 * @return
	 */
	public static int rollW6() {
		return new Random().nextInt( 6 );
	}
	
	public static HeroQuestDice rollHeroQuestDice() {
		int result = ClassicalGameUtil.rollW6();
		
		if ( result == 0 )	// 0 = monster defense
			return HeroQuestDice.MONSTER_DEFENSE;
		
		if ( result <= 2 )	// 1,2 = hero defense
			return HeroQuestDice.HERO_DEFENSE;
		
		return HeroQuestDice.ATTACK;	// 3,4,5,6 attack
	}
	
	public static HeroQuestDice[] roolHeroQuestDices(int number) {
		Preconditions.checkArgument( number >= 0 );
		
		HeroQuestDice[] dices = new HeroQuestDice[ number ];
		
		for (int i=0; i<number; i++) {
			dices[i] = ClassicalGameUtil.rollHeroQuestDice();
		}
		
		return dices;
	}
	
	public static int getDiceAppearence(HeroQuestDice[] dices, HeroQuestDice dice) {
		int n = 0;
		
		for (int i=0; i<dices.length; i++) {
			if ( dices[i] == dice ) {
				n++;
			}
		}		
		
		return n;
	}
	
	public static int rollAttackDices(int number) {
		return ClassicalGameUtil.getDiceAppearence(  ClassicalGameUtil.roolHeroQuestDices( number ), HeroQuestDice.ATTACK );
	}
	
	public static int rollHeroDefenceDices(int number) {
		return ClassicalGameUtil.getDiceAppearence(  ClassicalGameUtil.roolHeroQuestDices( number ), HeroQuestDice.HERO_DEFENSE );
	}
	
	public static int rollMonsterDefenceDices(int number) {
		return ClassicalGameUtil.getDiceAppearence(  ClassicalGameUtil.roolHeroQuestDices( number ), HeroQuestDice.MONSTER_DEFENSE );
	}
	
	public static void monsterAttackHero(Monster monster, Hero hero) {
		Logger logger = Logger.getLogger( "game" );
		
		int attack = ClassicalGameUtil.rollAttackDices( monster.getNumAttackDices() );
		int defense = ClassicalGameUtil.rollHeroDefenceDices( hero.getNumDefenseDices() );
		
		// no damage
		if ( defense >= attack ) {
			logger.info( monster.getName() + " attacked " + hero.getName() + " but dealt no damage." );
			return;
		}
		
		// deal damage!
		int damage = attack - defense;
		
		logger.info( monster.getName() + " attacked " + hero.getName() + " and dealt " + damage + " damage." );
		
		// hero survives
		if ( hero.bodyForce > damage ) {
			hero.bodyForce -= damage;
			return;
		}
		
		// hero dies!
		hero.bodyForce = 0;
		
		logger.info( hero.getName() + " dies!" );
		
		hero.getMap().removeUnit( hero );
	}
	
	public static void heroAttackMonster(Hero hero, Monster monster) {
		Logger logger = Logger.getLogger( "game" );
		
		int attack = ClassicalGameUtil.rollAttackDices( hero.getNumAttackDices() );
		int defense = ClassicalGameUtil.rollMonsterDefenceDices( monster.getNumDefenseDices() );
		
		// no damage
		if ( defense >= attack ) {
			logger.info( hero.getName() + " attacked " + monster.getName() + " but dealt no damage." );
			return;
		}
		
		// deal damage!
		int damage = attack - defense;
		
		logger.info( hero.getName() + " attacked " + monster.getName() + " and dealt " + damage + " damage." );
		
		// monster survives (usually not the case anyway)
		if ( monster.bodyForce > damage ) {
			monster.bodyForce -= damage;
			return;
		}
		
		// monster dies!
		monster.bodyForce = 0;
		
		logger.info( monster.getName() + " dies!" );	
		
		monster.getMap().removeUnit( monster );
	}
}
