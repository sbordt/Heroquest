package de.d2dev.heroquest.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.ai.MapCommunicator;
import de.d2dev.heroquest.engine.ai.SearchKnot;
import de.d2dev.heroquest.engine.ai.astar.AStar;
import de.d2dev.heroquest.engine.ai.astar.Path;
import de.d2dev.heroquest.engine.files.HqMapFile;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Game;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.game.Unit.ViewDirection;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.Java2DRenderWindow;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import java.util.Stack;

public class ClientApplication extends Game implements KeyListener {
	
	private String settingsPath = "";
	
	private Map map;
	
	private ResourceLocator resourceFinder;
	
	private Renderer renderer;
	private QuadRenderModel renderTarget;
	
	private Java2DRenderWindow window;
	
	private Unit hero;
	
	private Unit monster;
	
	public ClientApplication() {
		
	}
	
	public ClientApplication(HqMapFile map, ResourceLocator resourceFinder) {
		this.map = new Map( map.map.getRootElement() );
		this.resourceFinder = resourceFinder;
	}
	
	public void init() throws Exception {
		this.hero = new Unit( this.map.getField(0, 0), Unit.Type.HERO );
		this.monster = new Unit( this.map.getField( this.map.getWidth()-1, this.map.getHeight()-1 ), Unit.Type.MONSTER );
		
		this.renderTarget = new QuadRenderModel( map.getWidth(), map.getHeight() );
		
		this.renderer = new Renderer( map, renderTarget, this.resourceFinder );
		this.map.addListener(renderer);
		this.renderer.render();
		
		this.window = new Java2DRenderWindow( this.renderer.getRederTarget(), this.resourceFinder );
		this.window.setTitle("HeroQuest");
		this.window.setVisible( true );
		
		this.window.addKeyListener( this );
	}
	
	public void run() {
            
            Field[][] field = this.map.getFields();
            
//            for (int i = 0; i < field.length; i++) {
//                
//                for (int j = 0; j < field[i].length; j++) {
//                    System.out.print(field[i][j].isBlocked() ? "# " : "0 ");               
//                }
//                System.out.println();
//                
//            }
            
            MapCommunicator communicator = new MapCommunicator(this.map);
            Stack<Path<SearchKnot>> result = communicator.search(0, 1, field.length-2, field[0].length-1, 1);
            System.out.println(result.size());
            for(Path<SearchKnot> path : result){
                int[][] printPath = new int[30][30];
                for(SearchKnot knot : path.getTrace()){         
                    
                    int x = knot.getX();
                    int y = knot.getY();
                    this.map.getField(x, y).setTexture( TextureResource.createTextureResource("error.jpg"));
                    printPath[x][y] = 1;

                }
                for (int i = 0; i < printPath.length; i++) {
                    for (int j = 0; j < printPath[i].length; j++) {
                        System.out.print(printPath[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
	}

	
	public static void main(String[] args) throws Exception {
		ClientApplication app = new ClientApplication();
		app.init();
		app.run();
	}
	
	public void heroesRound() {}
	
	public void monstersRound() {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println( e.getKeyChar() );
		
		if ( e.getKeyChar() == 's' ) {	// walk down
			try {
				if ( this.hero.getField().getY() != this.map.getHeight()-1 
						&& !this.map.getField( this.hero.getField().getX(), this.hero.getField().getY()+1 ).isBlocked() ) {
					this.hero.moveTo( this.map.getField( this.hero.getField().getX(), this.hero.getField().getY()+1 ) );
					this.hero.setViewDir( ViewDirection.DOWN );					
				}
			} catch (GameStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if ( e.getKeyChar() == 'w' ) {	// walk up
			try {
				if ( this.hero.getField().getY() != 0 
						&& !this.map.getField( this.hero.getField().getX(), this.hero.getField().getY()-1 ).isBlocked() ) {
					this.hero.moveTo( this.map.getField( this.hero.getField().getX(), this.hero.getField().getY()-1 ) );
					this.hero.setViewDir( ViewDirection.UP);					
				}
			} catch (GameStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if ( e.getKeyChar() == 'a' ) {	// walk left
			try {
				if ( this.hero.getField().getX() != 0 
						&& !this.map.getField( this.hero.getField().getX()-1, this.hero.getField().getY() ).isBlocked() ) {
					this.hero.moveTo( this.map.getField( this.hero.getField().getX()-1, this.hero.getField().getY() ) );
					this.hero.setViewDir( ViewDirection.LEFT );					
				}
			} catch (GameStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if ( e.getKeyChar() == 'd' ) {	// walk right
			try {
				if ( this.hero.getField().getX() != this.map.getWidth()-1 
						&& !this.map.getField( this.hero.getField().getX()+1, this.hero.getField().getY() ).isBlocked() ) {
					this.hero.moveTo( this.map.getField( this.hero.getField().getX()+1, this.hero.getField().getY() ) );
					this.hero.setViewDir( ViewDirection.RIGHT );					
				}
			} catch (GameStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
