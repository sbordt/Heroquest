package de.d2dev.heroquest.client;

import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.ai.MapCommunicator;
import de.d2dev.heroquest.engine.ai.SearchKnot;
import de.d2dev.heroquest.engine.ai.astar.AStar;
import de.d2dev.heroquest.engine.ai.astar.Knot;
import de.d2dev.heroquest.engine.ai.astar.Path;
import de.d2dev.heroquest.engine.files.HqMapFile;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.Java2DRenderWindow;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import java.util.Stack;

public class ClientApplication {
	
	private String settingsPath = "";
	
	private Map map;
	
	private ResourceLocator resourceFinder;
	
	private Renderer renderer;
	private QuadRenderModel renderTarget;
	
	private Java2DRenderWindow window;
	
	public ClientApplication() {
		
	}
	
	public ClientApplication(HqMapFile map, ResourceLocator resourceFinder) {
		this.map = new Map( map.map.getRootElement() );
		this.resourceFinder = resourceFinder;
	}
	
	public void init() {
		this.renderTarget = new QuadRenderModel( map.getWidth(), map.getHeight() );
		
		this.renderer = new Renderer( map, renderTarget, this.resourceFinder );
		this.renderer.render();
		
		this.window = new Java2DRenderWindow( this.renderer.getRederTarget(), this.resourceFinder );
		this.window.setTitle("HeroQuest");
		this.window.setVisible( true );
	}
	
	public void run() {
            MapCommunicator communicator = new MapCommunicator(this.map,this.map.getHeight(),this.map.getWidth());
            AStar<SearchKnot> astar = new AStar<SearchKnot>();
            Stack<Path<SearchKnot>> result = astar.search(communicator.getKnot(0,0), 4);
            System.out.println(result.size());
            for(Path<SearchKnot> path : result){
                for(SearchKnot knot : path.getTrace()){                    
                    int x = knot.getX();
                    int y = knot.getY();
                    this.map.getField(x, y).setTexture( TextureResource.createTextureResource("error.jpg"));
                    for (int i = 0; i <= x; i++) {
                        for (int j = 0; j < y; j++) {
                            System.out.print(" ");
                        }
                        System.out.println("x");
                    }
                    System.out.println();
                }
            }
	}
	
	public static void main(String[] args) {
		ClientApplication app = new ClientApplication();
		app.init();
		app.run();
	}
}
