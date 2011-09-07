package de.d2dev.heroquest.editor;

import java.io.File;

import com.jme3.asset.AssetManager;

import de.d2dev.fourseasons.Application;
import de.d2dev.fourseasons.resource.TFileResourceFinder;
import de.d2dev.fourseasons.resource.util.TFileLocator;
import de.d2dev.heroquest.engine.files.HqRessourceFile;
import de.schlichtherle.truezip.file.TFile;

/**
 * Utility for having our dev data in a shared dropbox folder.
 * @author Sebastian Bordt
 *
 */
public class Dropbox {

	/**
	 * {@code null} if not found.
	 */
	public String dropboxFolderPath = null;
	
	/**
	 * {@code null} if not found.
	 */
	public String dropboxEditorFolder = null;
	
	/**
	 * {@code null} if not found.
	 */
	public HqRessourceFile dropboxResources = null;
	
	/**
	 * {@code null} if not found.
	 */	
	public TFile dropboxScriptFolder = null;
	
	/**
	 * {@code null} if not found.
	 */	
	public TFile dropboxAssetsFolder = null;
	
	public Dropbox() throws Exception {
    	// is there a dropbox storage location?	
    	if ( (this.dropboxFolderPath = Application.getFileSystemDropbox()) != null ) {
    		this.dropboxFolderPath += "/HeroQuest/HeroQuest Editor";
    		
    		if ( !new File( this.dropboxFolderPath ).exists() ) {
    			this.dropboxFolderPath = null;
    			return;
    		}
    	}
    	
    	if ( this.dropboxFolderPath != null ) {
    		// dropbox resources
    		this.dropboxResources = new HqRessourceFile( this.dropboxFolderPath + "/" + "globalResources.zip" );
    		
    		// dropbox script folder 'script'
    		this.dropboxScriptFolder = new TFile( this.dropboxFolderPath + "/script" );
    		
    		// dropbox assets folder 'globalAssets'
    		this.dropboxAssetsFolder = new TFile( this.dropboxFolderPath + "/assets" );
    	}				
	}
	
	/**
	 * Make a {@link TFileResourceFinder} find dropbox resources.
	 * @param finder
	 */
	public void addAsResourceLocation(TFileResourceFinder finder) {
		if ( this.dropboxFolderPath != null ) {
			this.dropboxResources.addAsResourceLocation( finder );
			
			finder.textureLocations.add( this.dropboxResources.textures );
			finder.luaScriptLocations.add( this.dropboxScriptFolder );
		}
	}
	
	/**
	 * Make JME find our dropbox resources.
	 * @param assetManager
	 */
	public void addAsResourceLocation(AssetManager assetManager) {
		if ( this.dropboxFolderPath != null ) {
			assetManager.registerLocator( this.dropboxAssetsFolder.getAbsolutePath(), TFileLocator.class );
		}
	}
	
}
