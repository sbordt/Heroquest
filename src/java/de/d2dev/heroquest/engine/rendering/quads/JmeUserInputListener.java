package de.d2dev.heroquest.engine.rendering.quads;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;

import de.d2dev.heroquest.engine.rendering.GUI.CharacterInfoController;
import de.lessvoid.nifty.Nifty;

public class JmeUserInputListener implements AnalogListener, ActionListener {
	private JmeRenderer renderer = null;
	private boolean rightMouseButton = false;
	private Vector2f dragStartPos = new Vector2f(0,0);
	
	public JmeUserInputListener (JmeRenderer renderer){
		this.renderer = renderer;
	}
//-----------------------------------------------------------------------------------------------------
//----------------------------------ON ANALOG --------------------------------------------------------
//----------------------------------------------------------------------------------------------------
	public void onAnalog(String name, float value, float tpf) {
       
//-----------------------------------Cam Move------------------------------------------------------------		
		// Die Pfeil Rechts Taste soll die Kamera nach rechts bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	if (name.equals("right")) {
    		renderer.moveCamera (renderer.getCameraSpeed()*tpf*renderer.getZoomLevel(), 0, true);
        }
    	// Die Pfeil links Taste soll die Kamera nach links bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	else if (name.equals("left")) {
    		renderer.moveCamera (-renderer.getCameraSpeed()*tpf*renderer.getZoomLevel(), 0, true);
        }
    	// Die Pfeil oben Taste soll die Kamera nach oben bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	else if (name.equals("up")) {
    		renderer.moveCamera (0, renderer.getCameraSpeed()*tpf*renderer.getZoomLevel(), true);
        }
    	// Die Pfeil unten Taste soll die Kamera nach unten bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	else if (name.equals("down")) {
    		renderer.moveCamera (0, -renderer.getCameraSpeed()*tpf*renderer.getZoomLevel(), true);
        }
//-------------------------------------Cam Zoom----------------------------------------------------    	
        else if (name.equals("zoomOut")){
        	float aspect = (float) renderer.getCamera().getWidth() / renderer.getCamera().getHeight();
        	// Wenn es links am rand ist und rechts nicht versuche es nach rechts zu bewegen
        	if (
        		(((renderer.getCamera().getLocation().x - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) > 0) != true)&&
        		((renderer.getCamera().getLocation().x + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) < renderer.getQuadRenderModel().getWidth())
        		){
        		renderer.moveCamera (renderer.getZoomSpeed()*tpf*aspect, 0, true);
        	}
        	// Wenn es rechts am rand ist und links nicht versuche es nach links zu bewegen
        	if (
        		(((renderer.getCamera().getLocation().x + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) < renderer.getQuadRenderModel().getWidth()) != true)&&
        		((renderer.getCamera().getLocation().x - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) > 0)
       			){
       			renderer.moveCamera (-renderer.getZoomSpeed()*tpf*aspect, 0, true);
       		}
        		// Wenn es oben am rand ist und unten nicht versuche es nach unten zu bewegen
        	if (
        		(((renderer.getCamera().getLocation().y + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) < renderer.getQuadRenderModel().getHeight())!= true)&&
        		((renderer.getCamera().getLocation().y - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) > 0)
       			){
       			renderer.moveCamera (0, -renderer.getZoomSpeed()*tpf, true);
       		}
        		// Wenn es unten am rand ist und oben nicht versuche es nach oben zu bewegen
        	if (
        		(((renderer.getCamera().getLocation().y - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) > 0)!= true)&&
        		((renderer.getCamera().getLocation().y + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) < renderer.getQuadRenderModel().getHeight())
        		){
        		renderer.moveCamera (0, renderer.getZoomSpeed()*tpf, true);
        	}
        	// Wenn keiner den Kameraränder den Bildschirm verlassen würde wird normal gezoomt
        	if (
            	// Es würde den links Speilfeldrand nicht überschreiten / ist rechts nicht am rand
            	((renderer.getCamera().getLocation().x - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) > 0)&&
            	// Es darf nicht rechts aus dem Bild gezoomt werden
            	((renderer.getCamera().getLocation().x + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) < renderer.getQuadRenderModel().getWidth())&&
            	// Es darf nicht oben aus dem Bild gezoomt werden
            	((renderer.getCamera().getLocation().y + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) < renderer.getQuadRenderModel().getHeight())&&
            	// Es darf nicht unten aus dem Bild gezoomt werden
            	((renderer.getCamera().getLocation().y - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) > 0)
            	){
        		renderer.setZoomLevel(renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf) ;
            	renderer.getCamera().setFrustum( -100, 1100, -renderer.getZoomLevel() * aspect, renderer.getZoomLevel() * aspect, renderer.getZoomLevel(), -renderer.getZoomLevel() );
            }
        	// Wenn die Kameraränder an beiden seiten einer achse das Spielfeld verlassen würden
        	// Wernden die Kameraränder mit den Spielfeldrändern gleich gesetzt
        	
        	// Es würde oben den rand überschreiten und unten
        	if (
        			(((renderer.getCamera().getLocation().y + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) < renderer.getQuadRenderModel().getHeight())!= true)&&
        			(((renderer.getCamera().getLocation().y - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) > 0)!= true)
        		){
        			// Camera auf die Horizontal speilfeldhalbierende setzen
        			// WICHTIG: RenderModell.getHeight() liefert einen Int der gecastet werden muss
        		
        			renderer.getCamera().setLocation(new Vector3f(	renderer.getCamera().getLocation().x,
        															(float) renderer.getRenderModel().getHeight()/2,
        															renderer.getCamera().getLocation().z));
        			
        			// Camera Frustum auf ränder setzen
        			renderer.getCamera().setFrustum(-100, 1100,
        											-((float)renderer.getRenderModel().getHeight()/2)*aspect, ((float)renderer.getRenderModel().getHeight()/2)*aspect, 
        											(float) renderer.getRenderModel().getHeight()/2, (float)-renderer.getRenderModel().getHeight()/2);
        	}
        	// Es würde rechts und links den rand überschreiten
        	if (
        			(((renderer.getCamera().getLocation().x + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) < renderer.getQuadRenderModel().getWidth()) != true)&&
        			(((renderer.getCamera().getLocation().x - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) > 0) != true)
        		){
        			// Camera auf die vertikal speilfeldhalbierende setzen
        			// WICHTIG: RenderModell.getHeight() liefert einen Int der gecastet werden muss
        		
        			renderer.getCamera().setLocation(new Vector3f(	(float) renderer.getRenderModel().getWidth()/2, // XPos auf hälfte
        															renderer.getCamera().getLocation().y,			// Y Pos bleibt
        															renderer.getCamera().getLocation().z));			// z Pos bleibt auch
        			
        			// Camera Frustum auf ränder setzen
        			renderer.getCamera().setFrustum(-100, 1100,
        											-(float)renderer.getRenderModel().getWidth()/2, (float)renderer.getRenderModel().getWidth()/2, 
        											((float)renderer.getRenderModel().getWidth()/2)/aspect, -((float)renderer.getRenderModel().getWidth()/2)/aspect);
        	
        	}
        	
        }
        else if (name.equals("zoomIn")){
            renderer.setZoomLevel(renderer.getZoomLevel() - renderer.getZoomSpeed()*tpf);
            float aspect = (float) renderer.getCamera().getWidth() / renderer.getCamera().getHeight();
            renderer.getCamera().setFrustum( -100, 1100, -renderer.getZoomLevel() * aspect, renderer.getZoomLevel() * aspect, renderer.getZoomLevel(), -renderer.getZoomLevel() );
        }
