package de.d2dev.heroquest.editor;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.LuaResource;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.schlichtherle.truezip.file.TFile;

public class EditorRessourceLocator implements ResourceLocator {
	
	public final Editor editor;
	
	public EditorRessourceLocator(Editor editor) {
		this.editor = editor;
	}

	@Override
	public String getAbsoluteLocation(Resource r) {
		TFile resource;
		
		// absolute path - general
		if ( ( resource = new TFile( r.getName() ) ).exists() ) {
			return resource.getAbsolutePath();
		}
		
		// textures
		if ( r.getType().isType( TextureResource.TYPENAME ) ) {
			if ( ( resource = new TFile( this.editor.globalRessources.textures.getAbsolutePath() + "/" + r.getName() ) ).exists() ) {
				return resource.getAbsolutePath();
			}
			
			// dropbox global resources
			if ( this.editor.dropboxResources != null ) {
				if ( ( resource = new TFile( this.editor.dropboxResources.textures.getAbsolutePath() + "/" + r.getName() ) ).exists() ) {
					return resource.getAbsolutePath();
				}
			}
		}
		
		// lua script
		if ( r.getType().isType( LuaResource.TYPENAME ) ) {
			if ( ( resource = new TFile( this.editor.scriptFolder.getAbsolutePath() + "/" + r.getName() ) ).exists() ) {
				return resource.getAbsolutePath();
			}
			
			// dropbox
			if ( this.editor.dropboxScriptFolder != null ) {
				if ( ( resource = new TFile( this.editor.dropboxScriptFolder.getAbsolutePath() + "/" + r.getName() ) ).exists() ) {
					return resource.getAbsolutePath();
				} 
			}
			
		}
		
		System.out.println( "Resource '" + r.getName() + "' of type '" + r.getType().getTypeName() + "' could not be found!" );
		
		return null;
	}
}
