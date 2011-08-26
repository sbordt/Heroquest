package de.d2dev.heroquest.engine.rendering.quads;

public interface QuadRenderModelListener {

	public void onAddQuad(RenderQuad quad);
	public void onRemoveQuad(RenderQuad quad);
	
	public void onQuadMoved(RenderQuad quad);
	public void onQuadTextureChanged(RenderQuad quad);
}
