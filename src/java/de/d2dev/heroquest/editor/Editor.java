package de.d2dev.heroquest.editor;

import de.d2dev.fourseasons.Application;
import de.d2dev.fourseasons.script.ScriptEngine;

import de.d2dev.heroquest.editor.gui.*;
import de.d2dev.heroquest.editor.script.EditorLuaScriptDecomposer;
import de.d2dev.heroquest.editor.script.LuaMapCreatorFunction;
import de.d2dev.heroquest.engine.files.Files;
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
	
	public EditorResources resources;
	
	public EditorSettings settings;
	
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
    
    	// Aquire resources
    	this.resources = new EditorResources();
    	
    	// Load settings - settings contains gui related and other editor settings
    	this.settings = new EditorSettings( new TFile ( this.resources.publicDataStoragePath, "settings.xml" ) );
    	
    	// script engine setup
    	scriptEngine = ScriptEngine.createDefaultScriptEngine( this.resources.resourceFinder, new EditorLuaScriptDecomposer() );
    	
    	// load the 'classical' map
    	try {    		
    		LuaMapCreatorFunction function = (LuaMapCreatorFunction) scriptEngine.load( new TFile( this.resources.dropbox.dropboxFolderPath + "/script/map templates/level0.lua" ) ).getFunctions().get(0);
    		
    		this.map = function.createMap();
    	} catch(Exception e) {
    		e.printStackTrace();
    		
    		this.map = new Map( 10, 10 );	// dummy map
    	}
    	
    	// main window
    	this.mainWindow = new EditorMain( this );
    	
    	this.mainWindow.setLocation( Integer.valueOf( this.settings.properties.getProperty( MAIN_WINDOW_X, "0" ) ),
    								 Integer.valueOf( this.settings.properties.getProperty( MAIN_WINDOW_Y, "0" ) ) );
    	
    	// init renderer
    	this.renderTarget = new QuadRenderModel( this.map.getWidth(), this.map.getHeight() );
    	
		this.renderer = new Renderer( this.map, this.renderTarget, this.resources.resourceFinder, false );
		this.renderer.render();
	}
	

	
	public void start() {
		this.mainWindow.setVisible( true );
	}
	
	public void close() throws Exception {
		// save properties to file
		this.settings.properties.setProperty( MAIN_WINDOW_X, Integer.toString( this.mainWindow.getX() ) );
		this.settings.properties.setProperty( MAIN_WINDOW_Y, Integer.toString( this.mainWindow.getY() ) );
		
		if ( this.mainWindow.java2DRenderWindow != null ) {
			this.settings.properties.setProperty( JAVA_2D_WINDOW_X, Integer.toString( this.mainWindow.java2DRenderWindow.getX() ) );
			this.settings.properties.setProperty( JAVA_2D_WINDOW_Y, Integer.toString( this.mainWindow.java2DRenderWindow.getY() ) );
			
			this.settings.properties.setProperty( JAVA_2D_WINDOW_WIDTH, Integer.toString( this.mainWindow.java2DRenderWindow.getWidth() ) );
			this.settings.properties.setProperty( JAVA_2D_WINDOW_HEIGHT, Integer.toString( this.mainWindow.java2DRenderWindow.getHeight() ) );
		}
		
		this.settings.save();
	}
	
	public static void main(String[] args) throws Exception {
		Application.useNativeLookAndFeal();
    	
    	// start the editor
    	Editor editor = new Editor();
    	editor.initialize();
    	editor.start();
	}
	
}
