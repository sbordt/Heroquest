package de.d2dev.heroquest.engine.rendering;

import java.util.HashMap;
import java.util.List;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.game.Door;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Hero;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.MapListener;
import de.d2dev.heroquest.engine.game.Monster;
import de.d2dev.heroquest.engine.game.Room;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad.TextureTurn;

/**
 * This class renders a heroquest map. The render source is the given {@link Map}, the
 * target a {@link QuadRenderModel}. Fancy class, very essential for the rendering :-)
 * @author Sebastian Bordt
 *
 */
public class Renderer implements MapListener {
	
	/*
	 * z-Layers for our RenderQuads
	 */
	private static int GROUND = 0;
	private static int WALLS = 1;
	private static int DOORS = 2;
	private static int UNITS = 100;
	private static int DOOR_ARCS = 200;
	private static int FOG_OF_WAR = 1000;
	
	/*
	 * Render options and their default values
	 */
	private boolean fogOfWar = true;
	
	/*
	 * Render data
	 */
	private Map map;
	
	private QuadRenderModel renderTarget;
	
	/**
	 * Do we render for the first time to this render target?
	 */
	private boolean firstTime;
	
	private HashMap<Field, RenderQuad> fieldTextures = new HashMap<Field, RenderQuad>();
	
	private HashMap<Field, RenderQuad> walls = new HashMap<Field, RenderQuad>();
	
	private HashMap<Door, RenderQuad> doors = new HashMap<Door, RenderQuad>();
	private HashMap<Door, RenderQuad> door_arcs = new HashMap<Door, RenderQuad>();
	
	private HashMap<Unit, RenderQuad> units = new HashMap<Unit, RenderQuad>();
	
	private HashMap<Field, RenderQuad> fogOfWarQuads = new HashMap<Field, RenderQuad>();
	private Resource fogTexture = TextureResource.createTextureResource("fog of war/fog.png");
	
	public Renderer(Map map, QuadRenderModel renderTarget, ResourceLocator resourceFinder) {
		this.map = map;

		this.setRenderTarget(renderTarget);
	}
		
	/*
	 * Render options getter/setter
	 */

	public boolean isFogOfWar() {
		return fogOfWar;
	}

	public void setFogOfWar(boolean fogOfWar) {
		this.fogOfWar = fogOfWar;
		
		// TODO update rendering
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
	
	
	/**
	 * Render the map!
	 */
	public void render() {
		// first time? then setup
		if ( this.firstTime ) {
			this.fieldTextures.clear();
			this.walls.clear();
			
			// reveal all passage fields
			for (Field[] row : map.getFields()) {
				for (Field field : row) {
					if ( field.isPassage() ) {
						field.reveal();
					}
				}
			}	
			
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
	

	/*
	 * Private methods that perform the rendering!
	 */	
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
			// Resource wallTexture = this.wallTextureCreator.createWallTexture( field );
			Resource wallTexture = TextureResource.createTextureResource( "walls/wall.png" );
			
			this.walls.put( field, new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, WALLS,  wallTexture ) );
			this.renderTarget.addQuad( this.walls.get( field ) );
		}
		
		// doors
		Door door = field.getDoor();
		
		if ( door != null ) {
			this.renderDoor( door );
		}
		
		// fog of war
		this.renderFogOfWar( field );
	}
	
	/**
	 * 
	 * @param field
	 */
	private void renderFogOfWar(Field field) {
		// remove previous quad
		RenderQuad quad;
		
		if ( (quad = this.fogOfWarQuads.get( field ) ) != null ) {
			this.renderTarget.removeQuad( quad );
			this.fogOfWarQuads.remove( field );
		}
		
		// render fog of war quad
		if ( !this.fogOfWar )
			return;
		
		if ( field.belongsToRoom() && !field.isRevealed() ) {			
			quad = new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, FOG_OF_WAR,  fogTexture );
			
			this.fogOfWarQuads.put( field, quad );
			this.renderTarget.addQuad( quad );
		}
		
		// hide wall fields if all surrounding non-wall fields have not been revealed yet
		if ( field.isWall() ) {
			List<Field> surrounders = field.getSurroundingFields();
			
			for (Field f : surrounders) {
				if ( !f.isWall() && f.isRevealed() ) {
					return;
				}
			}
				
			quad = new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, FOG_OF_WAR,  fogTexture );
				
