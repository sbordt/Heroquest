package de.d2dev.heroquest.editor;

import javax.swing.UIManager;

import de.d2dev.fourseasons.Application;
import de.d2dev.heroquest.editor.gui.*;
import de.d2dev.heroquest.engine.files.Files;
import de.d2dev.heroquest.engine.files.HqRessourceFile;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

public class Editor {
	
	public String publicDataStoragePath;
	
	public HqRessourceFile globalRessources;
	public EditorRessourceLocator resourceProvider;
	
	public QuadRenderModel renderModel;
	
	
	public Editor() {
		this.renderModel = new QuadRenderModel(500, 500);
		this.renderModel.addQuad( new RenderQuad( 10.0f, 10.0f, 100.0f, 100.0f, 0, "fields/yellow/yellow.bmp" ) );
	}
	
	public static void main(String[] args) throws Exception {
    	try  
    	{  
    	  //Tell the UIManager to use the platform look and feel  
    	  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
    	}  
    	catch(Exception e)  
    	{  
    	  //Do nothing  
    	} 
    	
    	Files.registerFileFormats();
    	
    	Editor editor = new Editor();
    	editor.publicDataStoragePath = Application.getPublicStoragePath("HeroQuest Editor");
    	
    	editor.globalRessources = new HqRessourceFile( editor.publicDataStoragePath + "/" + "globalRessources.zip" );
    	
    	editor.resourceProvider = new EditorRessourceLocator( editor );
 
		EditorMain main = new EditorMain( editor );
		main.setVisible(true);
	}
}
