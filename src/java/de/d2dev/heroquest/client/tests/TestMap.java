package de.d2dev.heroquest.client.tests;

import de.d2dev.fourseasons.Application;
import de.d2dev.fourseasons.resource.TFileResourceFinder;
import de.d2dev.heroquest.editor.Dropbox;
import de.d2dev.heroquest.engine.files.HqRessourceFile;
import de.schlichtherle.truezip.file.TFile;

public class TestMap {	
	
	public String publicDataStoragePath;

	public TFile scriptFolder;
	public HqRessourceFile globalRessources;
	
	public Dropbox dropbox;
	
	public TFileResourceFinder resourceFinder = new TFileResourceFinder();
	
	public TestMap() throws Exception {
		// use the editors public data storage path
		this.publicDataStoragePath = Application.getPublicStoragePath("HeroQuest Editor");
		
    	// global resources are in globalResources.zip
    	this.globalRessources = new HqRessourceFile( this.publicDataStoragePath + "/" + "globalResources.zip" );
    	
    	this.resourceFinder.textureLocations.add( this.globalRessources.textures );
    	
    	// script folder 'script'
    	this.scriptFolder = new TFile( this.publicDataStoragePath + "/script" );
    	
    	this.resourceFinder.luaScriptLocations.add( this.scriptFolder );
		
		// dropbox setup
		this.dropbox = new Dropbox();
		this.dropbox.addAsResourceLocation( resourceFinder );
	}
}
