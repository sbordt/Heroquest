package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.d2dev.fourseasons.resource.ResourceLocator;

public class Java2DRenderPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Java2DRenderer renderer;
	
	public Java2DRenderPanel(QuadRenderModel m, ResourceLocator p) {
		this.renderer = new Java2DRenderer( m, p, 40 );
		
		this.setPreferredSize( new Dimension( this.renderer.getTargetWidth(), this.renderer.getTargetHeight() ) );
		this.setMaximumSize( new Dimension( this.renderer.getTargetWidth(), this.renderer.getTargetHeight() ) );
	}
	
	public Java2DRenderer getRenderer() {
		return this.renderer;
	}
	
	public void setRenderModel(QuadRenderModel m) {
		// set the new render model
		this.renderer = new Java2DRenderer( m, this.renderer.getResourceLocator(), 40 );
		
		// adapt the size
		this.setPreferredSize( new Dimension( this.renderer.getTargetWidth(), this.renderer.getTargetHeight() ) );
		this.setMaximumSize( new Dimension( this.renderer.getTargetWidth(), this.renderer.getTargetHeight() ) );
		
		// repaint the panel
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
        // We'll do our own painting, so leave out
        // a call to the superclass behavior
		Graphics2D graphics = (Graphics2D) g;

        // Black background fill
		graphics.setColor( new Color(0f, 0f, 0.75f) );
		graphics.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		// ask the renderer to render!
		BufferedImage img = renderer.render();
		
		// display the rendered image (put it in the center if we are bigger than it)
		int x = 0, y = 0;
		
		if ( this.getWidth()  > img.getWidth() )
			x = ( this.getWidth() - img.getWidth() ) / 2;
		
		if ( this.getHeight()  > img.getHeight() )
			y = ( this.getHeight()  - img.getHeight() ) / 2;
		
		graphics.drawImage( img, x, y, null );
    }

}
