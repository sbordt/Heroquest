package de.d2dev.heroquest.engine.game;

public interface MapListener {
	
	public void onUnitEntersField(Field field);
	public void onUnitLeavesField(Field field);
	
	public void onFieldRevealed(Field field);
	
	public void onDoorOpened(Door door);
	
	/**
	 * Will be fired after the rooms individual fields have been revealed.
	 * @param room
	 */
	public void onRoomRevealed(Room room);
	
	public void onFieldTextureChanges(Field field);

}
