package de.d2dev.heroquest.engine.rendering.stupid;

/**
 * A rectangle to be rendered by our stupid Renderer.
 * Has x,y,width,height (floating point valus), a texture and
 * a z-layer (integer).
 * @author Sebastian Bordt
 *
 */
public class RenderQuad {
	
	private float x;
	private float y;
	int z;
	
	private float widht;
	private float height;
	
	String texture;
	
	RenderQuad(float x, float y, float width, float height, int z, String texture) {
		this.x = x;
		this.y = y;
		this.widht = width;
		this.height = height;
		this.z = z;
		this.texture = texture;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
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
