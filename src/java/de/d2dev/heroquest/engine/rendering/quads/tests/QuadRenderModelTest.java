package de.d2dev.heroquest.engine.rendering.quads.tests;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

public class QuadRenderModelTest {

	public QuadRenderModel model;
	
	@Before
	public void init() {
		this.model = new QuadRenderModel( 10, 15 );
	}
	
	@Test
	public void test() {
		Vector<RenderQuad> quads = new Vector<RenderQuad>();
		
		quads.add( new RenderQuad( 6.0f, 6.0f, 1.0f, 1.0f, 25, TextureResource.createTextureResource("Nassddde") ) );
		quads.add( new RenderQuad( 1.0f, 1.0f, 1.0f, 1.0f, -123, TextureResource.createTextureResource("Nase") ) );
		quads.add( new RenderQuad( 1.0f, 2.0f, 2.0f, 2.0f, 0, TextureResource.createTextureResource("Nadsfsdfsse") ) );
		quads.add( new RenderQuad( 2.0f, 1.0f, 1.0f, 1.0f, -123, TextureResource.createTextureResource("Nsdf") ) );
		quads.add( new RenderQuad( 6.0f, 6.0f, 1.0f, 1.0f, 25, TextureResource.createTextureResource("Nassddde") ) );
		
		
		for (RenderQuad quad : quads) 
			this.model.addQuad(quad);
				
		// check quads
		assertTrue( this.model.getQuads().containsAll( quads ) );
		assertTrue( quads.containsAll( this.model.getQuads() ) );
		
		// check sorting
		for (int i=0; i<this.model.getQuads().size(); i++) {
			if ( i!= 0) {
				assertTrue( this.model.getQuads().get(i).getZLayer() >= this.model.getQuads().get(i-1).getZLayer() );
			}
		}
	}
	
}
