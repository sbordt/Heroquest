package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.HashMap;

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
		for ( RenderQuad quad : this.model.getQuads()  ) {
			this.renderQuad(graphics, quad);
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
	
	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddQuad(RenderQuad quad) {
		System.out.println("Add");
	}

	@Override
	public void onRemoveQuad(RenderQuad quad) {
		System.out.println("remove");
		
	}

	@Override
	public void onQuadMoved(RenderQuad quad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onQuadTextureChanged(RenderQuad quad) {
		// TODO Auto-generated method stub
		
	}
	
	private void renderQuad(Graphics2D g, RenderQuad quad) {
		// load the texture
		BufferedImage img = this.provideTexture( quad.getTexture() );
		
		// Scaling for the resolution, rotation for the turn and translation for the position
		AffineTransform transform = new AffineTransform();

		transform.translate( quad.getX() * this.ppUnit, quad.getY() * this.ppUnit );
		
		switch( quad.getTextureTurn() ) {
		case NORMAL:	// no rotation needed
			break;
		case TURN_LEFT_90_DEGREE:
			transform.rotate(Math.PI * 1.5, img.getWidth() / 2.0, img.getHeight() / 2.0);
			break;
		case TURN_LEFT_180_DEGREE:
			transform.rotate(Math.PI, img.getWidth() / 2.0, img.getHeight() / 2.0);
			break;
		case TURN_LEFT_270_DEGREE:
			transform.rotate(Math.PI / 2.0, img.getWidth() / 2.0, img.getHeight() / 2.0);
			break;
		}
		
		transform.scale( quad.getWidth() * this.ppUnit /  ((float) img.getWidth()), quad.getHeight() * this.ppUnit /  ((float) img.getHeight()) );
		
		// draw!
		g.drawImage( img, transform, null);
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

}
