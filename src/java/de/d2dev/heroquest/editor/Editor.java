package de.d2dev.heroquest.editor;

import javax.swing.UIManager;

import de.d2dev.fourseasons.Application;
import de.d2dev.heroquest.editor.gui.*;
import de.d2dev.heroquest.engine.Map;
import de.d2dev.heroquest.engine.files.Files;
import de.d2dev.heroquest.engine.files.HqRessourceFile;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;

public class Editor {
	
	public String publicDataStoragePath;
	
	public HqRessourceFile globalRessources;
	public EditorRessourceLocator resourceProvider;
	
	public Map map;
	
	public Renderer renderer;
	public QuadRenderModel renderModel;
	
	public Editor() {
		this.map = new Map( 10, 10 );
		
		this.renderer = new Renderer( this.map );
		this.renderer.render();
		
		this.renderModel = this.renderer.getRederTarget();
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
