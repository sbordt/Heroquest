/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.quads;

import java.util.HashMap;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.*;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.math.Vector3f;

import de.d2dev.fourseasons.resource.JmeAssetLocatorAdapter;
import de.d2dev.fourseasons.resource.ResourceLocator;

/**
 *
 * @author Justus
 */
public class JmeRenderer extends SimpleApplication implements QuadRenderer, QuadRenderModelListener {
	
	// Variablen die die Cameraeinstellungen bestimmen
	private float cameraSpeed = 2f;
    private float zoomLevel = 5.0f;
    private float zoomSpeed = 3f;
    
    
    // Spielfeldvariablen
    QuadRenderModel quadRenderModel = null;
    HashMap<RenderQuad, Geometry> geometrics = new HashMap<RenderQuad, Geometry>();
    
    
    ResourceLocator resourceLocator;
    
    public JmeRenderer (QuadRenderModel model, ResourceLocator locator) {
        super();
        
        this.quadRenderModel = model;
        this.resourceLocator = locator;
    }
    /** 
     * zeichnet den übergebenen Spielfeldstatus(StupidRenderModel) als 3D Szene.
     * 
     */
     
    private void createMap (QuadRenderModel map){
       
    	// Wenn eine neue Map erstellt wird soll sie zentriert werden
    	cam.setLocation(cam.getLocation().add(new Vector3f(map.getWidth()/2, map.getHeight()/2, 0)));
    	// Für jedes Quad des QuadRenderModels wird ein Quad erstellt mit der Textur versehen
    	// und zum rootNode hinzugefügt
        for (int i = 0; i < map.getQuads().size(); i++){
            
        	// Das Quad wird in seiner Größe erstellt
        	Quad quad = new Quad(map.getQuads().get(i).getWidth(),map.getQuads().get(i).getHeight());
            Geometry geom = new Geometry("Quad", quad);
            // Das Quad wird im Geometry bewegt
            geom.move(map.getQuads().get(i).getX(), map.getQuads().get(i).getY(), map.getQuads().get(i).getZLayer());
            // Ein Material mit der zum Quad gehörenden Textur wird erzeugt und dem Geometry hinzugefügt
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            Texture texture = assetManager.loadTexture( map.getQuads().get(i).getTexture().getName() );
            mat.setTexture("ColorMap", texture);
            geom.setMaterial(mat);
            // Das Geometrie Object wird dem rootnode hinzugefügt
            this.rootNode.attachChild(geom);
            // Damit später auf die Quads zugefriffen werden kann werden diese mit dem zugehörigen
            // RenderQuad als Schlüssel gespeichert 
            geometrics.put(map.getQuads().get(i), geom);
        }
    }
    
    
    /**
     * initialisiert die Tastenbelegungen für einfache Navigation in der Szene.
     */
    
    private void initKeys(){
       flyCam.setEnabled(paused);
       inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
       inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
       inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
       inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
       inputManager.addMapping("zoomOut", new KeyTrigger(KeyInput.KEY_O));
       inputManager.addMapping("zoomIn", new KeyTrigger(KeyInput.KEY_I));
       
       inputManager.addListener(analogListener, new String[]{"left", "right", "up", "down", "zoomOut", "zoomIn"});
    }
    
