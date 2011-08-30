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
public class JmeRenderer extends SimpleApplication implements QuadRenderer, QuadRenderModelListener, JmeResizeableApp {
	
	// Variablen die die Cameraeinstellungen bestimmen
	private float cameraSpeed = 2f;
    private float zoomLevel = 5.0f;
    private float zoomSpeed = 3f;

    
	// Spielfeldvariablen
    QuadRenderModel quadRenderModel = null;
	HashMap<RenderQuad, Geometry> geometrics = new HashMap<RenderQuad, Geometry>();
    
    // Listener
    JmeUserInputListener userListener;
    
    
    ResourceLocator resourceLocator;
    
    public JmeRenderer (QuadRenderModel model, ResourceLocator locator) {
        super();
        
        this.quadRenderModel = model;
        this.resourceLocator = locator;
        userListener = new JmeUserInputListener (this);
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
       
       inputManager.addListener(userListener, new String[]{"left", "right", "up", "down", "zoomOut", "zoomIn"});
    }
    
    
    
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
	
	@Override
	public void onResize(int width, int height) {
		
	}	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////// Getter & Setter /////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setCameraSpeed(float cameraSpeed) {
		this.cameraSpeed = cameraSpeed;
	}
	public float getCameraSpeed() {
		return cameraSpeed;
	}
	public void setZoomSpeed(float zoomSpeed) {
		this.zoomSpeed = zoomSpeed;
	}
	public float getZoomSpeed() {
		return zoomSpeed;
	}
	public float getZoomLevel() {
		return zoomLevel;
	}
	public void setZoomLevel(float zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	public QuadRenderModel getQuadRenderModel() {
		return quadRenderModel;
	}
	public void setQuadRenderModel(QuadRenderModel quadRenderModel) {
		this.quadRenderModel = quadRenderModel;
	}
}
