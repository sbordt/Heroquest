package de.d2dev.heroquest.engine.rendering.quads;

import java.util.Comparator;

import de.d2dev.fourseasons.resource.Resource;

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
	
	Resource texture;
	
	
	public RenderQuad(float x, float y, float width, float height, int zLayer, Resource texture) {
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
	
	public Resource getTexture() {
		return this.texture;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + ((texture == null) ? 0 : texture.hashCode());
		result = prime * result + Float.floatToIntBits(widht);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + zLayer;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RenderQuad other = (RenderQuad) obj;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (texture == null) {
			if (other.texture != null)
				return false;
		} else if (!texture.equals(other.texture))
			return false;
		if (Float.floatToIntBits(widht) != Float.floatToIntBits(other.widht))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (zLayer != other.zLayer)
			return false;
		return true;
	}
}
