package de.d2dev.heroquest.client.tests;

import de.d2dev.heroquest.client.ClientApplication;
import de.schlichtherle.truezip.file.TFile;

public class Demo0 {
	
	public static void main(String[] args) throws Exception {
		TestMap map = new TestMap();
		
		ClientApplication app = new ClientApplication( new TFile( map.resources.dropbox.dropboxFolderPath + "/script/map templates/demo0.lua" ), map.resources.resourceFinder, false );
		app.init();
		
		//app.addTestMonsters(3);
		app.addTestHeroes(1);
		
		app.run();
	}
}