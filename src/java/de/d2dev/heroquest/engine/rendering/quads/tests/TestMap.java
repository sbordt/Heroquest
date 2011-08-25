/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.quads.tests;

import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

/**
 *
 * @author Justus
 */
public class TestMap extends QuadRenderModel {

    public TestMap() {
		super(20, 30);
		
        for (int i = 0; i < 20; i++){
            for (int j = 0; j < 30; j++){
            	String texturePath = TestMap.class.getResource( "/de/d2dev/heroquest/engine/rendering/quads/tests/quad.jpg" ).getPath().substring(1);
            	System.out.println( texturePath );

            	this.addQuad( new RenderQuad(i,j,1,1,0, TextureResource.createTextureResource( texturePath ) ) );
            }
        }
	}
}
