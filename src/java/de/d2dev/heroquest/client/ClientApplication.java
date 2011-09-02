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
    private Unit hero;
    
    private AISystem aiSystem;
        
    private boolean heroesRound = true;
    
    private List<Monster> monstersToGo;
    private List<GameAction> actionsToPerform;

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
    	
        this.hero = this.unitFactory.createBarbarian( this.map.getField(0, 0) );

        this.renderTarget = new QuadRenderModel(map.getWidth(), map.getHeight());

        this.renderer = new Renderer(map, renderTarget, this.resourceFinder);
        this.map.addListener(renderer);
        this.renderer.render();

        this.window = new Java2DRenderWindow(this.renderer.getRederTarget(), this.resourceFinder);
        this.window.setTitle("HeroQuest");
        this.window.setVisible(true);

        this.window.addKeyListener(this);
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
    
        this.renderer.render();
        this.window.repaint();
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
    }

//    public static void main(String[] args) throws Exception {
//        ClientApplication app = new ClientApplication();
//        app.init();
//        app.run();
//    }

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

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());

        if (this.heroesRound) {
            if (e.getKeyChar() == 's') {	// walk down
                try {
                    if (this.hero.canMoveDown()) {
                        this.hero.moveDown();
                    }
                    this.hero.setViewDir(Direction2D.DOWN);
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'w') {	// walk up
                try {
                    if (this.hero.canMoveUp()) {
                        this.hero.moveUp();
                    }
                    this.hero.setViewDir(Direction2D.UP);
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'a') {	// walk left
                try {
                    if (this.hero.canMoveLeft()) {
                        this.hero.moveLeft();
                    }
                    this.hero.setViewDir(Direction2D.LEFT);
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'd') {	// walk right
                try {
                    if (this.hero.canMoveRight()) {
                        this.hero.moveRight();
                    }
                    this.hero.setViewDir(Direction2D.RIGHT);
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'm') {	// MONSTER!!!
                this.startMonstersRound();
            }
        }
        if (e.getKeyChar() == 'z') {
            Field[][] field = this.map.getFields();

            for (int y = 0; y < field[0].length; y++) {
                for (int x = 0; x < field.length; x++) {
                    System.out.print(field[x][y].isBlocked() ? "# " : ". ");
                }
                System.out.println();

            }
            
//            for (Field[] col  : field) {
//                for (Field f : col) {
//                   System.out.print(f.isBlocked() ? "# " : ". "); 
//                }
//                System.out.println();
//            }
            

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
