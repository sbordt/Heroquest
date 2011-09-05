package de.d2dev.heroquest.client.tests;

import de.d2dev.fourseasons.Application;
import de.d2dev.heroquest.editor.EditorResources;
import de.d2dev.heroquest.editor.EditorSettings;
import de.schlichtherle.truezip.file.TFile;

public class TestMap {	
	
	public EditorResources resources;
	public EditorSettings settings;
	
	public TestMap() throws Exception {
		Application.useNativeLookAndFeal();
		
		// use the editors resources
		this.resources = new EditorResources();
		
    	// use the editors settings
    	this.settings = new EditorSettings( new TFile ( this.resources.publicDataStoragePath, "settings.xml" ) );
	}
}
