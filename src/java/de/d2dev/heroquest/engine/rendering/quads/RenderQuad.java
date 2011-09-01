package de.d2dev.heroquest.engine.rendering.quads;

import java.util.Comparator;

import de.d2dev.fourseasons.resource.Resource;

/**
 * A rectangle to be rendered by our stupid Renderer.
 * Has x,y,width,height (floating point valus), a texture (possibly turned) and
 * a z-layer (integer).
 * @author Sebastian Bordt
 *
 */
public class RenderQuad {
	
	/**
	 * Allow the quads texture to be turned (by 0, 90, 180 or 270 degree).
	 * We want our monsters to look in any direction! 
	 * @author Sebastian Bordt
	 *
	 */
	public enum TextureTurn {
		NORMAL,
		TURN_LEFT_90_DEGREE,
		TURN_LEFT_180_DEGREE,
		TURN_LEFT_270_DEGREE,
	}
	
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
	
	private Resource texture;
	private TextureTurn textureTurn = TextureTurn.NORMAL;
	
	public RenderQuad(float x, float y, float width, float height, int zLayer, Resource texture) {
		this.x = x;
		this.y = y;
		this.widht = width;
		this.height = height;
		this.zLayer = zLayer;
		this.texture = texture;
	}
	
	/**
	 * Also set the texture turn.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param zLayer
	 * @param texture
	 * @param turn
	 */
	public RenderQuad(float x, float y, float width, float height, int zLayer, Resource texture, TextureTurn turn) {
		this( x, y, width, height, zLayer, texture );
		
		this.textureTurn = turn;
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
	
	public TextureTurn getTextureTurn() {
		return this.textureTurn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + ((texture == null) ? 0 : texture.hashCode());
		result = prime * result
				+ ((textureTurn == null) ? 0 : textureTurn.hashCode());
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
		if (textureTurn != other.textureTurn)
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
