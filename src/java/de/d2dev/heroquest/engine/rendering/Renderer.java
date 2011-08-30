package de.d2dev.heroquest.engine.rendering;

import java.util.HashMap;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.MapListener;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

/**
 * This class renders a heroquest map. The render source is the given {@link Map}, the
 * target a {@link QuadRenderModel}.
 * @author Sebastian Bordt
 *
 */
public class Renderer implements MapListener {
	
	private static int GROUND = 0;
	private static int WALLS = 1;
	private static int UNITS = 100;
	
	
	private Map map;
	
	private QuadRenderModel renderTarget;
	
	/**
	 * Do we render for the first time to this render target?
	 */
	private boolean firstTime;
	
	private HashMap<Field, RenderQuad> fieldTextures = new HashMap<Field, RenderQuad>();
	
	private HashMap<Field, RenderQuad> walls = new HashMap<Field, RenderQuad>();
	
	private HashMap<Unit, RenderQuad> units = new HashMap<Unit, RenderQuad>();
	
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
		
		// units
		this.renderUnits();
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

	@Override
	public void onUnitEntersField(Field field) {
		this.renderUnits();
	}

	@Override
	public void onUnitLeavesField(Field field) {
		this.renderUnits();
	}
	
	private void renderUnits() {
		// remove all currently rendered units
		for ( RenderQuad quad : this.units.values() ) {
			this.renderTarget.removeQuad( quad );
		}
		
		this.units.clear();
		
		// render units
		for (Field[] row : map.getFields()) {	// dummy to get all units
			for (Field field : row) {
				if ( field.hasUnit() ) {
					this.units.put( field.getUnit(), this.renderUnit( field.getUnit() ) );
					this.renderTarget.addQuad( this.units.get( field.getUnit() ) );
				}
			}
		}		
	}
	
	private RenderQuad renderUnit(Unit unit) {
		Field field = unit.getField();
		
		String pictureName = "error.jpg";
		
		
		if ( unit.getType() == Unit.Type.HERO ) {
			pictureName = "units/heroes/barbarian/";
			
 		
		}
		else if ( unit.getType() == Unit.Type.MONSTER ) {
			pictureName = "units/monsters/orc/";
		}
		
		// view direction specific picture!
		if ( unit.getViewDir() == Unit.ViewDirection.UP ) {
			pictureName += "top.jpg";
		} else if ( unit.getViewDir() == Unit.ViewDirection.LEFT ) {
			pictureName += "left.jpg";
		} else if ( unit.getViewDir() == Unit.ViewDirection.RIGHT ) {
			pictureName += "right.jpg";
		} else {	// bottom
			pictureName += "bottom.jpg";
		} 
		
		return new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, UNITS,  TextureResource.createTextureResource(pictureName) );
	}
}
