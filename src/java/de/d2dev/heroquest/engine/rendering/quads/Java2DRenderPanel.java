package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import javax.swing.JPanel;

import de.d2dev.fourseasons.resource.ResourceLocator;

public class Java2DRenderPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Java2DRenderer renderer;
	
	public Java2DRenderPanel(QuadRenderModel m, ResourceLocator p) {
		this.renderer = new Java2DRenderer(m, p);
		
		this.setPreferredSize( new Dimension( m.getWidth(), m.getHeight() ) );
		this.setMaximumSize( new Dimension( m.getWidth(), m.getHeight() ) );
	}
	
	public Java2DRenderer getRenderer() {
		return this.renderer;
	}
	
	protected void paintComponent(Graphics g) {
        // We'll do our own painting, so leave out
        // a call to the superclass behavior
		Graphics2D graphics = (Graphics2D) g;

        // Black background fill
		graphics.setColor( Color.BLACK );
		graphics.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		// ask the renderer to render!
		BufferedImage img = renderer.render();
		
		// display the rendered image
		graphics.drawImage( img, 0, 0, null );
    }

}
