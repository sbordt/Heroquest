package de.d2dev.heroquest.engine.rendering.stupid;

import de.d2dev.fourseasons.resource.ResourceLocator;

public class AbstractRenderer implements StupidRenderer {

	protected StupidRenderModel model;
	protected ResourceLocator resourceProvider;
	
	protected AbstractRenderer(StupidRenderModel m, ResourceLocator p) {
		this.model = m;
		this.resourceProvider = p;
	}
	
	@Override
	public void setRenderModel(StupidRenderModel m) {
		this.model = m;
	}

	@Override
	public StupidRenderModel getRenderModel() {
		return this.model;
	}

}
