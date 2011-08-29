package de.d2dev.heroquest.engine.rendering;

import java.util.HashMap;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

/**
 * This class renders a heroquest map. The render source is the given {@link Map}, the
 * target a {@link QuadRenderModel}.
 * @author Sebastian Bordt
 *
 */
public class Renderer {
	
	private static int GROUND = 0;
	private static int WALLS = 1;
	
	
	private Map map;
	
	private QuadRenderModel renderTarget;
	
	/**
	 * Do we render for the first time to this render target?
	 */
	private boolean firstTime;
	
	private HashMap<Field, RenderQuad> fieldTextures = new HashMap<Field, RenderQuad>();
	
	private HashMap<Field, RenderQuad> walls = new HashMap<Field, RenderQuad>();
	
	private WallTextureCreator wallTextureCreator;
	
	public Renderer(Map map, QuadRenderModel renderTarget, ResourceLocator resourceFinder) {
		this.map = map;

		this.setRenderTarget(renderTarget);
		
		this.wallTextureCreator = new WallTextureCreator( map, resourceFinder, TextureResource.createTextureResource( "fields/brown.jpg" ) );
	}
	
	public void render() {
		// first time? then setup
		if ( this.firstTime ) {
			this.fieldTextures.clear();
			this.walls.clear();
			
			for (Field[] row : map.getFields()) {
				for (Field field : row) {
					// field textures
					this.fieldTextures.put( field, new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, GROUND, field.getTexture() ) );
					this.renderTarget.addQuad( this.fieldTextures.get( field ) );
					
					// walls
					if ( field.isWall() ) {
						Resource wallTexture = this.wallTextureCreator.createWallTexture( field );
						
						this.walls.put( field, new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, WALLS,  wallTexture ) );
						this.renderTarget.addQuad( this.walls.get( field ) );
					}
				}
			}	
			
			this.firstTime = false;
		}
		
		// one quad for each field
		/*for (Field[] row : map.getFields()) {
			for (Field field : row) {
				RenderQuad quad =  this.fieldTextures.get( field );
				if ( !=  )
			}
		}*/		
	}

	public QuadRenderModel getRederTarget() {
		return renderTarget;
	}
	
	public void setRenderTarget(QuadRenderModel renderTarget) {
		renderTarget.resize( map.getWidth(), map.getHeight() );	// put the render target to the proper size
		renderTarget.clear();
		
		this.renderTarget = renderTarget;
		this.firstTime = true;
	}
	
	public Map getMap() {
		return map;
	}
}
