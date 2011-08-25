package de.d2dev.heroquest.engine.rendering.quads;

import java.util.Comparator;

/**
 * A rectangle to be rendered by our stupid Renderer.
 * Has x,y,width,height (floating point valus), a texture and
 * a z-layer (integer).
 * @author Sebastian Bordt
 *
 */
public class RenderQuad {
	
	public class ZOrderComparator implements Comparator<RenderQuad> {
		
		@Override
		public int compare(RenderQuad arg0, RenderQuad arg1) {
			return arg0.zLayer < arg1.zLayer ? -1 : (arg0.zLayer == arg1.zLayer ? 0 : 1);
		}
	}
	
	private float x;
	private float y;
	int zLayer;
	
	private float widht;
	private float height;
	
	String texture;
	
	
	public RenderQuad(float x, float y, float width, float height, int zLayer, String texture) {
		this.x = x;
		this.y = y;
		this.widht = width;
		this.height = height;
		this.zLayer = zLayer;
		this.texture = texture;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public int getZLayer() {
		return this.zLayer;
	}
	
	public float getWidth() {
		return this.widht;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public String getTexture() {
		return this.texture;
	}
}
