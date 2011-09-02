package de.d2dev.heroquest.engine.game;

public class Door {
	
	private Field field;
	
	private boolean isOpen;
	
	Door(Field field) {
		this.field = field;
		
		this.isOpen = false;
	}
	
	/**
	 * The map the door belongs to.
	 * @return
	 */
	public Map getMap() {
		return this.field.getMap();
	}
	
	/**
	 * The field the door is standing on.
	 * @return
	 */
	public Field getField() {
		return this.field;
	}

	public boolean isOpen() {
		return this.isOpen;
	}
	
	public boolean isClosed() {
		return !this.isOpen;
	}
	
	public void open() {
		// TODO method
	}
}
