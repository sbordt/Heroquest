package de.d2dev.heroquest.editor;

import de.d2dev.fourseasons.Application;
import de.d2dev.fourseasons.resource.TFileResourceFinder;
import de.d2dev.heroquest.engine.files.HqRessourceFile;
import de.schlichtherle.truezip.file.TFile;

/**
 * This class provides access to the resources used by our editor -
 * also used by our tests!
 * @author Sebastian Bordt
 *
 */
public class EditorResources {
	
	public String publicDataStoragePath;
	
	public TFile textureFolder;
	public HqRessourceFile globalRessources;
	
	public Dropbox dropbox;
	
	public TFileResourceFinder resourceFinder = new TFileResourceFinder();
	
	public EditorResources() throws Exception {
		// get our public data storage location
    	this.publicDataStoragePath = Application.getPublicStoragePath("HeroQuest Editor");
    	
    	// textures folder 'textures'
    	this.textureFolder = new TFile( this.publicDataStoragePath + "/textures" );
    	
    	if ( this.textureFolder.exists() ) {
    		this.resourceFinder.textureLocations.add( this.textureFolder );
    	}
    	
    	// global resources are in globalResources.zip
    	this.globalRessources = new HqRessourceFile( this.publicDataStoragePath + "/" + "globalResources.zip" );
    	
    	this.resourceFinder.textureLocations.add( this.globalRessources.textures );
    	
    	// Dropbox setup
    	this.dropbox = new Dropbox();
    	this.dropbox.addAsResourceLocation( this.resourceFinder );
	}
}
