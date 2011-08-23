package de.d2dev.heroquest.editor;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
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
		
		// textures
		if ( r.getType().isType( TextureResource.TYPENAME ) ) {
			if ( ( resource = new TFile( this.editor.globalRessources.textures.getAbsolutePath() + "/" + r.getName() ) ).exists() ) {
				return resource.getAbsolutePath();
			}
			
		}
		
		return null;
	}
}