			this.fogOfWarQuads.put( field, quad );
			this.renderTarget.addQuad( quad );
		}
	}
	
	/**
	 * Render a door.
	 * @param door
	 */
	private void renderDoor(Door door) {
		// remove previous door render quad
		RenderQuad quad;
		RenderQuad arc_quad;
		
		if ( (quad = this.doors.get( door ) ) != null ) {
			this.renderTarget.removeQuad( quad );
			this.doors.remove( door );
		}
		
		// remove previous door arc quad		
		if ( (arc_quad = this.door_arcs.get( door ) ) != null ) {
			this.renderTarget.removeQuad( arc_quad );
			this.door_arcs.remove( door );
		}
		
		// render the door!
		Field field = door.getField();
		arc_quad = null;
		
		Resource vertOpenTex = TextureResource.createTextureResource("doors/open/vertical.png");
		Resource vertOpenArcTex = TextureResource.createTextureResource("doors/open/vertical_arc.png");
		Resource horOpenTex = TextureResource.createTextureResource("doors/open/horizontal.png");
		Resource horOpenArcTex = TextureResource.createTextureResource("doors/open/horizontal_arc.png");
		
		Resource vertClosedTex = TextureResource.createTextureResource("doors/closed/vertical.png");
		Resource horClosedTex = TextureResource.createTextureResource("doors/closed/horizontal.png");
		
		// open?
		if ( door.isOpen() ) {
			// vertical?
			if ( field.getUpperField().isWall() ) {
				quad = new RenderQuad( field.getX(), field.getY() - 0.5f, 1.0f, 2f, DOORS, vertOpenTex ); 
				
				arc_quad = new RenderQuad( field.getX(), field.getY(), 1, 1, DOOR_ARCS, vertOpenArcTex ); 
			} else {
				quad = new RenderQuad( field.getX() -0.5f, field.getY(), 2, 1, DOORS, horOpenTex ); 
				
				arc_quad = new RenderQuad( field.getX(), field.getY(), 1, 1, DOOR_ARCS, horOpenArcTex ); 
			}	
		} 
		// closed
		else {
			// vertical?
			if ( field.getUpperField().isWall() ) {
				quad = new RenderQuad( field.getX(), field.getY() - 0.5f, 1.0f, 2f, DOORS, vertClosedTex ); 
			} else {
				quad = new RenderQuad( field.getX() -0.5f, field.getY(), 2, 1, DOORS, horClosedTex ); 
			}				
		}
			
		this.renderTarget.addQuad( quad );
		this.doors.put( door, quad );
		
		if ( arc_quad != null ) {
			this.renderTarget.addQuad( arc_quad );
			this.door_arcs.put( door, arc_quad );			
		}
	}
	
	private RenderQuad renderUnit(Unit unit) {
		Field field = unit.getField();
		
		String pictureName = "error.jpg";
		
		if ( unit.isHero() ) {
			switch( ((Hero) unit).getHeroType() ) {
			case BARBARIAN:
				pictureName = "units/heroes/barbarian/barbarian.jpg";
				break;
			case DWARF:
				pictureName = "units/heroes/dwarf/dwarf.jpg";
				break;
			case ALB:
				pictureName = "units/heroes/alb/alb.jpg";
				break;
			case WIZARD:
				pictureName = "units/heroes/wizard/wizard.jpg";
				break;
			}
		}
		else if ( unit.isMonster() ) {
			switch( ((Monster) unit).getMonsterType() ) {
			case CHAOS_WARRIOR:
				pictureName = "units/monsters/chaos warrior.jpg";
				break;
			case FIMIR:
				pictureName = "units/monsters/fimir.jpg";
				break;
			case GARGOYLE:
				pictureName = "units/monsters/gargoyle.jpg";
				break;
			case GOBLIN:
				pictureName = "units/monsters/goblin.jpg";
				break;
			case MUMMY:
				pictureName = "units/monsters/mummy.jpg";
				break;
			case ORC:
				pictureName = "units/monsters/orc.jpg";
				break;
			case SKELETON:
				pictureName = "units/monsters/skeleton.jpg";
				break;
			case ZOMBIE:
				pictureName = "units/monsters/zombie.jpg";
				break;
			}
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
	
	/*
	 * React on map events to update the render model!
	 */
	@Override
	public void onUnitEntersField(Field field) {
		this.renderUnits();
	}

	@Override
	public void onUnitLeavesField(Field field) {
		this.renderUnits();
	}
	
	@Override
	public void onFieldRevealed(Field field) {
		this.renderField(field);
		
		// update all surrounding wall fields!
		for (Field f : field.getSurroundingFields()) {
			if ( f.isWall() ) {
				this.renderField( f );
			}
		}
	}
	
	@Override
	public void onRoomRevealed(Room room) {
		// nothing to do we react on the individual fields
	}
	
	@Override
	public void onDoorOpened(Door door) {
		this.renderDoor(door);
	}
	
	@Override
	public void onFieldTextureChanges(Field field) {
		this.renderField( field );
	}




}
