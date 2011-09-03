package de.d2dev.heroquest.client.tests;

import de.d2dev.heroquest.client.ClientApplication;
import de.schlichtherle.truezip.file.TFile;

public class TestMap0 extends TestMap {
	
	public TestMap0() throws Exception {
		super();
	}

	public static void main(String[] args) throws Exception {
		TestMap0 test = new TestMap0();
                
                System.out.println( new TFile( test.dropbox.dropboxFolderPath + "/script/map templates/level0.lua" ));
		
                for( TFile f : test.resourceFinder.luaScriptLocations ) {
                    System.out.println( f.getAbsolutePath() );
                }
                
		ClientApplication app = new ClientApplication( new TFile( test.dropbox.dropboxFolderPath + "/script/map templates/level0.lua" ), test.resourceFinder );
		app.init();
		
		app.addTestMonsters(3);
		app.addTestHeroes(1);
		
		app.run();
	}
}