package de.d2dev.heroquest.engine.game;

import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad.TextureTurn;

public class TextureOverlay /*extends MapObject*/ {
	
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	private Resource texture;
	private TextureTurn turn;
	
	public TextureOverlay() {
		super();
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.texture = TextureResource.createTextureResource( "error.jpg" );
		this.turn = TextureTurn.NORMAL;
	}

	public TextureOverlay(float x, float y, float width, float height,
			Resource texture, TextureTurn turn) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
		this.turn = turn;
	}

	public Resource getTexture() {
		return texture;
	}

	public void setTexture(Resource texture) {
		this.texture = texture;
	}

	public TextureTurn getTurn() {
		return turn;
	}

	public void setTurn(TextureTurn turn) {
		this.turn = turn;
	}
	
	public void setTurnToNormal() {
		this.turn = TextureTurn.NORMAL;
	}
	
	public void setTurnTo90Degree() {
		this.turn = TextureTurn.TURN_LEFT_90_DEGREE;
	}
	
	public void setTurnTo180Degree() {
		this.turn = TextureTurn.TURN_LEFT_180_DEGREE;
	}
	
	public void setTurnTo270Degree() {
		this.turn = TextureTurn.TURN_LEFT_270_DEGREE;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
