package de.d2dev.heroquest.editor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileOutputStream;

/**
 * Use a a {@code Properties} file for our editors settings.
 * @author Sebastian Bordt
 *
 */
public class EditorSettings {
	
	private static final String RENDERER = "Renderer";
	private static final String JME_RENDERER = "jme";
	private static final String JAVA2D_RENDERER = "java2D";
		
	public TFile file;
	
	public Properties properties = new Properties();
	
	private boolean useJmeRenderer = false;
	
	public EditorSettings(TFile file) throws IOException {
		this.file = file;
		
    	if ( file.exists() ) {
    		this.properties.loadFromXML( new FileInputStream( file ) );
    		
    		// renderer to use
    		if ( this.properties.getProperty( RENDERER, JAVA2D_RENDERER ).equals( JME_RENDERER) ) {
    			this.useJmeRenderer = true;
    		}
    	}
	}
	
	public boolean useJmeRenderer() {
		return this.useJmeRenderer;
	}
	
	public boolean useJava2DRenderer() {
		return !this.useJmeRenderer;
	}
	
	public void save() throws IOException {
		this.properties.setProperty( RENDERER, this.useJmeRenderer ? JME_RENDERER : JAVA2D_RENDERER );
		
		this.properties.storeToXML( new TFileOutputStream( this.file ), "Renderer: jme/java2D" );		
	}
	
}
