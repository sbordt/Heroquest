package de.d2dev.heroquest.engine.game;

public interface MapListener {
	
	public void onUnitEntersField(Field field);
	public void onUnitLeavesField(Field field);
	
	//public void onDoorOpens(Door door);
	//public void onRoomRevealed(Room room);
	
	public void onFieldTextureChanges(Field field);

}
