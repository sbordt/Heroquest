package de.d2dev.heroquest.engine.ai.tests;

import de.d2dev.heroquest.client.ClientApplication;
import de.d2dev.heroquest.client.tests.TestMap;
import de.schlichtherle.truezip.file.TFile;

public class AITest1 {
	
	public static void main(String[] args) throws Exception {
		TestMap map = new TestMap();
		
		ClientApplication app = new ClientApplication( new TFile( map.resources.dropbox.dropboxFolderPath + "/script/map templates/ai tests/ai1.lua" ), map.resources, map.settings, false );
		app.init();
		app.run();
	}
}
