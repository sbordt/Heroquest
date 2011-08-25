package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;

public class Java2DRenderer extends AbstractQuadRenderer {
	
	private HashMap< String, BufferedImage > textures = new HashMap< String, BufferedImage >();

	protected Java2DRenderer(QuadRenderModel m, ResourceLocator p) {
		super(m, p);
	}
	
	public BufferedImage render() {
		// create a render target
		BufferedImage target = new BufferedImage( this.model.getWidth(), this.model.getHeight(), BufferedImage.TYPE_INT_ARGB );
		Graphics2D graphics = target.createGraphics();
		
		// fill the background (black)
		graphics.setColor( Color.BLACK );
		graphics.fillRect( 0, 0, model.getWidth(), model.getHeight() );
		
		// render the quads
		List<RenderQuad> quads = this.model.getQuads();
		
		for ( RenderQuad quad : quads ) {
			BufferedImage img = this.provideTexture( quad.getTexture() );
			
			AffineTransform scaling = new AffineTransform();
			scaling.setToScale( quad.getWidth() /  ((float) img.getWidth()), quad.getHeight() /  ((float) img.getHeight()) ); 
			
			AffineTransform translation = new AffineTransform();
			translation.setToTranslation( quad.getX(), quad.getY() );
			
			translation.concatenate( scaling );
			graphics.drawImage( img, translation, null);
		}
		
		// done!
		return target;
	}
	
	BufferedImage provideTexture(Resource texture) {
		BufferedImage img;
		
		if ( (img = this.textures.get( texture.getName() )) == null ) {
			try {
				img = TextureResource.load( this.resourceProvider.getAbsoluteLocation( texture ) );
				this.textures.put( texture.getName(), img );
			} catch (Exception e) {
				e.printStackTrace();
				
				// provide magenta picture to indicate the error
				img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB );
				Graphics2D graphics = img.createGraphics();
				
				
				graphics.setColor( Color.MAGENTA );
				graphics.fillRect( 0, 0, img.getWidth(), img.getHeight() );
			}
		}
		
		return img;
	}

}
