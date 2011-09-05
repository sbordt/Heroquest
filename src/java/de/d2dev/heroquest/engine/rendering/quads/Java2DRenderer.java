package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.HashMap;

import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.BufferedImageStorage;

/**
 * This class renders a {@link QuadRenderModel} into a {@link java.awt.BufferedImage} (using {@code BufferedImage.TYPE_INT_ARGB}). 
 * {@code ppUnit} determines the number of pixels per render model unit. 
 * @author Sebastian Bordt
 *
 */
public class Java2DRenderer extends AbstractQuadRenderer {
	
	private BufferedImageStorage imageStorage;
	
	private HashMap<BufferedImage, BufferedImage> turnedBy90Degree = new HashMap<BufferedImage, BufferedImage>();
	private HashMap<BufferedImage, BufferedImage> turnedBy180Degree = new HashMap<BufferedImage, BufferedImage>();
	private HashMap<BufferedImage, BufferedImage> turnedBy270Degree = new HashMap<BufferedImage, BufferedImage>();
	
	private int ppUnit;
	
	private BufferedImage target;
	
	public Java2DRenderer(QuadRenderModel m, ResourceLocator p, int ppUnit) {
		super(m, p);
		
		if ( ppUnit <= 0 )	// verify
			throw new IllegalArgumentException();
		
		this.ppUnit = ppUnit;
		
		// image storage creation
		this.imageStorage = new BufferedImageStorage( p );
		
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
	
	private void renderQuad(Graphics2D g, RenderQuad quad) {
		// load the texture
		BufferedImage img = this.imageStorage.provideTexture( quad.getTexture() );
		
		// turn the texture
		BufferedImage turned_img = img;
		
		switch( quad.getTextureTurn() ) {
		case NORMAL:	// no rotation needed
			break;
		case TURN_LEFT_90_DEGREE:
			if ( (turned_img = this.turnedBy90Degree.get( img ) ) == null ) {
				turned_img = this.turnBy90Degree( img );
				this.turnedBy90Degree.put( img, turned_img );
			}
			break;
		case TURN_LEFT_180_DEGREE:	
			if ( (turned_img = this.turnedBy180Degree.get( img ) ) == null ) {
				turned_img = this.turnBy180Degree( img );
				this.turnedBy180Degree.put( img, turned_img );
			}
			break;
		case TURN_LEFT_270_DEGREE:
			if ( (turned_img = this.turnedBy270Degree.get( img ) ) == null ) {
				turned_img = this.turnBy270Degree( img );
				this.turnedBy270Degree.put( img, turned_img );
			}
			break;
		}	
		
		// Scaling for the resolution and translation for the position
		AffineTransform transform = new AffineTransform();
		
		transform.translate( quad.getX() * this.ppUnit, quad.getY() * this.ppUnit );
		transform.scale( quad.getWidth() * this.ppUnit /  ((float) turned_img.getWidth()), quad.getHeight() * this.ppUnit /  ((float) turned_img.getHeight()) );

		// draw!
		g.drawImage( turned_img, transform, null);
	}
	
	/**
	 * 
	 * @param img
	 * @return A new {@code BufferedImage} that contains the original image turned by 90 degree.
	 */
	private BufferedImage turnBy90Degree(BufferedImage img) {
		BufferedImage turned_img = new BufferedImage( img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = turned_img.createGraphics();
		
		g.translate( 0, img.getWidth() );
		g.rotate( Math.PI * 1.5 );
				
		g.drawImage( img, new AffineTransform(), null );
		
		return turned_img;
	}
	
	/**
	 * 
	 * @param img
	 * @return A new {@code BufferedImage} that contains the original image turned by 180 degree.
	 */
	private BufferedImage turnBy180Degree(BufferedImage img) {
		BufferedImage turned_img = new BufferedImage( img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = turned_img.createGraphics();
		
		g.setColor(Color.WHITE);
		
		g.fillRect(0, 0, turned_img.getWidth(), turned_img.getHeight());
		
		g.translate( img.getHeight(), img.getWidth() );
		g.rotate( Math.PI );
		
		g.drawImage( img, new AffineTransform(), null );
		
		return turned_img;
	}

	/**
	 * 
	 * @param img
	 * @return A new {@code BufferedImage} that contains the original image turned by 270 degree.
	 */
	private BufferedImage turnBy270Degree(BufferedImage img) {
		BufferedImage turned_img = new BufferedImage( img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = turned_img.createGraphics();
		
		g.setColor(Color.WHITE);
		
		g.fillRect(0, 0, turned_img.getWidth(), turned_img.getHeight());
		
		g.translate( img.getHeight(), 0 );
		g.rotate( Math.PI / 2.0 );
		
		g.drawImage( img, new AffineTransform(), null );
		
		return turned_img;
	}
}
