package de.d2dev.heroquest.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.UIManager;

import de.d2dev.fourseasons.Application;
import de.d2dev.heroquest.editor.gui.*;
import de.d2dev.heroquest.engine.Map;
import de.d2dev.heroquest.engine.files.Files;
import de.d2dev.heroquest.engine.files.HqRessourceFile;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;

public class Editor {
	
	public static String MAIN_WINDOW_X = "mainWndX";
	public static String MAIN_WINDOW_Y = "mainWndY";
	public static String JAVA_2D_WINDOW_X = "java2DWndX";
	public static String JAVA_2D_WINDOW_Y = "java2DWndY";
	public static String JAVA_2D_WINDOW_WIDTH = "java2DWndWidth";
	public static String JAVA_2D_WINDOW_HEIGHT = "java2DWndHeight";
	
	public String publicDataStoragePath;
	
	public Properties properties = new Properties();
	
	public HqRessourceFile globalRessources;
	public EditorRessourceLocator resourceProvider;
	
	public Map map;
	
	public Renderer renderer;
	public QuadRenderModel renderModel;
	
	public EditorMain mainWindow;
	
	public Editor() throws Exception {		
		this.initialize();
	}
	
	public void initialize() throws Exception {
		this.map = new Map( 10, 10 );
		
		this.renderer = new Renderer( this.map );
		this.renderer.render();
		
    	// register custom container file formats
    	Files.registerFileFormats();		
    	
    	// get our public data storage location
    	this.publicDataStoragePath = Application.getPublicStoragePath("HeroQuest Editor");
    	
    	// properties file contains gui related and other editor settings
    	File properties_file = new File ( this.publicDataStoragePath + "/" + "settings.xml" );
    	
    	if ( properties_file.exists() ) {
    		this.properties.loadFromXML( new FileInputStream( properties_file ) );
    	}
    	
    	// global resources are in globalRessources.zip
    	this.globalRessources = new HqRessourceFile( this.publicDataStoragePath + "/" + "globalRessources.zip" );
    	
    	// our resource locator
    	this.resourceProvider = new EditorRessourceLocator( this );
    	
    	// main window
    	this.mainWindow = new EditorMain( this );
    	
    	this.mainWindow.setLocation( Integer.valueOf( this.properties.getProperty( MAIN_WINDOW_X, "0" ) ),
    								 Integer.valueOf( this.properties.getProperty( MAIN_WINDOW_Y, "0" ) ) );
	}
	
	public void start() {
		this.mainWindow.setVisible( true );
	}
	
	public void close() throws Exception {
		// save properties to file
		this.properties.setProperty( MAIN_WINDOW_X, Integer.toString( this.mainWindow.getX() ) );
		this.properties.setProperty( MAIN_WINDOW_Y, Integer.toString( this.mainWindow.getY() ) );
		
		this.properties.setProperty( JAVA_2D_WINDOW_X, Integer.toString( this.mainWindow.java2DRenderWindow.getX() ) );
		this.properties.setProperty( JAVA_2D_WINDOW_Y, Integer.toString( this.mainWindow.java2DRenderWindow.getY() ) );
		
		this.properties.setProperty( JAVA_2D_WINDOW_WIDTH, Integer.toString( this.mainWindow.java2DRenderWindow.getWidth() ) );
		this.properties.setProperty( JAVA_2D_WINDOW_HEIGHT, Integer.toString( this.mainWindow.java2DRenderWindow.getHeight() ) );
		
		this.properties.storeToXML( new FileOutputStream( this.publicDataStoragePath + "/" + "settings.xml" ), "" );
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
    	
    	// start the editor
    	Editor editor = new Editor();
    	editor.start();
	}
}
