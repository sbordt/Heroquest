package de.d2dev.heroquest.engine.game;

public class Game {
	
	public enum State {
		HEROES_ROUND,
		MONSTER_ROUND,
	}
	
	public State round;
	
	Map map;
	
	public void heroesRound() {}
	public void monstersRound() {}
}
