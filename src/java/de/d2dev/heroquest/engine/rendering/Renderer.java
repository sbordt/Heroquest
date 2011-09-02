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
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad.TextureTurn;

/**
 * This class renders a heroquest map. The render source is the given {@link Map}, the
 * target a {@link QuadRenderModel}.
 * @author Sebastian Bordt
 *
 */
public class Renderer implements MapListener {
	
	private static int GROUND = 0;
	private static int WALLS = 1;
	private static int DOORS = 2;
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
					this.renderField( field );
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
	

	@Override
	public void onFieldTextureChanges(Field field) {
		this.renderField( field );
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
	
	private void renderField(Field field) {
		// remove previous quad
		RenderQuad quad;
		
		if ( (quad = this.fieldTextures.get( field ) ) != null ) {
			this.renderTarget.removeQuad( quad );
			this.fieldTextures.remove( field );
		}
		
		// create the new texture
		quad = new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, GROUND, field.getTexture() );
		
		this.fieldTextures.put( field, quad );
		this.renderTarget.addQuad( this.fieldTextures.get( field ) );
		
		// walls
		if ( field.isWall() ) {
			Resource wallTexture = this.wallTextureCreator.createWallTexture( field );
			
			this.walls.put( field, new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, WALLS,  wallTexture ) );
			this.renderTarget.addQuad( this.walls.get( field ) );
		}
		
		// doors
		if ( field.isDoor() ) {
			Resource doorTexture = TextureResource.createTextureResource("doors/closed/vertical.png");
			
			quad = new RenderQuad( field.getX(), field.getY() - 0.5f, 1.0f, 2f, DOORS, doorTexture ); 
			this.renderTarget.addQuad( quad );
		}
	}
	
	private RenderQuad renderUnit(Unit unit) {
		Field field = unit.getField();
		
		String pictureName = "error.jpg";
		
		if ( unit.isHero() ) {
			pictureName = "units/heroes/barbarian/barbarian.jpg";
		}
		else if ( unit.isMonster() ) {
			pictureName = "units/monsters/orc/orc.jpg";
		}
		
		// view direction specific picture!
		TextureTurn turn;
		
		switch( unit.getViewDir() ) {
		case UP:
			turn = TextureTurn.NORMAL;
			break;
		case LEFT:
			turn = TextureTurn.TURN_LEFT_90_DEGREE;
			break;
		case RIGHT:
			turn = TextureTurn.TURN_LEFT_270_DEGREE;
			break;	
		default:	// down
			turn = TextureTurn.TURN_LEFT_180_DEGREE;
			break;
		}
		
		return new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, UNITS,  TextureResource.createTextureResource(pictureName), turn );
	}
}
