package de.d2dev.heroquest.engine.game.mapobjects;

import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.MapObject;
import de.d2dev.heroquest.engine.game.Rotation2D;

public class Blockade extends MapObject {

	public Blockade(Map map, int x, int y) {
		super( map, x, y, Rotation2D.NONE, 1, 1, TextureResource.createTextureResource("objects/blockade.png") );
	}

	@Override
	protected boolean blocksFieldObjectSpace(int x, int y) {
		return true;
	}

}
