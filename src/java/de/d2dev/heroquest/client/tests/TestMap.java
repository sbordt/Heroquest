package de.d2dev.heroquest.client.tests;

import de.d2dev.fourseasons.resource.TFileResourceFinder;
import de.d2dev.heroquest.editor.Dropbox;

public class TestMap {	
	
	public Dropbox dropbox;
	public TFileResourceFinder resourceFinder = new TFileResourceFinder();
	
	public TestMap() throws Exception {
		// dropbox setup
		this.dropbox = new Dropbox();
		this.dropbox.addAsResourceLocation( resourceFinder );
	}
}
