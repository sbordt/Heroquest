package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.HashMap;
import java.util.List;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;

/**
 * This class renders a {@link QuadRenderModel} into a {@link java.awt.BufferedImage} (using {@code BufferedImage.TYPE_INT_ARGB}). 
 * {@code ppUnit} determines the number of pixels per render model unit. 
 * @author Sebastian Bordt
 *
 */
public class Java2DRenderer extends AbstractQuadRenderer {
	
	private HashMap< String, BufferedImage > textures = new HashMap< String, BufferedImage >();
	
	private int ppUnit;
	
	private BufferedImage target;
	
	public Java2DRenderer(QuadRenderModel m, ResourceLocator p, int ppUnit) {
		super(m, p);
		
		if ( ppUnit <= 0 )	// verify
			throw new IllegalArgumentException();
		
		this.ppUnit = ppUnit;
		
		// create the render target
		this.target = new BufferedImage( this.model.getWidth() * this.ppUnit, this.model.getHeight() * this.ppUnit,  BufferedImage.TYPE_INT_ARGB );
	}
	
	/**
	 * Render!
	 * @return The render target.
	 */
	public BufferedImage render() {
		Graphics2D graphics = target.createGraphics();
		
		// fill the background (black)
		graphics.setColor( Color.BLACK );
		graphics.fillRect( 0, 0, this.target.getWidth(), this.target.getHeight() );
		
		// render the quads
		List<RenderQuad> quads = this.model.getQuads();
		
		for ( RenderQuad quad : quads ) {
			BufferedImage img = this.provideTexture( quad.getTexture() );
			
			AffineTransform scaling = new AffineTransform();
			scaling.setToScale( quad.getWidth() * this.ppUnit /  ((float) img.getWidth()), quad.getHeight() * this.ppUnit /  ((float) img.getHeight()) ); 
			
			AffineTransform translation = new AffineTransform();
			translation.setToTranslation( quad.getX() * this.ppUnit, quad.getY() * this.ppUnit );
			
			translation.concatenate( scaling );
			graphics.drawImage( img, translation, null);
		}
		
		// done!
		return target;
	}
	
	@Override
	public void setRenderModel(QuadRenderModel m) {
		// clear loaded texture
		this.textures.clear();
		
		// change the underlying model
		this.model = m;
		
		// create the render target
		this.target = new BufferedImage( this.model.getWidth() * this.ppUnit, this.model.getHeight() * this.ppUnit,  BufferedImage.TYPE_INT_ARGB );
	}
	
	public int getTargetWidth() {
		return this.target.getWidth();
	}
	
	public int getTargetHeight() {
		return this.target.getHeight();
	}
	
	public BufferedImage getRenderTarget() {
		return this.target;
	}
	
	public ResourceLocator getResourceLocator() {
		return this.resourceProvider;
	}
	
	private BufferedImage provideTexture(Resource texture) {
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

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddQuad(RenderQuad quad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemoveQuad(RenderQuad quad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onQuadMoved(RenderQuad quad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onQuadTextureChanged(RenderQuad quad) {
		// TODO Auto-generated method stub
		
	}
}