//---------------------------------------------dag Move cam------------------------------------------------    	
        else if (name.equals("dragMoveX")){
        	float xDiff = this.renderer.getInputManager().getCursorPosition().x - dragStartPos.x;
        	float yDiff = this.renderer.getInputManager().getCursorPosition().y - dragStartPos.y;
        	
        	float aspect = (float) renderer.getCamera().getWidth() / (float) renderer.getCamera().getHeight();
        	
        	if (rightMouseButton){
        		renderer.moveCamera(-xDiff*((renderer.getZoomLevel()*aspect)/renderer.getContext().getSettings().getWidth())*2, -yDiff*(renderer.getZoomLevel()/renderer.getContext().getSettings().getHeight())*2, true);
        		this.dragStartPos.x = this.renderer.getInputManager().getCursorPosition().x;
				this.dragStartPos.y = this.renderer.getInputManager().getCursorPosition().y;
        	}
        }
      }
//-----------------------------------------------------------------------------------------------------
//----------------------------------ON Action --------------------------------------------------------
//----------------------------------------------------------------------------------------------------
	@Override
	public void onAction(String name, boolean pressed, float tpf) {
		if (name.equals("rightMouseButton")){
			if (rightMouseButton == false){
				this.dragStartPos.x = this.renderer.getInputManager().getCursorPosition().x;
				this.dragStartPos.y = this.renderer.getInputManager().getCursorPosition().y;
			}
			this.rightMouseButton = pressed;
		}
//-------------------------------------------GUI------------------------------------------------
		else if (name.equals("characterInfo")){
			renderer.getRootNode().detachChildNamed("Statistics View");
			renderer.getRootNode().detachChildNamed("Frames per second");
			System.out.println("Gui started");
			NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(renderer.getHqAssetManager(), renderer.getInputManager(), renderer.getAudioRenderer(), renderer.getGuiViewPort());
				/** Create a new NiftyGUI object */
				Nifty nifty = niftyDisplay.getNifty();
				/** Read your XML and initialize your custom ScreenController */
				nifty.fromXml("interfaces/GUI.xml", "start", new CharacterInfoController());
				// attach the Nifty display to the gui view port as a processor
				renderer.getGuiViewPort().addProcessor(niftyDisplay);
		}
		
	}
}
