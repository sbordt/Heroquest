package de.d2dev.heroquest.engine.rendering.quads;

import java.util.List;

public class JmeQuadRenderModel {
	
	private QuadRenderModel m;

	public JmeQuadRenderModel(int width, int height) {
		super(width, height);
	}
	
	@Override
	public List<RenderQuad> getQuads() {
		for (RenderQuad q : m.getQuads() ){
			
		}
	}

}
