package de.d2dev.heroquest.engine.rendering.quads;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class QuadRenderModel {
	
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
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
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
		}
	}
	
	public void removeQuad(RenderQuad quad) {
		quads.remove( quad );
	}
}
