package de.d2dev.heroquest.client;

import de.d2dev.heroquest.engine.files.HqMapFile;
import de.d2dev.heroquest.engine.gamestate.Map;

public class ClientApplication {
	
	private String settingsPath = "";
	
	private Map map;
	
	public ClientApplication() {
		
	}
	
	public ClientApplication(HqMapFile map) {
	}
	
	public void init() {
		
	}
	
	public void run() {
	}
	
	public static void main(String[] args) {
		ClientApplication app = new ClientApplication();
		app.init();
		app.run();
	}
}
