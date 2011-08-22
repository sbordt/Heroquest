package de.d2dev.heroquest.engine.rendering.stupid;

import java.util.List;

public interface StupidRenderModel {
	
	public int getWidth();
	public int getHeight();
	
	public List<RenderQuad> getQuads();
}
