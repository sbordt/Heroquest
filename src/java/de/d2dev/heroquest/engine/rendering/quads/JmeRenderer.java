/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.quads;

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
public class JmeRenderer extends SimpleApplication implements QuadRenderer {
   
    // Variablen die die Cameraeinstellungen bestimmen
    private float cameraSpeed = 0.01f;
    private float zoomLevel = 5.0f;
    private float zoomSpeed = 0.01f;
    
    QuadRenderModel model;
    ResourceLocator resourceLocator;
    
    public JmeRenderer (QuadRenderModel model, ResourceLocator locator) {
        super();
        
        this.model = model;
        this.resourceLocator = locator;
    }
    /** 
     * zeichnet den übergebenen Spielfeldstatus(StupidRenderModel) als 3D Szene.
     * 
     */
     
    private void createMap (QuadRenderModel map){
       
        for (int i = 0; i < map.getQuads().size(); i++){
            System.out.println(map.getQuads().size());
            System.out.println (map.getQuads().get(i).getTexture());
            Quad quad = new Quad(map.getQuads().get(i).getWidth(),map.getQuads().get(i).getHeight());
            Geometry geom = new Geometry("Quad", quad);
            geom.move(map.getQuads().get(i).getX(), map.getQuads().get(i).getY(), map.getQuads().get(i).getZLayer());
            System.out.println ("Vor dem Mat erstellen");
            if (assetManager != null)
                System.out.println ("null nicht");
            else
                System.out.println ("null");
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            System.out.println ("danach");
            
            Texture texture = assetManager.loadTexture( map.getQuads().get(i).getTexture().getName() );
            
            System.out.println ("nach textur");
            mat.setTexture("ColorMap", texture);
            geom.setMaterial(mat);
            rootNode.attachChild(geom); 
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
        if (name.equals("right")) 
            cam.setLocation(cam.getLocation().add(new Vector3f(cameraSpeed, 0, 0)));
        else if (name.equals("left")) 
            cam.setLocation(cam.getLocation().add(new Vector3f(-cameraSpeed, 0, 0)));
        else if (name.equals("up")) 
            cam.setLocation(cam.getLocation().add(new Vector3f(0, cameraSpeed, 0)));
        else if (name.equals("down")) 
            cam.setLocation(cam.getLocation().add(new Vector3f(0, -cameraSpeed, 0)));
        else if (name.equals("zoomOut")){
            zoomLevel = zoomLevel - zoomSpeed;
            System.out.println (zoomLevel);
            float aspect = (float) cam.getWidth() / cam.getHeight();
            cam.setFrustum( -100, 1000, -zoomLevel * aspect, zoomLevel * aspect, zoomLevel, -zoomLevel );
        }
        else if (name.equals("zoomIn")){
            zoomLevel = zoomLevel + zoomSpeed;
            System.out.println (zoomLevel);
            float aspect = (float) cam.getWidth() / cam.getHeight();
            cam.setFrustum( -100, 1000, -zoomLevel * aspect, zoomLevel * aspect, zoomLevel, -zoomLevel );
        }
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
        //cam.setLocation(new Vector3f(0.0f, 0.0f, 0.0f));
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
        createMap(model);
    }
    
	@Override
	public void setRenderModel(QuadRenderModel m) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public QuadRenderModel getRenderModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
