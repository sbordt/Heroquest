package de.d2dev.heroquest.engine.rendering.quads.tests;

import de.d2dev.fourseasons.resource.DummyResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.rendering.quads.Java2DRenderWindow;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

public class Java2DRendererTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QuadRenderModel model = new QuadRenderModel(10,20);
		
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 20; j++){
            	String texturePath = TestMap.class.getResource( "/de/d2dev/heroquest/engine/rendering/quads/tests/quad.jpg" ).getPath().substring(1);
            	model.addQuad( new RenderQuad(i,j,1,1,0, TextureResource.createTextureResource( texturePath ) ) );
            }
        }
        
        assert model.getQuads().size() == 200;
		
		Java2DRenderWindow window = new Java2DRenderWindow( model, new DummyResourceLocator() );
		window.setVisible(true);
	}
}
