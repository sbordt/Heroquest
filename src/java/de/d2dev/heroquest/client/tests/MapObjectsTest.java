package de.d2dev.heroquest.client.tests;

import de.d2dev.heroquest.client.ClientApplication;
import de.schlichtherle.truezip.file.TFile;

public class MapObjectsTest extends TestMap {

	public MapObjectsTest() throws Exception {
		super();
	}
	
	public static void main(String[] args) throws Exception {
		TestMap0 test = new TestMap0();
		
		ClientApplication app = new ClientApplication( new TFile( test.resources.dropbox.dropboxFolderPath + "/script/map templates/map tests/map_objects.lua" ), test.resources, test.settings, false );
		app.init();
		
		app.addTestHeroes(1);
		
		app.run();
	}
}
