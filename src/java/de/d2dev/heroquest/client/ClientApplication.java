package de.d2dev.heroquest.client;

import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.heroquest.engine.files.HqMapFile;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.Java2DRenderWindow;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;

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
	}
	
	public static void main(String[] args) {
		ClientApplication app = new ClientApplication();
		app.init();
		app.run();
	}
}
