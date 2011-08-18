package de.d2dev.heroquest.engine.rendering;

import java.util.Iterator;

import de.d2dev.heroquest.engine.Direction;
import de.d2dev.heroquest.engine.DoorType;

/**
 * The {@code RenderField} is part of our {@link RenderModel}. The render model
 * is composed by {@code RenderField}'s. 
 * @author Sebastian Bordt
 *
 */
public interface RenderField {
	
	public boolean isRevealed();
	
	public boolean isWall();
	
	public boolean hasUpperWall();
	public boolean hasLowerWall();
	public boolean hasLeftWall();
	public boolean hasRightWall();
	
	public String getUpperRightTexture();
	public String getUpperLeftTexture();
	public String getLowerLeftTexture();
	public String getLowerRightTexture();
	
	public boolean isDoor();
	public DoorType getDoorType();
	public boolean isDoorOpen();
	
	public Iterator<String> getTextures();
	
	public boolean hasMonster();	// Helden sind auch Monster
	public String getMonsterType();
	public Direction getViewDirection();
}
