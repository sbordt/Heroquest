package de.d2dev.heroquest.engine.rendering.quads;

import com.jme3.input.controls.AnalogListener;

public class JmeUserInputListener implements AnalogListener {
	private JmeRenderer renderer = null;
	
	public JmeUserInputListener (JmeRenderer renderer){
		this.renderer = renderer;
	}
	
	public void onAnalog(String name, float value, float tpf) {
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
        else if (name.equals("zoomOut")){
        	float aspect = (float) renderer.getCamera().getWidth() / renderer.getCamera().getHeight();
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
            	renderer.getCamera().setFrustum( -100, 1000, -renderer.getZoomLevel() * aspect, renderer.getZoomLevel() * aspect, renderer.getZoomLevel(), -renderer.getZoomLevel() );
            }
        	// Wenn 
        	else{
        		// Wenn es links am rand ist und rechts nicht versuche es nach rechts zu bewegen
        		if (
        			(((renderer.getCamera().getLocation().x - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) > 0) != true)&&
        			((renderer.getCamera().getLocation().x + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) < renderer.getQuadRenderModel().getWidth())
        			){
        			renderer.moveCamera (1, 0, true);
        		}
        		// Wenn es rechts am rand ist und links nicht versuche es nach links zu bewegen
        		if (
        			(((renderer.getCamera().getLocation().x + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) < renderer.getQuadRenderModel().getWidth()) != true)&&
        			((renderer.getCamera().getLocation().x - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)*aspect) > 0)
        			){
        			renderer.moveCamera (-1, 0, true);
        		}
        		// Wenn es oben am rand ist und unten nicht versuche es nach unten zu bewegen
        		if (
        			(((renderer.getCamera().getLocation().y + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) < renderer.getQuadRenderModel().getHeight())!= true)&&
        			((renderer.getCamera().getLocation().y - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) > 0)
        			){
        			renderer.moveCamera (0, -1, true);
        		}
        		// Wenn es unten am rand ist und oben nicht versuche es nach oben zu bewegen
        		if (
        			(((renderer.getCamera().getLocation().y - (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) > 0)!= true)&&
        			((renderer.getCamera().getLocation().y + (renderer.getZoomLevel() + renderer.getZoomSpeed()*tpf)) < renderer.getQuadRenderModel().getHeight())
        			){
        			renderer.moveCamera (0, 1, true);
        		}
        	}
        }
        else if (name.equals("zoomIn")){
            renderer.setZoomLevel(renderer.getZoomLevel() - renderer.getZoomSpeed()*tpf);
            float aspect = (float) renderer.getCamera().getWidth() / renderer.getCamera().getHeight();
            renderer.getCamera().setFrustum( -100, 1000, -renderer.getZoomLevel() * aspect, renderer.getZoomLevel() * aspect, renderer.getZoomLevel(), -renderer.getZoomLevel() );
        }
        /*System.out.println("Höhe: " + (cam.getFrustumTop() - cam.getFrustumBottom()));
        System.out.println("Breite: " + (cam.getFrustumRight() - cam.getFrustumLeft()));
        System.out.println("X: " + cam.getLocation().x);
        System.out.println("Y: " + cam.getLocation().y);
        System.out.println("Z: " + cam.getLocation().z);
        System.out.println("Linker Rand: " + (cam.getLocation().x - (cam.getFrustumRight() - cam.getFrustumLeft())/2));
        System.out.println("rechter Rand: " + (cam.getLocation().x + (cam.getFrustumRight() - cam.getFrustumLeft())/2));
        */System.out.println("Unterer Rand: " + (renderer.getCamera().getLocation().y - (renderer.getCamera().getFrustumTop() - renderer.getCamera().getFrustumBottom())/2));
        System.out.println("Oberer Rand: " + (renderer.getCamera().getLocation().y + (renderer.getCamera().getFrustumTop() - renderer.getCamera().getFrustumBottom())/2));
        /*System.out.println("Cursor Position: " + inputManager.getCursorPosition());*/
      }
}
