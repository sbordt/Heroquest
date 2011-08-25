package de.d2dev.heroquest.engine.rendering;

import java.util.HashMap;

import de.d2dev.heroquest.engine.Field;
import de.d2dev.heroquest.engine.Map;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

/**
 * This class renders a heroquest map. The render source is the given {@link Map}, the
 * target a {@link QuadRenderModel}.
 * @author Sebastian Bordt
 *
 */
public class Renderer {
	
	private Map map;
	
	private QuadRenderModel renderTarget;
	
	private boolean firstTime = true;
	private HashMap<Field, RenderQuad> fieldTextures = new HashMap<Field, RenderQuad>();
	
	
	public Renderer(Map map) {
		this.map = map;
		this.renderTarget = new QuadRenderModel( map.getWidth(), map.getHeight() );
	}
	
	public void render() {
		// first time? then setup
		if ( this.firstTime ) {
			for (Field[] row : map.getFields()) {
				for (Field field : row) {
					this.fieldTextures.put( field, new RenderQuad( field.getX(), field.getY(), 1.0f, 1.0f, 0, field.getTexture() ) );
					this.renderTarget.addQuad( this.fieldTextures.get( field ) );
				}
			}			
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
	
	public Map getMap() {
		return map;
	}
}
