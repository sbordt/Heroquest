package de.d2dev.heroquest.engine.game.mapobjects;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.game.Map;

public class MapObjectFactory {
	
	private Map map;
	
	public MapObjectFactory(Map map) {
		this.map = map;
	}

	public Map getMap() {
		return map;
	}
	
	public void createBlockade(int x, int y) throws GameStateException {
		Blockade blockade = new Blockade( this.map, x, y );
		this.map.addMapObject(blockade);
	}
	
}
