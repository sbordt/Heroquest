package de.d2dev.heroquest.client;

import de.d2dev.fourseasons.gamestate.GameStateException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.script.ScriptEngine;
import de.d2dev.heroquest.editor.script.EditorLuaScriptDecomposer;
import de.d2dev.heroquest.editor.script.LuaMapCreatorFunction;
import de.d2dev.heroquest.engine.ai.AISystem;
import de.d2dev.heroquest.engine.files.HqMapFile;
import de.d2dev.heroquest.engine.game.Direction2D;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Hero;
import de.d2dev.heroquest.engine.game.Hero.HeroType;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Monster;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.game.UnitFactory;
import de.d2dev.heroquest.engine.game.action.GameAction;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.Java2DRenderWindow;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.schlichtherle.truezip.file.TFile;

import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class ClientApplication implements KeyListener {

//    private String settingsPath = "";
    
    private ScriptEngine scriptEngine;    
    
    private Map map;
    private ResourceLocator resourceFinder;
    private Renderer renderer;
    private QuadRenderModel renderTarget;
    private Java2DRenderWindow window;
    
    private UnitFactory unitFactory;
    
    private AISystem aiSystem;
        
    private boolean heroesRound = true;
    
    private Unit activeHero = null;
    
    private List<Monster> monstersToGo;
    private List<GameAction> actionsToPerform;
    
	/**************************************************************************************
	 * 
	 * 						  		INITIALIZATION/SETUP
	 * 
	 **************************************************************************************/    

    public ClientApplication(TFile mapCreatorScript, ResourceLocator resourceFinder) throws Exception {
    	this.initResourceFinder(resourceFinder);
    	
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
    	
    	// script engine setup
    	scriptEngine = ScriptEngine.createDefaultScriptEngine( this.resourceFinder, new EditorLuaScriptDecomposer() );
    }
    
    public void init() throws Exception {
        this.aiSystem = new AISystem(map);

    	this.unitFactory = new UnitFactory();
    }
    
    public void addTestMonsters(int numMonsters) {
    	if ( numMonsters <= 0 )
    		return;
    	
    	if ( numMonsters > 10 )
    		numMonsters = 10;
    	
    	for (int i=0; i<numMonsters; i++) {
    		try {
				Monster monster = this.unitFactory.createOrc( this.map.getField( 34, 10 + i ) );
				monster.setAiController( this.aiSystem.creatAIController(monster) );
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
    		this.activeHero = this.map.getHeroes().get(0);
    		
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

//        Field[][] field = this.map.getFields();
//
//        MapCommunicator communicator = new MapCommunicator(this.map);
//        Stack<Path<SearchKnot>> result = communicator.search(field.length - 1, field[0].length - 1, 0, 0, 1);
//        System.out.println(result.size());
//        for (Path<SearchKnot> path : result) {
//            for (SearchKnot knot : path.getTrace()) {
//                int x = knot.getX();
//                int y = knot.getY();
//                this.map.getField(x, y).setTexture(TextureResource.createTextureResource("error.jpg"));
//                System.out.println("Texture.set");
//            }
//        }
//        System.out.println("Hallo");
//        this.renderer.render();
//        this.window.repaint();
    	
        this.renderTarget = new QuadRenderModel(map.getWidth(), map.getHeight());

        this.renderer = new Renderer(map, renderTarget, this.resourceFinder);
        this.map.addListener(renderer);
        this.renderer.render();

        this.window = new Java2DRenderWindow(this.renderer.getRederTarget(), this.resourceFinder);
        this.window.setTitle("HeroQuest");
        this.window.setVisible(true);

        this.window.addKeyListener(this);
    }

    public void heroesRound() {
    }

    
	/**************************************************************************************
	 * 
	 * 							      LET THE MONSTERS ACT!
	 * 
	 **************************************************************************************/    
    
    public void startMonstersRound() {
    	this.heroesRound = false;
 
    	// Get the maps monsters
        this.monstersToGo = this.map.getMonsters();
        
        if ( this.monstersToGo.isEmpty() ) {	// nothing to do
        	this.endMonstersRound();
        	return;
        }
        
        // Let the first monster perform actions!
        this.actionsToPerform = this.monstersToGo.get(0).getAIController().getActions();
        
        this.performMonsterActions();
    }
    
    public void endMonstersRound() {
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
        	this.monstersToGo.remove(0);
        	
        	// next monster perform actions!
        	if ( !this.monstersToGo.isEmpty() ) {
        		this.actionsToPerform = this.monstersToGo.get(0).getAIController().getActions();
        		
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
            action.excecute();
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
            Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, null, ex);
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
    	// no active hero => no heroes
    	if ( this.activeHero == null )
    		return;
    	
		try {
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
			 * Move selected hero
			 */
			else if (e.getKeyChar() == 's') { // walk down

				if (this.activeHero.canMoveDown()) {
					this.activeHero.moveDown();
				}
				this.activeHero.setViewDir(Direction2D.DOWN);

			} 
			else if (e.getKeyChar() == 's') { // walk down

				if (this.activeHero.canMoveDown()) {
					this.activeHero.moveDown();
				}
				this.activeHero.setViewDir(Direction2D.DOWN);

			} 
			else if (e.getKeyChar() == 'w') { // walk up
				if (this.activeHero.canMoveUp()) {
					this.activeHero.moveUp();
				}
				this.activeHero.setViewDir(Direction2D.UP);

			} 
			else if (e.getKeyChar() == 'a') { // walk left
				if (this.activeHero.canMoveLeft()) {
					this.activeHero.moveLeft();
				}
				this.activeHero.setViewDir(Direction2D.LEFT);

			} 
			else if (e.getKeyChar() == 'd') { // walk right
				if (this.activeHero.canMoveRight()) {
					this.activeHero.moveRight();
				}
				this.activeHero.setViewDir(Direction2D.RIGHT);
			}
					
			/*
			 * Start monsters round!
			 */			
			else if (e.getKeyChar() == 'm') { // MONSTER!!!
				this.startMonstersRound();
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
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }
}
