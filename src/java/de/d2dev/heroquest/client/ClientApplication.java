package de.d2dev.heroquest.client;


import de.d2dev.fourseasons.gamestate.GameStateException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.script.ScriptEngine;
import de.d2dev.heroquest.editor.EditorResources;
import de.d2dev.heroquest.editor.EditorSettings;
import de.d2dev.heroquest.editor.script.EditorLuaScriptDecomposer;
import de.d2dev.heroquest.editor.script.LuaMapCreatorFunction;
import de.d2dev.heroquest.engine.ai.AISystem;
import de.d2dev.heroquest.engine.files.HqMapFile;
import de.d2dev.heroquest.engine.game.*;
import de.d2dev.heroquest.engine.game.Hero.HeroType;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Monster.MonsterType;
import de.d2dev.heroquest.engine.game.action.*;
import de.d2dev.heroquest.engine.game.classical.ClassicalGameContext;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.sound.JmeSoundPlayer;
import de.schlichtherle.truezip.file.TFile;

import java.util.*;

import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ClientApplication implements KeyListener, WindowListener {

//    private String settingsPath = "";
	private EditorSettings settings;
	private EditorResources resources;
	
    private boolean fogOfWar;
	
    private ScriptEngine scriptEngine;    
    
    private ClassicalGameContext game;
    private Map map;
    
    private ResourceLocator resourceFinder;
    
    private Renderer renderer;
    private QuadRenderModel renderTarget;
    private ClientWindow window;
    
    private JmeSoundPlayer soundPlayer;
    
    private UnitFactory unitFactory;
    
    private AISystem aiSystem;
        
    private boolean heroesRound = true;
    
    private Hero activeHero = null;
    
    private Monster activeMonster = null;
    private List<GameAction> actionsToPerform;
    
	/**************************************************************************************
	 * 
	 * 						  		INITIALIZATION/SETUP
	 * 
	 **************************************************************************************/    

    public ClientApplication(TFile mapCreatorScript, EditorResources resources, EditorSettings settings, boolean fogOfWar) throws Exception {
    	this.resources = resources;
    	this.initResourceFinder( this.resources.resourceFinder );
    	
    	this.fogOfWar = fogOfWar;
    	
    	this.settings = settings;
    	
    	// load the map creator script  		
    	LuaMapCreatorFunction function = (LuaMapCreatorFunction) scriptEngine.load( mapCreatorScript ).getFunctions().get(0);
    	
    	// run the map creator script  	
    	this.map = function.createMap();
    }

    public ClientApplication(HqMapFile map, ResourceLocator resourceFinder) {
    	this.initResourceFinder(resourceFinder);
    	
        this.map = new Map(map.map.getRootElement());
        this.resourceFinder = resourceFinder;
    }
    
    public void initResourceFinder(ResourceLocator resourceFinder) {
    	// set the given resource finder
    	this.resourceFinder = resourceFinder;
    	
    	// log4j
    	BasicConfigurator.configure();
    	Logger.getRootLogger().removeAllAppenders();
    	    	
    	// script engine setup
    	scriptEngine = ScriptEngine.createDefaultScriptEngine( this.resourceFinder, new EditorLuaScriptDecomposer() );
    }
    
    public void init() throws Exception {
    	// game context - add to map
    	this.game = new ClassicalGameContext( map );
    	this.map.setContext( this.game );
    	
    	// sound player setup
    	this.soundPlayer = new JmeSoundPlayer( this.resources.assestManager );
    	this.game.addListener( soundPlayer );
    	
    	// initialize the ai system - add all existing monsters 
        this.aiSystem = new AISystem(map);
        
        for (Monster m : map.getMonsters()) {
        	if ( m.getAIController() == null ) {
        		m.setAiController( this.aiSystem.creatAIController( m ) );
        	}
        }
        
        // select some hero if there is one on the map
        if ( !this.map.getHeroes().isEmpty() ) {
        	this.activeHero = this.map.getHeroes().get( 0 );
        }

    	this.unitFactory = new UnitFactory();
    }
    
    public void addTestMonsters(int numMonsters) {
    	MonsterType[] monsterTypes = { MonsterType.GOBLIN, MonsterType.ORC, MonsterType.FIMIR, MonsterType.SKELETON,
    			MonsterType.ZOMBIE, MonsterType.MUMMY, MonsterType.CHAOS_WARRIOR, MonsterType.GARGOYLE };
    	
    	if ( numMonsters <= 0 )
    		return;
    	
    	if ( numMonsters > 8 )
    		numMonsters = 8;
    	
    	for (int i=0; i<numMonsters; i++) {
    		try {
    			this.unitFactory.createMonster( this.map.getField( 34, 10 + i), monsterTypes[i], this.aiSystem );
			} catch (GameStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void addTestHeroes(int numHeroes) {
    	if ( numHeroes <= 0 )
    		return;
    	
    	if ( numHeroes > 4 )
    		numHeroes = 4;
    	
    	try {
    		this.unitFactory.createBarbarian( this.map.getField(0, 0) );
    		this.activeHero = (Hero) this.map.getHeroes().get(0);
    		
    		if ( numHeroes > 1)
    			this.unitFactory.createDwarf( this.map.getField(0, 1 ) );
    		
    		if ( numHeroes > 2)
    			this.unitFactory.createAlb( this.map.getField(0, 2) );
    		
    		if ( numHeroes > 3)
    			this.unitFactory.createWizard( this.map.getField(0, 3) );
    	} catch (GameStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void run() {    	
        this.renderTarget = new QuadRenderModel(map.getWidth(), map.getHeight());

        this.renderer = new Renderer(map, renderTarget, this.resourceFinder, this.fogOfWar);
        this.map.addListener(renderer);
        this.renderer.render();

        this.window = new ClientWindow( this.resources, this.settings, this.renderer.getRederTarget() );
        this.window.setTitle("HeroQuest");
        this.window.setVisible(true);

        this.window.addKeyListener(this);
        this.window.addWindowListener(this);
    }

    public void heroesRound() {
    }
    
	/**************************************************************************************
	 * 
	 * 						  		        SHUTDOWN
	 * 
	 **************************************************************************************/    
    
	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			this.settings.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	/**************************************************************************************
	 * 
	 * 							      LET THE MONSTERS ACT!
	 * 
	 **************************************************************************************/    
    
    public void startMonstersRound() {
    	this.heroesRound = false;
 
    	// notify the ai system
    	this.aiSystem.startMonstersRound();
    	
    	// Get the first monster to act
        this.activeMonster = this.aiSystem.getNextMonster();
        
        if ( this.activeMonster == null ) {	// nothing to do
        	this.endMonstersRound();
        	return;
        }
        
        // Let the first monster perform actions!
        this.actionsToPerform = this.activeMonster.getAIController().getActions();
        
        this.performMonsterActions();
    }
    
    public void endMonstersRound() {
    	// notify the ai system
    	this.aiSystem.endMonstersRound();
    	
    	this.heroesRound = true;
    }

    public void performMonsterActions() {
    	// monster perform action
    	if ( !this.actionsToPerform.isEmpty() ) { 
            this.performAction( this.actionsToPerform.get(0) );
            this.actionsToPerform.remove(0);        	
        }    	
    	// the current monsters has all actions performed
    	else {
        	// Get the next monster perform actions!
            this.activeMonster = this.aiSystem.getNextMonster();        	

        	if ( this.activeMonster != null ) {
        		this.actionsToPerform = this.activeMonster.getAIController().getActions();
        		
        		this.performMonsterActions();
        	} 
        	// no more monsters - end monsters round
        	else {
            	this.endMonstersRound();
            	return;        		
        	}
        } 
    }

    public void performAction(GameAction action) {
    	// perform the action
        try {
        	this.game.execute( action );
        } catch (GameStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.renderer.render();
        this.window.repaint();
        
        // short pause
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        	ex.printStackTrace();
        }

        // what to do next?
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
            	if ( !ClientApplication.this.heroesRound) {
            		ClientApplication.this.performMonsterActions();
            	}
            }
        });
    }
    
	/**************************************************************************************
	 * 
	 * 								INPUT METHODS
	 * 
	 **************************************************************************************/
    
    public void handleHeroesRoundKeyEvent(KeyEvent e) {
    	
		/*
		 * Select heroes
		 */
		if (e.getKeyChar() == '1') { // select barbarian
			Hero barbarian = this.map.getHero( HeroType.BARBARIAN );
			
			if ( barbarian != null ) {
				this.activeHero = barbarian;
			}
		} 
		
		else if (e.getKeyChar() == '2') { // select dwarf
			Hero dwarf = this.map.getHero( HeroType.DWARF );
			
			if ( dwarf != null ) {
				this.activeHero = dwarf;
			}
		} 
		
		else if (e.getKeyChar() == '3') { // select alb
			Hero alb = this.map.getHero( HeroType.ALB );
			
			if ( alb != null ) {
				this.activeHero = alb;
			}
		} 
		
		else if (e.getKeyChar() == '4') { // select wizard
			Hero wizard = this.map.getHero( HeroType.WIZARD );
			
			if ( wizard != null ) {
				this.activeHero = wizard;
			}
		}
    		
		/*
		 * Start monsters round!
		 */			
		else if ( this.heroesRound && e.getKeyChar() == 'm') { // MONSTER!!!
			this.startMonstersRound();
		}
		
    	// no active hero => no hero actions!
    	if ( this.activeHero == null )
    		return;
    	
		try {
			/*
			 * Move selected hero
			 */
			if (e.getKeyChar() == 's') { // walk down

				if (this.activeHero.canMoveDown()) {
					this.game.execute( new MoveAction( this.activeHero, Direction2D.DOWN ) );
				}
				this.activeHero.setViewDir(Direction2D.DOWN);

			} 
			else if (e.getKeyChar() == 'w') { // walk up
				if (this.activeHero.canMoveUp()) {
					this.game.execute( new MoveAction( this.activeHero, Direction2D.UP ) );
				}
				this.activeHero.setViewDir(Direction2D.UP);

			} 
			else if (e.getKeyChar() == 'a') { // walk left
				if (this.activeHero.canMoveLeft()) {
					this.game.execute( new MoveAction( this.activeHero, Direction2D.LEFT ) );
				}
				this.activeHero.setViewDir(Direction2D.LEFT);

			} 
			else if (e.getKeyChar() == 'd') { // walk right
				if (this.activeHero.canMoveRight()) {
					this.game.execute( new MoveAction( this.activeHero, Direction2D.RIGHT ) );
				}
				this.activeHero.setViewDir(Direction2D.RIGHT);
			}
			
			/*
			 * Turn selected hero
			 */
			else if (e.getKeyCode() == KeyEvent.VK_UP) { // action
				this.activeHero.setViewDir(Direction2D.UP);
			}
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) { // action
				this.activeHero.setViewDir(Direction2D.LEFT);
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) { // action
				this.activeHero.setViewDir(Direction2D.DOWN);
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // action
				this.activeHero.setViewDir(Direction2D.RIGHT);
			}
			
			/**
			 * Action!
			 */
			else if (e.getKeyCode() == KeyEvent.VK_ENTER) { // action
				Field actionField = this.activeHero.getViewField();
				
				if ( actionField == null )
					return;

				// open doors
				if ( actionField.isDoor() && actionField.getDoor().isClosed() ) {
					actionField.getDoor().open();
					return;
				}
				
				// attack monsters! (yea)
				if ( actionField.hasUnit() && actionField.getUnit().isMonster() ) {
					
					this.game.execute( new AttackAction( this.activeHero, actionField.getUnit() ) );
				}
			}
					
		} catch (GameStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	
        if (this.heroesRound) {
        	this.handleHeroesRoundKeyEvent(e);
        }
        
        // print map
        if (e.getKeyChar() == 'z') {
            Field[][] field = this.map.getFields();

            for (int y = 0; y < field[0].length; y++) {
                for (int x = 0; x < field.length; x++) {
                    System.out.print(field[x][y].isBlocked() ? "# " : ". ");
                }
                System.out.println();

            }
        }

        this.renderer.render();
        this.window.repaint();
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // just override
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // just override
    }

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
