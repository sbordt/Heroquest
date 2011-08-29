package de.d2dev.heroquest.engine.rendering.quads;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class QuadRenderModel {

	public Vector<QuadRenderModelListener> listeners = new Vector<QuadRenderModelListener>();
	
	private int width;
	private int height;
	
	/**
	 * z-soted list of our render quads.
	 */
	private List<RenderQuad> quads = new LinkedList<RenderQuad>();
	
	/**
	 * unmodifiable access.
	 */
	private List<RenderQuad> unmodifiable_quads = Collections.unmodifiableList( quads );
	
	public QuadRenderModel(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void addListener(QuadRenderModelListener l) {
		if ( !this.listeners.contains( l ) ) {
			this.listeners.add( l );
		}
	}
	
	public void removeListener(QuadRenderModelListener l) {
		this.listeners.remove( l );
	}
	
	private void fireOnAddQuad(RenderQuad quad) {
		for ( QuadRenderModelListener l : this.listeners ) {
			l.onAddQuad( quad );
		}
	}
	
	private void fireOnRemoveQuad(RenderQuad quad) {
		for ( QuadRenderModelListener l : this.listeners ) {
			l.onRemoveQuad( quad );
		}
	}
	
	private void fireOnResize() {
		for ( QuadRenderModelListener l : this.listeners ) {
			l.onResize( this.width, this.height );
		}
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Resize the rendering area.
	 * @param width > 0
	 * @param height > 0
	 */
	public void resize(int width, int height) {
		if ( width <= 0 || height <= 0 )	// verify parameters
			throw new IllegalArgumentException( "QuadRenderModel width and height must be greater than 0" );
		
		if ( this.width == width && this.height == height )	// no resize operation needed - it might be expensive for the renderer
			return;

		this.width = width;
		this.height = height;
		
		this.fireOnResize();	// notify listeners		
	}
	
	public void setHeight(int height) {

	}
	
	/**
	 * Getter for an unmodifiable z-sorted list of all {@code RenderQuad}'s.
	 * @return
	 */
	public List<RenderQuad> getQuads() {
		return this.unmodifiable_quads;
	}
	
	public void addQuad(RenderQuad quad) {
		if ( !quads.contains( quad) ) {
			quads.add( quad );
			Collections.sort( quads, quad.new ZOrderComparator() );	
			
			this.fireOnAddQuad( quad );
		}
	}
	
	public void removeQuad(RenderQuad quad) {
		if ( this.quads.contains( quad ) ) {
			this.fireOnRemoveQuad( quad );	// notify listeners before
			
			quads.remove( quad );
		}
	}
	
	/**
	 * Clear the render model, i.e. remove all quads.
	 */
	public void clear() {
		while ( !this.quads.isEmpty() ) {
			this.removeQuad( this.quads.get(0) );
		}
	}
}
