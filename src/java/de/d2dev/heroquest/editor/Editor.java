package de.d2dev.heroquest.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.UIManager;

import de.d2dev.fourseasons.Application;
import de.d2dev.fourseasons.resource.TFileResourceFinder;
import de.d2dev.fourseasons.script.ScriptEngine;

import de.d2dev.heroquest.editor.gui.*;
import de.d2dev.heroquest.editor.script.EditorLuaScriptDecomposer;
import de.d2dev.heroquest.editor.script.LuaMapCreatorFunction;
import de.d2dev.heroquest.engine.files.Files;
import de.d2dev.heroquest.engine.files.HqRessourceFile;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;

import de.schlichtherle.truezip.file.TFile;

public class Editor {
	
	public static String MAIN_WINDOW_X = "mainWndX";
	public static String MAIN_WINDOW_Y = "mainWndY";
	public static String JAVA_2D_WINDOW_X = "java2DWndX";
	public static String JAVA_2D_WINDOW_Y = "java2DWndY";
	public static String JAVA_2D_WINDOW_WIDTH = "java2DWndWidth";
	public static String JAVA_2D_WINDOW_HEIGHT = "java2DWndHeight";
	
	public String publicDataStoragePath;
	
	public Dropbox dropbox;
	
	public Properties properties = new Properties();
	
	public TFile scriptFolder;
	public HqRessourceFile globalRessources;
	
	public TFileResourceFinder resourceProvider = new TFileResourceFinder();
	
	public ScriptEngine scriptEngine;
	
	public Map map;
	
	public Renderer renderer;
	public QuadRenderModel renderTarget;
	
	public EditorMain mainWindow;
	
	public Editor() throws Exception {		
	}
	
	public void initialize() throws Exception {		
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
    	this.globalRessources = new HqRessourceFile( this.publicDataStoragePath + "/" + "globalResources.zip" );
    	
    	this.resourceProvider.textureLocations.add( this.globalRessources.textures );
    	
    	// script folder 'script'
    	this.scriptFolder = new TFile( this.publicDataStoragePath + "/script" );
    	
    	this.resourceProvider.luaScriptLocations.add( this.scriptFolder );
    	
    	// Dropbox setup
    	this.dropbox = new Dropbox();
    	this.dropbox.addAsResourceLocation( this.resourceProvider );
    	
    	// script engine setup
    	scriptEngine = ScriptEngine.createDefaultScriptEngine( this.resourceProvider, new EditorLuaScriptDecomposer() );
    	
    	// load the 'classical' map
    	try {    		
    		LuaMapCreatorFunction function = (LuaMapCreatorFunction) scriptEngine.load( new TFile( this.dropbox.dropboxFolderPath + "/script/map templates/classical.lua" ) ).getFunctions().get(0);
    		
    		this.map = function.createMap();
    	} catch(Exception e) {
    		e.printStackTrace();
    		
    		this.map = new Map( 10, 10 );	// dummy map
    	}
    	
    	// main window
    	this.mainWindow = new EditorMain( this );
    	
    	this.mainWindow.setLocation( Integer.valueOf( this.properties.getProperty( MAIN_WINDOW_X, "0" ) ),
    								 Integer.valueOf( this.properties.getProperty( MAIN_WINDOW_Y, "0" ) ) );
    	
    	// init renderer
    	this.renderTarget = new QuadRenderModel( this.map.getWidth(), this.map.getHeight() );
    	
		this.renderer = new Renderer( this.map, this.renderTarget, this.resourceProvider );
		this.renderer.render();
	}
	

	
	public void start() {
		this.mainWindow.setVisible( true );
	}
	
	public void close() throws Exception {
		// save properties to file
		this.properties.setProperty( MAIN_WINDOW_X, Integer.toString( this.mainWindow.getX() ) );
		this.properties.setProperty( MAIN_WINDOW_Y, Integer.toString( this.mainWindow.getY() ) );
		
		if ( this.mainWindow.java2DRenderWindow != null ) {
			this.properties.setProperty( JAVA_2D_WINDOW_X, Integer.toString( this.mainWindow.java2DRenderWindow.getX() ) );
			this.properties.setProperty( JAVA_2D_WINDOW_Y, Integer.toString( this.mainWindow.java2DRenderWindow.getY() ) );
			
			this.properties.setProperty( JAVA_2D_WINDOW_WIDTH, Integer.toString( this.mainWindow.java2DRenderWindow.getWidth() ) );
			this.properties.setProperty( JAVA_2D_WINDOW_HEIGHT, Integer.toString( this.mainWindow.java2DRenderWindow.getHeight() ) );
		}

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
    	editor.publicDataStoragePath = Application.getPublicStoragePath("HeroQuest Editor");
    	editor.initialize();
    	editor.start();
	}
	
}