    private AnalogListener analogListener = new AnalogListener() {
    
    public void onAnalog(String name, float value, float tpf) {
        // Die Pfeil Rechts Taste soll die Kamera nach rechts bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	if (name.equals("right")) {
        	float rechterBildschirmrand_XPos = (cam.getLocation().x + (cam.getFrustumRight() - cam.getFrustumLeft())/2);
        	// hier wird überprüft ob der neue Kameraausschnitt den Speilfeldbereich verlassen würde
        	if (rechterBildschirmrand_XPos + cameraSpeed*tpf*zoomLevel <= quadRenderModel.getWidth()){
        		// Die Kamera kann nun bewegt werden
        		cam.setLocation(cam.getLocation().add(new Vector3f(cameraSpeed*tpf*zoomLevel, 0, 0)));	
        	}
        }
    	// Die Pfeil links Taste soll die Kamera nach links bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	else if (name.equals("left")) {
        	float linkerBildschirmrand_XPos = (cam.getLocation().x - (cam.getFrustumRight() - cam.getFrustumLeft())/2);
        	// hier wird überprüft ob der neue Kameraausschnitt den Speilfeldbereich verlassen würde
        	if (linkerBildschirmrand_XPos - cameraSpeed*tpf*zoomLevel >= 0){
        		// Die Kamera kann nun bewegt werden
        		cam.setLocation(cam.getLocation().add(new Vector3f(-cameraSpeed*tpf*zoomLevel, 0, 0)));	
    		}
        }
    	// Die Pfeil oben Taste soll die Kamera nach oben bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	else if (name.equals("up")) {
        	float obererBildschirmrand_YPos = cam.getLocation().y + (cam.getFrustumTop() - cam.getFrustumBottom())/2;
        	// hier wird überprüft ob der neue Kameraausschnitt den Speilfeldbereich verlassen würde
        	if (obererBildschirmrand_YPos + cameraSpeed*tpf*zoomLevel <= quadRenderModel.getHeight()){
        		// Die Kamera kann nun bewegt werden
        		cam.setLocation(cam.getLocation().add(new Vector3f(0, cameraSpeed*tpf*zoomLevel, 0)));
        	}
        }
    	// Die Pfeil unten Taste soll die Kamera nach unten bewegen
    	// aber es soll überprüft werden, dass das Kamerabild die Map nicht verlässt
    	else if (name.equals("down")) {
    		float untererBildschirmrand_YPos = cam.getLocation().y - (cam.getFrustumTop() - cam.getFrustumBottom())/2;
        	// hier wird überprüft ob der neue Kameraausschnitt den Speilfeldbereich verlassen würde
        	if (untererBildschirmrand_YPos - cameraSpeed*tpf*zoomLevel >= 0){
        		// Die Kamera kann nun bewegt werden
        		cam.setLocation(cam.getLocation().add(new Vector3f(0, -cameraSpeed*tpf*zoomLevel, 0)));	
        	}
        }
        else if (name.equals("zoomOut")){
        	float aspect = (float) cam.getWidth() / cam.getHeight();
            // Wenn keiner den Kameraränder den Bildschirm verlassen würde wird normal gezoomt
        	if (
            	// Es würde den links Speilfeldrand nicht überschreiten / ist rechts nicht am rand
            	((cam.getLocation().x - (zoomLevel + zoomSpeed*tpf)*aspect) > 0)&&
            	// Es darf nicht rechts aus dem Bild gezoomt werden
            	((cam.getLocation().x + (zoomLevel + zoomSpeed*tpf)*aspect) < quadRenderModel.getWidth())&&
            	// Es darf nicht oben aus dem Bild gezoomt werden
            	((cam.getLocation().y + (zoomLevel + zoomSpeed*tpf)) < quadRenderModel.getHeight())&&
            	// Es darf nicht unten aus dem Bild gezoomt werden
            	((cam.getLocation().y - (zoomLevel + zoomSpeed*tpf)) > 0)
            	){
            	zoomLevel = zoomLevel + zoomSpeed*tpf;
            	cam.setFrustum( -100, 1000, -zoomLevel * aspect, zoomLevel * aspect, zoomLevel, -zoomLevel );
            }
        	// Wenn 
        	else{
        		// Wenn es links am rand ist und rechts nicht versuche es nach rechts zu bewegen
        		if (
        			(((cam.getLocation().x - (zoomLevel + zoomSpeed*tpf)*aspect) > 0) != true)&&
        			((cam.getLocation().x + (zoomLevel + zoomSpeed*tpf)*aspect) < quadRenderModel.getWidth())
        			){
        			moveCamera (1, 0, true);
        		}
        		// Wenn es rechts am rand ist und links nicht versuche es nach links zu bewegen
        		if (
        			(((cam.getLocation().x + (zoomLevel + zoomSpeed*tpf)*aspect) < quadRenderModel.getWidth()) != true)&&
        			((cam.getLocation().x - (zoomLevel + zoomSpeed*tpf)*aspect) > 0)
        			){
        			moveCamera (-1, 0, true);
        		}
        		// Wenn es oben am rand ist und unten nicht versuche es nach unten zu bewegen
        		if (
        			(((cam.getLocation().y + (zoomLevel + zoomSpeed*tpf)) < quadRenderModel.getHeight())!= true)&&
        			((cam.getLocation().y - (zoomLevel + zoomSpeed*tpf)) > 0)
        			){
        			moveCamera (0, -1, true);
        		}
        		// Wenn es unten am rand ist und oben nicht versuche es nach oben zu bewegen
        		if (
        			(((cam.getLocation().y - (zoomLevel + zoomSpeed*tpf)) > 0)!= true)&&
        			((cam.getLocation().y + (zoomLevel + zoomSpeed*tpf)) < quadRenderModel.getHeight())
        			){
        			moveCamera (0, 1, true);
        		}
        	}
        }
        else if (name.equals("zoomIn")){
            zoomLevel = zoomLevel - zoomSpeed*tpf;
            float aspect = (float) cam.getWidth() / cam.getHeight();
            cam.setFrustum( -100, 1000, -zoomLevel * aspect, zoomLevel * aspect, zoomLevel, -zoomLevel );
        }
        System.out.println("Höhe: " + (cam.getFrustumTop() - cam.getFrustumBottom()));
        System.out.println("Breite: " + (cam.getFrustumRight() - cam.getFrustumLeft()));
        System.out.println("X: " + cam.getLocation().x);
        System.out.println("Y: " + cam.getLocation().y);
        System.out.println("Z: " + cam.getLocation().z);
        System.out.println("Linker Rand: " + (cam.getLocation().x - (cam.getFrustumRight() - cam.getFrustumLeft())/2));
        System.out.println("rechter Rand: " + (cam.getLocation().x + (cam.getFrustumRight() - cam.getFrustumLeft())/2));
        System.out.println("Unterer Rand: " + (cam.getLocation().y - (cam.getFrustumTop() - cam.getFrustumBottom())/2));
        System.out.println("Oberer Rand: " + (cam.getLocation().y + (cam.getFrustumTop() - cam.getFrustumBottom())/2));
      }
   };
    
    /**
     * Setzt die Kameraprojektionsart auf parallele Projektion und passt die restlichen Werte an.
     */
    private void setCamToParallelProjektion (){
        // Die Kamera muss auf Parallele Projektion eingestellt werden
        cam.setParallelProjection(true);
        // und die Clipping ebenen müssen an das display angepasst werde
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum( -100, 1000, -zoomLevel * aspect, zoomLevel * aspect, zoomLevel, -zoomLevel );
        cam.update();
    }   
    
    
    /**
     * Muss überschrieben werden, da sie abstract ist.
     */
    @Override
    public void simpleInitApp() {
    	JmeAssetLocatorAdapter.locators.put("argh", this.resourceLocator );	// ugly hack
    	this.assetManager.registerLocator( "argh", new JmeAssetLocatorAdapter().getClass() );
    	
    	initKeys();
        setCamToParallelProjektion (); 
        createMap(this.quadRenderModel);
    }
    
	@Override
	public void setRenderModel(QuadRenderModel m) {
		this.quadRenderModel = m;
		rootNode.detachAllChildren();
		this.createMap(m);
	}
	@Override
	public QuadRenderModel getRenderModel() {
		return this.quadRenderModel;
	}
	@Override
	public void onAddQuad(RenderQuad quad) {
		this.quadRenderModel.addQuad(quad);
		// Das Quad wird in seiner Größe erstellt
    	Quad jmeQuad = new Quad(quad.getWidth(),quad.getHeight());
        Geometry geom = new Geometry("Quad", jmeQuad);
        // Das Quad wird im Geometry bewegt
        geom.move(quad.getX(), quad.getY(), quad.getZLayer());
        // Ein Material mit der zum Quad gehörenden Textur wird erzeugt und dem Geometry hinzugefügt
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = assetManager.loadTexture( quad.getTexture().getName() );
        mat.setTexture("ColorMap", texture);
        geom.setMaterial(mat);
        // Das Geometrie Object wird dem rootnode hinzugefügt
        this.rootNode.attachChild(geom);
        // Damit später auf die Quads zugefriffen werden kann werden diese mit dem zugehörigen
        // RenderQuad als Schlüssel gespeichert 
        geometrics.put(quad, geom);
	}
	@Override
	public void onRemoveQuad(RenderQuad quad) {
		rootNode.detachChild(geometrics.get(quad));
		geometrics.remove(quad);
	}
	@Override
	public void onQuadMoved(RenderQuad quad) {
		geometrics.get(quad).move(quad.getX(), quad.getY(), quad.getZLayer());
	}
	
	@Override
	public void onQuadTextureChanged(RenderQuad quad) {
		Texture texture = assetManager.loadTexture( quad.getTexture().getName() );
		geometrics.get(quad).getMaterial().setTexture("ColorMap", texture);
	}
	
	public void moveCamera(float x, float y, boolean dontLeaveMap){
		
		// Ein Temporärer Vector zum Bewegen wird erzeugt
		Vector3f tempVec = new Vector3f(x, y, 0);
		
		if (dontLeaveMap){
			
			// rechter Bildrand darf die Map nicht verlassen
			float rechterBildrand = (cam.getLocation().x + (cam.getFrustumRight() - cam.getFrustumLeft())/2);
			if (rechterBildrand + x > quadRenderModel.getWidth())
				tempVec.x = quadRenderModel.getWidth() - rechterBildrand ;
			
			// linker Bildrand darf die Map nicht verlassen
			float linkerBildrand = (cam.getLocation().x - (cam.getFrustumRight() - cam.getFrustumLeft())/2);
			if (linkerBildrand + x < 0)
				tempVec.x = 0 - linkerBildrand;
			
			// oberer Bildrand darf die Map nicht verlassen
			float obererBildrand = (cam.getLocation().y + (cam.getFrustumTop() - cam.getFrustumBottom())/2);
			if (obererBildrand + y > quadRenderModel.getHeight())
				tempVec.y = quadRenderModel.getHeight() - obererBildrand ;
			
			// rechter Bildrand darf die Map nicht verlassen
			float untererBildrand = (cam.getLocation().y - (cam.getFrustumTop() - cam.getFrustumBottom())/2);
			if (untererBildrand + y < 0)
				tempVec.y = 0 - untererBildrand ;
		}
		// Die Eigentliche Bewegung der Kamera um den ev. angepassten Vector
		cam.setLocation(cam.getLocation().add(tempVec));
	}
}
