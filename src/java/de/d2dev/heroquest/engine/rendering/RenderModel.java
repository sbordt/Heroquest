package de.d2dev.heroquest.engine.rendering;

/**
 * Render model for our HeroQuest game. {@code RenderModel} provides information about the
 * map as far as the rendering is concerned. The map is always rectangular and consists 
 * of discrete render fields. We employ the MVC pattern to do our rendering,
 * so there is a {@link RenderModelListener} and there are renderers.
 * @author Sebastian Bordt
 *
 */
public interface RenderModel {
	
	public void addListener(RenderModelListener l);
	public void removeListener(RenderModelListener l);
	
	public void getMapWidth();
	public void getMapHeight();
	
	/**
	 * Getter for the render field at position (x,y), where (0,0) is the upper
	 * left corner and (width-1,height-1) the last accessible field.
	 * @param x
	 * @param y
	 * @return
	 */
	public RenderField getMapField(int x, int y);
}
